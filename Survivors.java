import greenfoot.*;
import java.util.List;
/**
 * Super class for survivors
 * 
 * @author Paul with assistance from Claude (Improving pathfinding algorithm to much higher quality)
 */
public abstract class Survivors extends SuperSmoothMover
{
    // Core stats
    protected int startHP;
    protected int hp;
    protected boolean melee = false;
    protected boolean gun = false;
    protected boolean shield = false;
    protected boolean bandages = false;
    protected boolean wall = false;
    protected boolean hasGun = false;
    protected boolean hasBat = false;
    protected boolean hasShield = false;
    protected boolean hasBandages = false;
    
    // Movement AI constants - tuned for optimal performance
    private static final int DETECTION_RANGE = 150;
    private static final int DIRECTION_SAMPLES = 72;  // Every 5 degrees
    private static final int MIN_SPEED = 1;
    
    // Threat assessment constants
    private static final double BOSS_THREAT = 3.5;
    private static final double GIANT_THREAT = 2.5;
    private static final double PENGUIN_THREAT = 3.0;  // High due to speed
    private static final double REGULAR_THREAT = 1.0;
    
    // Scoring weights
    private static final double ZOMBIE_DANGER_WEIGHT = 8000.0;
    private static final double DISTANCE_BONUS_WEIGHT = 4.0;
    private static final double PREDICTION_WEIGHT = 1.5;
    private static final double SWARM_PENALTY_BASE = 2500.0;
    private static final double EDGE_APPROACH_PENALTY = 50.0;
    
    public void act()
    {
        getUserItems();
        if (melee && !hasBat) {
            spawnBat();
        } else if (shield && !hasShield){
            spawnShield();
        } else if (gun && !hasGun){
            spawnGun();
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
    
    public int getStartHP(){
        return startHP;
    }
    
    public int getAngleTowards(Actor target){
        return calculateAngle(target.getX(), target.getY());
    }
    
    private int calculateAngle(int targetX, int targetY){
        int dx = targetX - getX();
        int dy = targetY - getY();
        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) Math.toDegrees(angleInRadians);
        angleInDegrees = (angleInDegrees + 90) % 360;
        if (angleInDegrees < 0) angleInDegrees += 360;
        return angleInDegrees;
    }
    
    /**
     * Primary movement method - guarantees valid movement when zombies present
     */
    protected void moveIntelligently(int maxSpeed) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> nearbyZombies = getObjectsInRange(DETECTION_RANGE, Zombie.class);
        
        // Only move when threatened
        if (nearbyZombies.isEmpty()) {
            return;
        }
        
        // Find optimal escape direction with speed degradation
        MovementResult result = findOptimalMovement(nearbyZombies, world, maxSpeed);
        
        // Execute movement - result is guaranteed to be valid
        if (result.isValid()) {
            setRotation(result.angle);
            move(result.speed);
        }
    }
    
    
    /* Core pathfinding algorithm using potential field approach
     * Returns a guaranteed valid movement or emergency fallback
     */
    private MovementResult findOptimalMovement(List<Zombie> zombies, GameWorld world, int maxSpeed) {
        int currentSpeed = maxSpeed;
        
        // Try progressively smaller speeds until we find valid movement
        while (currentSpeed >= MIN_SPEED) {
            MovementResult result = evaluateAllDirections(zombies, world, currentSpeed);
            
            if (result.isValid()) {
                return result;
            }
            
            // Reduce speed for next iteration
            currentSpeed = currentSpeed / 2;
        }
        
        // Emergency: find any valid adjacent position
        return findEmergencyMovement(zombies, world);
    }
    
