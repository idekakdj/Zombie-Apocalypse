import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Super class for survivors
 * 
 * @author Paul with assistance from Claude 
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
    protected boolean hasBat = false; // ensures bat is only spawned once
    protected boolean hasShield = false;
    protected boolean hasBandages = false;
    
    private static final int ANGLE_SAMPLES = 36; 
    private static final int LOOK_AHEAD_DISTANCE = 100; 
    
    public void act()
    {
        getUserItems();
        if (melee && !hasBat) {
            spawnBat();
        } else if (shield && !hasShield){
            spawnShield();
        } else if(bandages && !hasBandages){
            
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
    
    // Movement algorithm from Claude
    protected void moveIntelligently(int speed) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> allZombies = world.getObjects(Zombie.class);
        List<Zombie> nearbyZombies = getObjectsInRange(DETECTION, Zombie.class);
        
        // Only move if zombies are within detection range
        if (nearbyZombies.isEmpty()) {
            return;
        }
        
        // Find the safest direction to move
        int bestAngle = findSafestDirection(allZombies, world, speed);
        
        // Move in the safest direction if it keeps us in bounds
        if (bestAngle != -1) {
            setRotation(bestAngle);
            move(speed);
        }
    }
    //From Claude
    // Calculates the safest direction considering all zombies and boundaries
    private int findSafestDirection(List<Zombie> zombies, GameWorld world, int speed) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestAngle = -1;
        
        // Test movement in multiple directions
        for (int i = 0; i < ANGLE_SAMPLES; i++) {
            int testAngle = (360 / ANGLE_SAMPLES) * i;
            
            // Calculate where we would be if we moved in this direction
            int projectedX = (int)(getX() + speed * Math.cos(Math.toRadians(testAngle - 90)));
            int projectedY = (int)(getY() + speed * Math.sin(Math.toRadians(testAngle - 90)));
            
            // Skip if this direction takes us out of bounds
            if (!world.isValidPosition(projectedX, projectedY)) {
                continue;
            }
            
            // Calculate safety score for this direction
            double score = calculateSafetyScore(projectedX, projectedY, zombies);
            
            if (score > bestScore) {
                bestScore = score;
                bestAngle = testAngle;
            }
        }
        
        return bestAngle;
    }
    
    // From Claude: Calculates how safe a position is based on zombie distances
    // Higher score = safer position (further from zombies, especially close ones)
    private double calculateSafetyScore(int x, int y, List<Zombie> zombies) {
        double totalScore = 0;
        
        for (Zombie zombie : zombies) {
            double distance = Math.sqrt(
                Math.pow(zombie.getX() - x, 2) + 
                Math.pow(zombie.getY() - y, 2)
            );
            
            // Claude: Closer zombies have exponentially more weight
            // Using inverse square to heavily prioritize avoiding close zombies
            if (distance < 1) distance = 1; // Prevent division by zero
            double weight = 1.0 / (distance * distance);
            
            // Claude: Further away = better score
            // We add the distance but subtract the weight to prioritize avoiding close zombies
            totalScore += distance - (weight * 10000);
        }
        
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
        if (hasBat) return; // make sure we only spawn it once
        World w = getWorld();
        if (w != null) {
            Bat bat = new Bat(50, 50, 30, this);
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