import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

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
    protected boolean hasGun = false;
    protected boolean hasBat = false; // ensures bat is only spawned once
    protected boolean hasShield = false;
    protected boolean hasBandages = false;
    
    private static final int ANGLE_SAMPLES = 36; 
    private static final int LOOK_AHEAD_DISTANCE = 100; 
    
    // Wall repair fields
    private Wall targetWall = null;
    private int repairCooldown = 0;
    private boolean returningToCenter = false;
    
    // Track if we've already handled the transition from night to day
    private boolean wasNighttime = false;
    
    public void act()
    {
        getUserItems();
        
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        // Check for night-to-day transition and kill all zombies with explosions
        if (wasNighttime && world.daytime) {
            killAllZombiesWithExplosions();
            wasNighttime = false;
        } else if (!world.daytime) {
            wasNighttime = true;
        }
        
        // During daytime, repair walls if wall upgrade is enabled
        if (world.daytime && wall) {
            repairWalls();
        } else {
            // During nighttime, use intelligent movement to avoid zombies
            moveIntelligently(3);
        }
        
        // Spawn equipment
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
    
    // Kill all zombies with explosion effects when day breaks
    private void killAllZombiesWithExplosions() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> zombies = world.getObjects(Zombie.class);
        for (Zombie zombie : zombies) {
            // Create explosion at zombie's location
            Explosion explosion = new Explosion();
            world.addObject(explosion, zombie.getX(), zombie.getY());
            
            // Remove the zombie
            world.removeObject(zombie);
        }
    }
    
    // Wall repair system
    private void repairWalls() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        // Decrement repair cooldown
        if (repairCooldown > 0) {
            repairCooldown--;
            return;
        }
        
        // If we don't have a target wall, find one
        if (targetWall == null || targetWall.getWorld() == null) {
            findNextWallToRepair();
        }
        
        // If we have a target wall, move towards it
        if (targetWall != null) {
            returningToCenter = false;
            moveTowardsWall();
            
            // Check if we're close enough to repair
            double distance = Math.sqrt(
                Math.pow(targetWall.getX() - getX(), 2) + 
                Math.pow(targetWall.getY() - getY(), 2)
            );
            
            if (distance < 40) {
                // Repair the wall
                targetWall.repair(3); // Repair 3 HP per act cycle
                repairCooldown = 5; // Small cooldown between repairs
                
                // If wall is fully repaired, find next wall
                if (targetWall.isFullyRepaired()) {
                    targetWall = null;
                }
            }
        } else {
            // No damaged walls, check for broken wall positions to rebuild
            boolean builtWall = rebuildBrokenWalls();
            
            // If no walls to build/repair, return to center
            if (!builtWall) {
                returnToCenter();
            }
        }
    }
    
    private void findNextWallToRepair() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Wall> walls = world.getObjects(Wall.class);
        Wall closestDamagedWall = null;
        double closestDistance = Double.MAX_VALUE;
        
        // Find the closest damaged wall
        for (Wall wall : walls) {
            if (!wall.isFullyRepaired()) {
                double distance = Math.sqrt(
                    Math.pow(wall.getX() - getX(), 2) + 
                    Math.pow(wall.getY() - getY(), 2)
                );
                
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestDamagedWall = wall;
                }
            }
        }
        
        targetWall = closestDamagedWall;
    }
    
    private void moveTowardsWall() {
        if (targetWall == null) return;
        
        int targetX = targetWall.getX();
        int targetY = targetWall.getY();
        
        // Calculate direction to wall
        int dx = targetX - getX();
        int dy = targetY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Only move if not too close
        if (distance > 35) {
            // Normalize and move
            double moveSpeed = 2.5;
            int newX = getX() + (int)((dx / distance) * moveSpeed);
            int newY = getY() + (int)((dy / distance) * moveSpeed);
            
            // Check if position is valid (within boundary)
            GameWorld world = (GameWorld) getWorld();
            if (world != null && world.isValidPosition(newX, newY)) {
                setLocation(newX, newY);
            }
        }
    }
    
    private boolean rebuildBrokenWalls() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return false;
        
        // Get the wall rectangle dimensions from GameWorld
        int width = 400;
        int height = 300;
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        int left = centerX - width / 2;
        int right = centerX + width / 2;
        int top = centerY - height / 2;
        int bottom = centerY + height / 2;
        
        int spacing = 25; // Match the spacing from drawWalls()
        
        // Check all wall positions and rebuild if missing
        List<int[]> positionsToCheck = new ArrayList<>();
        
        // Top and bottom walls
        for (int x = left; x <= right; x += spacing) {
            positionsToCheck.add(new int[]{x, top});
            positionsToCheck.add(new int[]{x, bottom});
        }
        
        // Left and right walls
        for (int y = top; y <= bottom; y += spacing) {
            positionsToCheck.add(new int[]{left, y});
            positionsToCheck.add(new int[]{right, y});
        }
        
        // Find closest missing wall position
        int[] closestPosition = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (int[] pos : positionsToCheck) {
            // Check if there's a wall at this position (within a small range)
            List<Wall> wallsNearby = world.getObjectsAt(pos[0], pos[1], Wall.class);
            boolean hasWall = false;
            for (Wall w : wallsNearby) {
                double dist = Math.sqrt(
                    Math.pow(w.getX() - pos[0], 2) + 
                    Math.pow(w.getY() - pos[1], 2)
                );
                if (dist < 15) {
                    hasWall = true;
                    break;
                }
            }
            
            if (!hasWall) {
                double distance = Math.sqrt(
                    Math.pow(pos[0] - getX(), 2) + 
                    Math.pow(pos[1] - getY(), 2)
                );
                
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPosition = pos;
                }
            }
        }
        
        // If we found a missing wall position, move towards it and rebuild
        if (closestPosition != null) {
            returningToCenter = false;
            double distance = Math.sqrt(
                Math.pow(closestPosition[0] - getX(), 2) + 
                Math.pow(closestPosition[1] - getY(), 2)
            );
            
            if (distance < 40) {
                // We're close enough, rebuild the wall
                Wall newWall = new Wall();
                world.addObject(newWall, closestPosition[0], closestPosition[1]);
                return true; // Built a wall
            } else {
                // Move towards the position
                int dx = closestPosition[0] - getX();
                int dy = closestPosition[1] - getY();
                double moveSpeed = 2.5;
                int newX = getX() + (int)((dx / distance) * moveSpeed);
                int newY = getY() + (int)((dy / distance) * moveSpeed);
                
                if (world.isValidPosition(newX, newY)) {
                    setLocation(newX, newY);
                }
                return true; // Working on building a wall
            }
        }
        
        return false; // No walls to build
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
            Bat bat = new Bat(50, 25, 50, this);
            w.addObject(bat, getX(), getY());
            hasBat = true;
        }
    }
    
    protected void spawnGun() {
        if (hasGun) return; // make sure we only spawn it once
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
    
    // Return to center of the map when done with repairs
    private void returnToCenter() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        double distance = Math.sqrt(
            Math.pow(centerX - getX(), 2) + 
            Math.pow(centerY - getY(), 2)
        );
        
        // If not at center, move towards it
        if (distance > 10) {
            returningToCenter = true;
            int dx = centerX - getX();
            int dy = centerY - getY();
            double moveSpeed = 2.5;
            int newX = getX() + (int)((dx / distance) * moveSpeed);
            int newY = getY() + (int)((dy / distance) * moveSpeed);
            
            if (world.isValidPosition(newX, newY)) {
                setLocation(newX, newY);
            }
        } else {
            returningToCenter = false;
        }
    }
}