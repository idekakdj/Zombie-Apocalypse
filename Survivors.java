import greenfoot.*;
import java.util.List;
/*
 * 
 * @author Paul assisted by Claude (all movement related stuff was improved by Claude)
 * 
 
 */
public abstract class Survivors extends SuperSmoothMover
{
    protected int startHP;
    protected int hp;
    protected final int DETECTION = 100;
    protected boolean melee = false;
    protected boolean gun = false;
    protected boolean shield = false;
    protected boolean bandages = false;
    protected boolean wall = false;
    protected boolean hasBat = false;
    protected boolean hasShield = false;
    protected boolean hasBandages = false;
    
    // Claude: Updated movement parameters
    private static final int ANGLE_SAMPLES = 72;
    private static final int TRAPPED_THRESHOLD = 3;
    private static final int MIN_SPEED = 1; 
    private int stuckCounter = 0;
    private int lastX = 0;
    private int lastY = 0;
    
    public void act()
    {
        getUserItems();
        if (melee && !hasBat) {
            spawnBat();
        } else if (shield && !hasShield){
            spawnShield();
        } else if(bandages && !hasBandages){
            spawnBandages();
        }
    }
    
    public void getUserItems(){
        World w = getWorld();
        if (w instanceof GameWorld) {
            GameWorld world = (GameWorld) w;
            melee = world.melee;
            gun = world.gun;
            shield = world.shield;
            bandages = world.bandages;
            wall = world.wall;
        }
    }
    
    //From Claude
    public int getAngleTowards(Actor target)
    {
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        
        // Convert to degrees (0 = right, 90 = down, 180 = left, 270 = up)
        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) Math.toDegrees(angleInRadians);
        
        // Adjust to Greenfoot's coordinate system (0 = up/north)
        angleInDegrees = (angleInDegrees + 90) % 360;
        if (angleInDegrees < 0) angleInDegrees += 360;
        