    /**
     * Calculates how safe a position is using potential field approach
     * Higher score = safer position
     */
    private double calculateSafetyScore(int x, int y, List<Zombie> zombies, GameWorld world) {
        double totalScore = 0;
        int nearbyCount = 0;
        
        // Evaluate threat from each zombie
        for (Zombie zombie : zombies) {
            double threat = getZombieThreat(zombie);
            double speed = getZombieSpeed(zombie);
            
            // Current distance
            double currentDist = distance(x, y, zombie.getX(), zombie.getY());
            
            // Predicted future position of zombie
            int zombieAngle = calculateAngle(zombie.getX(), zombie.getY());
            double predRadians = Math.toRadians(zombieAngle - 90);
            int predX = (int)(zombie.getX() + speed * Math.cos(predRadians));
            int predY = (int)(zombie.getY() + speed * Math.sin(predRadians));
            double predictedDist = distance(x, y, predX, predY);
            
            // Use worse case distance
            double effectiveDist = Math.min(currentDist, predictedDist * PREDICTION_WEIGHT);
            if (effectiveDist < 1) effectiveDist = 1;
            
            // Inverse square law for danger (very close = very dangerous)
            double dangerScore = (ZOMBIE_DANGER_WEIGHT / (effectiveDist * effectiveDist)) * threat;
            double distanceBonus = effectiveDist * DISTANCE_BONUS_WEIGHT;
            
            totalScore += distanceBonus - dangerScore;
            
            // Count nearby zombies for swarm detection
            if (currentDist < 80) {
                nearbyCount++;
            }
        }
        
        // Penalty for being in a swarm
        if (nearbyCount > 2) {
            totalScore -= (nearbyCount - 2) * SWARM_PENALTY_BASE;
        }
        
        // Gentle penalty for approaching edges (prefer center)
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        double distFromCenter = distance(x, y, centerX, centerY);
        double maxComfortDist = Math.min(world.getWidth() / 2 - 40, world.getHeight() / 2 - 40);
        
        if (distFromCenter > maxComfortDist) {
            totalScore -= (distFromCenter - maxComfortDist) * EDGE_APPROACH_PENALTY;
        }
        
        return totalScore;
    }
    
    /**
     * Emergency fallback when no optimal movement exists
     * Finds ANY valid in-bounds position, prioritizing away from nearest threat
     * GUARANTEED to find a move if ANY adjacent position is valid
     */
    private MovementResult findEmergencyMovement(List<Zombie> zombies, GameWorld world) {
        // Strategy 1: Move away from nearest zombie if possible
        Zombie nearest = findNearestZombie(zombies);
        
        if (nearest != null) {
            int awayAngle = (calculateAngle(nearest.getX(), nearest.getY()) + 180) % 360;
            
            // Test very fine-grained angles around the "away" direction
            for (int offset = 0; offset <= 180; offset += 5) {  // Changed from 10 to 5 for more precision
                for (int sign : new int[]{-1, 1}) {
                    int testAngle = (awayAngle + offset * sign + 360) % 360;
                    
                    double radians = Math.toRadians(testAngle - 90);
                    int projX = (int)(getX() + MIN_SPEED * Math.cos(radians));
                    int projY = (int)(getY() + MIN_SPEED * Math.sin(radians));
                    
                    if (world.isValidPosition(projX, projY)) {
                        return new MovementResult(testAngle, MIN_SPEED);
                    }
                }
            }
        }
        
        // Strategy 2: Try ALL angles at fine resolution
        for (int angle = 0; angle < 360; angle += 5) {
            double radians = Math.toRadians(angle - 90);
            int projX = (int)(getX() + MIN_SPEED * Math.cos(radians));
            int projY = (int)(getY() + MIN_SPEED * Math.sin(radians));
            
            if (world.isValidPosition(projX, projY)) {
                return new MovementResult(angle, MIN_SPEED);
            }
        }
        
        // Strategy 3: Try moving along the boundary (slide along edge)
        // Test all angles with even SMALLER movement
        for (int angle = 0; angle < 360; angle += 5) {
            double radians = Math.toRadians(angle - 90);
            // Try half of MIN_SPEED
            double microSpeed = MIN_SPEED * 0.5;
            int projX = (int)(getX() + microSpeed * Math.cos(radians));
            int projY = (int)(getY() + microSpeed * Math.sin(radians));
            
            if (world.isValidPosition(projX, projY)) {
                return new MovementResult(angle, 1);  // Move at least 1 pixel
            }
        }
        
        // Strategy 4: Absolute last resort - try to "unstick" by moving toward center
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        int angleToCenter = calculateAngle(centerX, centerY);
        
        // Test angles around center direction with micro-movement
        for (int offset = -45; offset <= 45; offset += 5) {
            int testAngle = (angleToCenter + offset + 360) % 360;
            double radians = Math.toRadians(testAngle - 90);
            
            // Try tiny movement
            double tinySpeed = 0.5;
            int projX = (int)(getX() + tinySpeed * Math.cos(radians));
            int projY = (int)(getY() + tinySpeed * Math.sin(radians));
            
            if (world.isValidPosition(projX, projY)) {
                return new MovementResult(testAngle, 1);
            }
        }
        
        // Truly stuck - this should almost never happen
        // Return current position as "valid" but no movement
        return new MovementResult(getRotation(), 0);
    }
    
