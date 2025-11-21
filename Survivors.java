import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Super class for survivors
 * 
 * @author Paul with assistance from Claude (all final movement logic was improved by Claude)
 * 
 * 
 */
public abstract class Survivors extends SuperSmoothMover
{
    protected int startHP;
    protected int hp;
    protected final int DETECTION = 150; // Increased from 100
    protected boolean melee = false;
    protected boolean gun = false;
    protected boolean shield = false;
    protected boolean bandages = false;
    protected boolean wall = false;
    protected boolean hasGun = false;
    protected boolean hasBat = false;
    protected boolean hasShield = false;
    protected boolean hasBandages = false;
    
    // Movement algorithm parameters 
    private static final int ANGLE_SAMPLES = 72; 
    private static final int LOOK_AHEAD_STEPS = 3; // Multi-step planning
    private static final double BOUNDARY_BUFFER = 50; // Stay away from edges
    private static final double SWARM_DETECTION_RADIUS = 80; // Detect zombie clusters
    private static final int MIN_MOVE_SPEED = 1;
    
    // Zombie threat weights by type
    private static final double BOSS_THREAT_MULTIPLIER = 3.0;
    private static final double GIANT_THREAT_MULTIPLIER = 2.0;
    private static final double PENGUIN_THREAT_MULTIPLIER = 2.5; // Fast!
    private static final double REGULAR_THREAT_MULTIPLIER = 1.0;
    
    public void act()
    {
        getUserItems();
        if (melee && !hasBat) {
            spawnBat();
        } else if (shield && !hasShield){
            spawnShield();
        } else if (gun && !hasGun){
            spawnGun();
        }
        else if(bandages && !hasBandages){
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
    
    public int getAngleTowards(Actor target)
    {
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        
        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) Math.toDegrees(angleInRadians);
        
        angleInDegrees = (angleInDegrees + 90) % 360;
        if (angleInDegrees < 0) angleInDegrees += 360;
        
        return angleInDegrees;
    }
    
    protected void moveIntelligently(int speed) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> allZombies = world.getObjects(Zombie.class);
        List<Zombie> nearbyZombies = getObjectsInRange(DETECTION, Zombie.class);
        
        // Only move if zombies are within detection range
        if (nearbyZombies.isEmpty()) {
            return;
        }
        
        // Try to find optimal move with full speed first
        int currentSpeed = speed;
        int bestAngle = -1;
        
        // Progressive speed reduction if no valid move found
        while (currentSpeed > 0 && bestAngle == -1) {
            bestAngle = findOptimalDirection(allZombies, world, currentSpeed);
            
            if (bestAngle == -1) {
                currentSpeed = Math.max(currentSpeed / 2, MIN_MOVE_SPEED);
                if (currentSpeed < MIN_MOVE_SPEED) break;
            }
        }
        
        // Emergency: If completely trapped, find least dangerous direction even if out of bounds
        if (bestAngle == -1) {
            bestAngle = findEmergencyEscapeDirection(allZombies, world, MIN_MOVE_SPEED);
        }
        
        // Execute movement
        if (bestAngle != -1) {
            setRotation(bestAngle);
            move(currentSpeed);
        }
    }
    
    private int findOptimalDirection(List<Zombie> zombies, GameWorld world, int speed) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestAngle = -1;
        
        for (int i = 0; i < ANGLE_SAMPLES; i++) {
            int testAngle = (360 / ANGLE_SAMPLES) * i;
            
            // Calculate projected position
            int projectedX = (int)(getX() + speed * Math.cos(Math.toRadians(testAngle - 90)));
            int projectedY = (int)(getY() + speed * Math.sin(Math.toRadians(testAngle - 90)));
            
            // CRITICAL: Must stay in bounds
            if (!world.isValidPosition(projectedX, projectedY)) {
                continue;
            }
            
            // Calculate comprehensive safety score
            double score = calculateAdvancedSafetyScore(
                projectedX, projectedY, testAngle, zombies, world, speed
            );
            
            if (score > bestScore) {
                bestScore = score;
                bestAngle = testAngle;
            }
        }
        