        return angleInDegrees;
    }
    
    // Claude: Completely rewritten movement algorithm
    protected void moveIntelligently(int speed) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> allZombies = world.getObjects(Zombie.class);
        List<Zombie> nearbyZombies = getObjectsInRange(DETECTION, Zombie.class);
        
        // Claude: Only move if zombies are within detection range
        if (nearbyZombies.isEmpty()) {
            return;
        }
        
        // Claude: Check if stuck (haven't moved much in last few frames)
        checkIfStuck();
        
        int bestAngle = findBestEscapeDirectionIterative(allZombies, world, speed);
        
        // Claude: If completely trapped, force movement toward center of boundary
        if (bestAngle == -1 || stuckCounter > TRAPPED_THRESHOLD) {
            bestAngle = moveTowardCenter(world);
            stuckCounter = 0;
        }
        
        // Claude: Always move if we found a valid direction
        if (bestAngle != -1) {
            setRotation(bestAngle);
            move(speed);
        }
    }
    
    // Claude: Tracks if survivor is stuck in same position
    private void checkIfStuck() {
        if (Math.abs(getX() - lastX) < 2 && Math.abs(getY() - lastY) < 2) {
            stuckCounter++;
        } else {
            stuckCounter = 0;
        }
        lastX = getX();
        lastY = getY();
    }
    
    // Claude: Finds direction toward center of boundary to escape corners
    private int moveTowardCenter(GameWorld world) {
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        int dx = centerX - getX();
        int dy = centerY - getY();
        
        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) Math.toDegrees(angleInRadians);
        angleInDegrees = (angleInDegrees + 90) % 360;
        if (angleInDegrees < 0) angleInDegrees += 360;
        
        return angleInDegrees;
    }
    
    private int findBestEscapeDirectionIterative(List<Zombie> zombies, GameWorld world, int initialSpeed) {
        int currentSpeed = initialSpeed;
        
        // Try progressively smaller speeds until we find a valid move or reach minimum
        while (currentSpeed >= MIN_SPEED) {
            double bestScore = Double.NEGATIVE_INFINITY;
            int bestAngle = -1;
            
            // Test many directions for better pathfinding
            for (int i = 0; i < ANGLE_SAMPLES; i++) {
                int testAngle = (360 / ANGLE_SAMPLES) * i;
                
                // Project position for this direction
                int projectedX = (int)(getX() + currentSpeed * Math.cos(Math.toRadians(testAngle - 90)));
                int projectedY = (int)(getY() + currentSpeed * Math.sin(Math.toRadians(testAngle - 90)));
                
                // Calculate safety score
                double score = calculateAdvancedSafetyScore(projectedX, projectedY, zombies, world);
                
                // Only consider in-bounds moves
                if (world.isValidPosition(projectedX, projectedY)) {
                    if (score > bestScore) {
                        bestScore = score;
                        bestAngle = testAngle;
                    }
                }
            }
            
            // If we found a valid angle, return it
            if (bestAngle != -1) {
                return bestAngle;
            }
            
            // Otherwise, try with smaller speed
            currentSpeed = currentSpeed / 2;
        }
        
        // If no valid move found even at minimum speed, return -1
        return -1;
    }
    
    // Claude: Enhanced safety calculation with multiple factors
    private double calculateAdvancedSafetyScore(int x, int y, List<Zombie> zombies, GameWorld world) {
        double totalScore = 0;
        
        // Claude: Calculate distance from all zombies
        for (Zombie zombie : zombies) {
            double distance = Math.sqrt(
                Math.pow(zombie.getX() - x, 2) + 
                Math.pow(zombie.getY() - y, 2)
            );
            
            // Claude: Exponential penalty for close zombies
            if (distance < 1) distance = 1;
            double proximityPenalty = 50000.0 / (distance * distance);
            
            // Claude: Linear bonus for being far away
            double distanceBonus = distance * 10;
            
            totalScore += distanceBonus - proximityPenalty;
        }
        
        // Claude: Bonus for staying in bounds
        if (world.isValidPosition(x, y)) {
            totalScore += 10000;
        }
        
        // Claude: Penalty for being near edges (to avoid corners)
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        double distanceFromCenter = Math.sqrt(
            Math.pow(x - centerX, 2) + 
            Math.pow(y - centerY, 2)
        );
        
        // Claude: Slight preference for center area
        totalScore += (300 - Math.min(distanceFromCenter, 300)) * 5;
        
        // Claude: Check future positions - penalize directions leading to traps
        int futureZombiesNearby = 0;
        for (Zombie zombie : zombies) {
            double futureDistance = Math.sqrt(
                Math.pow(zombie.getX() - x, 2) + 
                Math.pow(zombie.getY() - y, 2)
            );
            if (futureDistance < 50) {
                futureZombiesNearby++;
            }
        }
        totalScore -= futureZombiesNearby * 5000;
        
        return totalScore;
    }
    
    public abstract void takeDamage(int damage);
    
    public int getHP() {
        return hp;
    }
    
    public int getMaxHP() {
        return startHP;
    }
    
    public void heal(){
        hp += 25;
    }
    
    protected void spawnBat() {
        if (hasBat) return;
        World w = getWorld();
        if (w != null) {
            Bat bat = new Bat(50, 100, 120, this);
            w.addObject(bat, getX(), getY());
            hasBat = true;
        }
    }
    
    protected void spawnShield() {
        if (hasShield) return; 
        World w = getWorld();
        if (w != null) {
            Shield shield = new Shield(this);
            w.addObject(shield, getX(), getY() );
            hasShield = true;
        }
    }
    
    protected void spawnBandages(){
        if (hasBandages) return;
        World w = getWorld();
        if (w != null) {
            Bandages bandages = new Bandages(this);
            w.addObject(bandages, getX(), getY() );
            hasBandages = true;
        }
    }
}