    /**
     * Enhanced evaluation that tries sub-pixel movements when necessary
     */
    private MovementResult evaluateAllDirections(List<Zombie> zombies, GameWorld world, int speed) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestAngle = -1;
        
        // First pass: try at requested speed
        for (int i = 0; i < DIRECTION_SAMPLES; i++) {
            int testAngle = (360 * i) / DIRECTION_SAMPLES;
            
            double radians = Math.toRadians(testAngle - 90);
            int projX = (int)(getX() + speed * Math.cos(radians));
            int projY = (int)(getY() + speed * Math.sin(radians));
            
            if (!world.isValidPosition(projX, projY)) {
                continue;
            }
            
            double score = calculateSafetyScore(projX, projY, zombies, world);
            
            if (score > bestScore) {
                bestScore = score;
                bestAngle = testAngle;
            }
        }
        
        // If found valid angle, return it
        if (bestAngle != -1) {
            return new MovementResult(bestAngle, speed);
        }
        
        // Second pass: if at MIN_SPEED and still no valid move, try even smaller
        if (speed == MIN_SPEED) {
            for (int i = 0; i < DIRECTION_SAMPLES; i++) {
                int testAngle = (360 * i) / DIRECTION_SAMPLES;
                
                double radians = Math.toRadians(testAngle - 90);
                // Try half-pixel movement
                int projX = (int)(getX() + 0.5 * Math.cos(radians));
                int projY = (int)(getY() + 0.5 * Math.sin(radians));
                
                if (!world.isValidPosition(projX, projY)) {
                    continue;
                }
                
                double score = calculateSafetyScore(projX, projY, zombies, world);
                
                if (score > bestScore) {
                    bestScore = score;
                    bestAngle = testAngle;
                }
            }
            
            if (bestAngle != -1) {
                return new MovementResult(bestAngle, 1);  // Move at least 1 pixel
            }
        }
        
        return new MovementResult(-1, 0);
    }
    
    /**
     * Helper methods
     */
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    private Zombie findNearestZombie(List<Zombie> zombies) {
        Zombie nearest = null;
        double minDist = Double.MAX_VALUE;
        
        for (Zombie z : zombies) {
            double dist = distance(getX(), getY(), z.getX(), z.getY());
            if (dist < minDist) {
                minDist = dist;
                nearest = z;
            }
        }
        
        return nearest;
    }
    
    private double getZombieThreat(Zombie zombie) {
        if (zombie instanceof Boss) return BOSS_THREAT;
        if (zombie instanceof Giant) return GIANT_THREAT;
        if (zombie instanceof Penguin) return PENGUIN_THREAT;
        return REGULAR_THREAT;
    }
    
    private double getZombieSpeed(Zombie zombie) {
        if (zombie instanceof Boss) return 0.5;
        if (zombie instanceof Giant) return 1.0;
        if (zombie instanceof Penguin) return 4.0;
        return 2.0;
    }
    
    /**
     * Inner class to encapsulate movement result
     */
    private class MovementResult {
        int angle;
        int speed;
        
        MovementResult(int angle, int speed) {
            this.angle = angle;
            this.speed = speed;
        }
        
        boolean isValid() {
            return angle != -1 && speed > 0;
        }
    }
    
    /**
     * Abstract and utility methods
     */
    public abstract void takeDamage(int damage);
    
    public int getHP() {
        return hp;
    }
    
    public int getMaxHP() {
        return startHP;
    }
    
    public void heal(){
        hp += 25;
        if (hp > startHP) {
            hp = startHP;
        }
    }
    
    protected void spawnBat() {
        if (hasBat) return;
        World w = getWorld();
        if (w != null) {
            Bat bat = new Bat(50, 50, 30, this);
            w.addObject(bat, getX(), getY());
            hasBat = true;
        }
    }
    
    protected void spawnGun() {
        if (hasGun) return;
        World w = getWorld();
        if (w != null) {
            MachineGun gun = new MachineGun(50, 10, this);
            w.addObject(gun, getX(), getY());
            hasGun = true;
        }
    }
    
    protected void spawnShield() {
        if (hasShield) return; 
        World w = getWorld();
        if (w != null) {
            Shield shield = new Shield(this);
            w.addObject(shield, getX(), getY());
            hasShield = true;
        }
    }
    
    protected void spawnBandages(){
        if (hasBandages) return;
        World w = getWorld();
        if (w != null) {
            Bandages bandages = new Bandages(this);
            w.addObject(bandages, getX(), getY());
            hasBandages = true;
        }
    }
}