        return bestAngle;
    }
    
    private int findEmergencyEscapeDirection(List<Zombie> zombies, GameWorld world, int speed) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestAngle = -1;
        
        // In emergency, prioritize moving toward center (most likely to be valid)
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        int angleToCenter = getAngleTowards(centerX, centerY);
        
        // Test directions near center angle
        for (int offset = -90; offset <= 90; offset += 10) {
            int testAngle = (angleToCenter + offset + 360) % 360;
            
            int projectedX = (int)(getX() + speed * Math.cos(Math.toRadians(testAngle - 90)));
            int projectedY = (int)(getY() + speed * Math.sin(Math.toRadians(testAngle - 90)));
            
            // Only consider if in bounds
            if (!world.isValidPosition(projectedX, projectedY)) {
                continue;
            }
            
            double score = calculateBasicSafetyScore(projectedX, projectedY, zombies);
            
            if (score > bestScore) {
                bestScore = score;
                bestAngle = testAngle;
            }
        }
        
        return bestAngle;
    }
    
    // Helper for emergency situations
    private int getAngleTowards(int targetX, int targetY) {
        int dx = targetX - getX();
        int dy = targetY - getY();
        
        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) Math.toDegrees(angleInRadians);
        
        angleInDegrees = (angleInDegrees + 90) % 360;
        if (angleInDegrees < 0) angleInDegrees += 360;
        
        return angleInDegrees;
    }
    
    private double calculateAdvancedSafetyScore(
        int x, int y, int moveAngle, List<Zombie> zombies, GameWorld world, int speed
    ) {
        double totalScore = 0;
        
        // 1. ZOMBIE THREAT ASSESSMENT (with type-specific weights and prediction)
        double zombieThreatScore = 0;
        int nearbyCount = 0;
        
        for (Zombie zombie : zombies) {
            // Get zombie type multiplier
            double threatMultiplier = getZombieThreatMultiplier(zombie);
            
            // Current distance
            double distance = Math.sqrt(
                Math.pow(zombie.getX() - x, 2) + 
                Math.pow(zombie.getY() - y, 2)
            );
            
            // Predict zombie's next position (zombies always move toward survivor)
            double zombieSpeed = getZombieSpeed(zombie);
            int predictedZombieX = (int)(zombie.getX() + zombieSpeed * Math.cos(Math.toRadians(getAngleTowards(zombie) - 90)));
            int predictedZombieY = (int)(zombie.getY() + zombieSpeed * Math.sin(Math.toRadians(getAngleTowards(zombie) - 90)));
            
            // Distance to predicted position
            double predictedDistance = Math.sqrt(
                Math.pow(predictedZombieX - x, 2) + 
                Math.pow(predictedZombieY - y, 2)
            );
            
            // Use the worse of current and predicted distance
            double effectiveDistance = Math.min(distance, predictedDistance);
            
            if (effectiveDistance < 1) effectiveDistance = 1;
            
            // Balanced scoring: exponential danger for close zombies, linear bonus for far ones
            double dangerScore = (5000.0 / effectiveDistance) * threatMultiplier;
            double distanceBonus = effectiveDistance * 2;
            
            zombieThreatScore += distanceBonus - dangerScore;
            
            // Count nearby zombies for swarm detection
            if (effectiveDistance < SWARM_DETECTION_RADIUS) {
                nearbyCount++;
            }
        }
        
        totalScore += zombieThreatScore;
        
        // 2. SWARM PENALTY (avoid clusters of zombies)
        if (nearbyCount > 2) {
            double swarmPenalty = (nearbyCount - 2) * 3000;
            totalScore -= swarmPenalty;
        }
        
        // 3. BOUNDARY AWARENESS (stay away from edges)
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        // Distance from center
        double distanceFromCenter = Math.sqrt(
            Math.pow(x - centerX, 2) + 
            Math.pow(y - centerY, 2)
        );
        
        // Penalty for being near edges
        double maxSafeDistance = Math.min(
            world.getWidth() / 2 - BOUNDARY_BUFFER,
            world.getHeight() / 2 - BOUNDARY_BUFFER
        );
        
        if (distanceFromCenter > maxSafeDistance) {
            double edgePenalty = (distanceFromCenter - maxSafeDistance) * 100;
            totalScore -= edgePenalty;
        } else {
            // Small bonus for staying in the "safe zone"
            totalScore += 500;
        }
        
        // 4. MULTI-STEP LOOKAHEAD (is this direction a trap?)
        double futureScore = evaluateFuturePosition(x, y, moveAngle, zombies, world, speed, 1);
        totalScore += futureScore * 0.5; // Weight future less than immediate
        
        // 5. MOVEMENT CONSISTENCY (slight bonus to reduce zigzagging)
        int currentRotation = getRotation();
        int angleDiff = Math.abs(moveAngle - currentRotation);
        if (angleDiff > 180) angleDiff = 360 - angleDiff;
        
        if (angleDiff < 45) {
            totalScore += 200; // Small bonus for continuing in similar direction
        }
        
        return totalScore;
    }
    
    private double evaluateFuturePosition(
        int x, int y, int angle, List<Zombie> zombies, GameWorld world, int speed, int depth
    ) {
        if (depth > LOOK_AHEAD_STEPS) {
            return 0;
        }
        
        // Project further ahead
        int futureX = (int)(x + speed * Math.cos(Math.toRadians(angle - 90)));
        int futureY = (int)(y + speed * Math.sin(Math.toRadians(angle - 90)));
        
        // If future position is out of bounds, big penalty
        if (!world.isValidPosition(futureX, futureY)) {
            return -5000;
        }
        
        // Quick safety check at future position
        double futureScore = calculateBasicSafetyScore(futureX, futureY, zombies);
        
        // Recursively check next step (with reduced weight)
        double nextStepScore = evaluateFuturePosition(
            futureX, futureY, angle, zombies, world, speed, depth + 1
        );
        
        return futureScore * 0.7 + nextStepScore * 0.3;
    }
    
    private double calculateBasicSafetyScore(int x, int y, List<Zombie> zombies) {
        double totalScore = 0;
        
        for (Zombie zombie : zombies) {
            double distance = Math.sqrt(
                Math.pow(zombie.getX() - x, 2) + 
                Math.pow(zombie.getY() - y, 2)
            );
            
            if (distance < 1) distance = 1;
            
            totalScore += distance - (2000.0 / distance);
        }
        
        return totalScore;
    }
    
    private double getZombieThreatMultiplier(Zombie zombie) {
        if (zombie instanceof Boss) {
            return BOSS_THREAT_MULTIPLIER;
        } else if (zombie instanceof Giant) {
            return GIANT_THREAT_MULTIPLIER;
        } else if (zombie instanceof Penguin) {
            return PENGUIN_THREAT_MULTIPLIER;
        } else {
            return REGULAR_THREAT_MULTIPLIER;
        }
    }
    
    private double getZombieSpeed(Zombie zombie) {
        if (zombie instanceof Boss) {
            return 0.5;
        } else if (zombie instanceof Giant) {
            return 1.0;
        } else if (zombie instanceof Penguin) {
            return 4.0;
        } else { // Regular
            return 2.0;
        }
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
            Gun gun = new Gun(50, 50, this);
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