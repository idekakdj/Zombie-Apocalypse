import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Super class for survivors that handles all movement and spawning items that are selected
 * 
 * @author Paul with assistance from Cayden and from Claude (Expert AI pathfinding implementation)
 */
public abstract class Survivors extends SuperSmoothMover
{
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
    
    // Movement AI constants
    private static final int DETECTION_RANGE = 150;
    private static final int DIRECTION_SAMPLES = 72;
    private static final int MIN_SPEED = 1;
    
    // Threat assessment constants
    private static final double BOSS_THREAT = 3.5;
    private static final double GIANT_THREAT = 2.5;
    private static final double PENGUIN_THREAT = 3.0;
    private static final double REGULAR_THREAT = 1.0;
    
    // Scoring weights
    private static final double ZOMBIE_DANGER_WEIGHT = 5000.0;
    private static final double DISTANCE_BONUS_WEIGHT = 4.0;
    private static final double PREDICTION_WEIGHT = 1.5;
    private static final double SWARM_PENALTY_BASE = 2500.0;
    private static final double EDGE_APPROACH_PENALTY = 30000.0;
    private static final double WALL_PENALTY_WEIGHT = 30.0;
    
    // Wall repair fields
    private Wall targetWall = null;
    private int repairCooldown = 10;
    private boolean returningToCenter = false;
    private boolean wasNighttime = false;
        private GreenfootSound lego = new GreenfootSound ("lego.mp3");

    /**
     * gets user selection of items, checks for day and nighttime, moves away from zombies in range at night
     * spawns all equipment that is selected
     */
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
            returnToCenter();
            int speed = 2;
            moveIntelligently(speed);
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
    
    private void killAllZombiesWithExplosions() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> zombies = world.getObjects(Zombie.class);
        for (Zombie zombie : zombies) {
            Explosion explosion = new Explosion();
            world.addObject(explosion, zombie.getX(), zombie.getY());
            world.removeObject(zombie);
        }
    }
    
    private void repairWalls() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        if (repairCooldown > 0) {
            repairCooldown--;
            return;
        }
        
        if (targetWall == null || targetWall.getWorld() == null) {
            findNextWallToRepair();
        }
        
        if (targetWall != null) {
            returningToCenter = false;
            moveTowardsWall();
            
            double distance = Math.sqrt(
                Math.pow(targetWall.getX() - getX(), 2) + 
                Math.pow(targetWall.getY() - getY(), 2)
            );
            
            if (distance < 40) {
                targetWall.repair(5);
                repairCooldown = 10;
                
                if (targetWall.isFullyRepaired()) {
                    targetWall = null;
                }
            }
        } else {
            boolean builtWall = rebuildBrokenWalls();
            
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
        
        int dx = targetX - getX();
        int dy = targetY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 35) {
            double moveSpeed = 2.5;
            int newX = getX() + (int)((dx / distance) * moveSpeed);
            int newY = getY() + (int)((dy / distance) * moveSpeed);
            
            GameWorld world = (GameWorld) getWorld();
            if (world != null && world.isValidPosition(newX, newY)) {
                setLocation(newX, newY);
            }
        }
    }
    
    private boolean rebuildBrokenWalls() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return false;
        
        int width = 400;
        int height = 300;
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        int left = centerX - width / 2;
        int right = centerX + width / 2;
        int top = centerY - height / 2;
        int bottom = centerY + height / 2;
        
        int spacing = 25;
        
        List<int[]> positionsToCheck = new ArrayList<>();
        
        for (int x = left; x <= right; x += spacing) {
            positionsToCheck.add(new int[]{x, top});
            positionsToCheck.add(new int[]{x, bottom});
        }
        
        for (int y = top; y <= bottom; y += spacing) {
            positionsToCheck.add(new int[]{left, y});
            positionsToCheck.add(new int[]{right, y});
        }
        
        int[] closestPosition = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (int[] pos : positionsToCheck) {
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
        
        if (closestPosition != null) {
            returningToCenter = false;
            double distance = Math.sqrt(
                Math.pow(closestPosition[0] - getX(), 2) + 
                Math.pow(closestPosition[1] - getY(), 2)
            );
            
            if (distance < 40) {
                Wall newWall = new Wall();
                world.addObject(newWall, closestPosition[0], closestPosition[1]);
                                Greenfoot.playSound("lego.mp3");  

                return true;
            } else {
                int dx = closestPosition[0] - getX();
                int dy = closestPosition[1] - getY();
                double moveSpeed = 2.5;
                int newX = getX() + (int)((dx / distance) * moveSpeed);
                int newY = getY() + (int)((dy / distance) * moveSpeed);
                
                if (world.isValidPosition(newX, newY)) {
                    setLocation(newX, newY);
                }
                return true;
            }
        }
        
        return false;
    }
    /**
     * gets all booleans from game world to know which items to spawn
     */
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
    /**
     * gets start hp variable
     */
    public int getStartHP(){
        return startHP;
    }
    /**
     * gets angle zombies are approaching from
     */
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
    
    protected void moveIntelligently(int maxSpeed) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        List<Zombie> nearbyZombies = getObjectsInRange(DETECTION_RANGE, Zombie.class);
        
        if (nearbyZombies.isEmpty()) {
            return;
        }
        
        MovementResult result = findOptimalMovement(nearbyZombies, world, maxSpeed);
        
        if (result.isValid()) {
            setRotation(result.angle);
            move(result.speed);
        }
    }
    
    private MovementResult findOptimalMovement(List<Zombie> zombies, GameWorld world, int maxSpeed) {
        int currentSpeed = maxSpeed;
        
        while (currentSpeed >= MIN_SPEED) {
            MovementResult result = evaluateAllDirections(zombies, world, currentSpeed);
            
            if (result.isValid()) {
                return result;
            }
            
            currentSpeed = currentSpeed / 2;
        }
        
        return findEmergencyMovement(zombies, world);
    }
    
    private MovementResult evaluateAllDirections(List<Zombie> zombies, GameWorld world, int speed) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestAngle = -1;
        
        for (int i = 0; i < DIRECTION_SAMPLES; i++) {
            int testAngle = (360 * i) / DIRECTION_SAMPLES;
            
            double radians = Math.toRadians(testAngle - 90);
            int projX = (int)(getX() + speed * Math.cos(radians));
            int projY = (int)(getY() + speed * Math.sin(radians));
            
            if (!world.isValidPosition(projX, projY)) {
                continue;
            }
            int edgeBuffer = 80;
            if (projX < edgeBuffer || projX > world.getWidth() - edgeBuffer ||
                projY < edgeBuffer || projY > world.getHeight() - edgeBuffer) {
                continue;
            }
            if (wall && wouldCollideWithWall(projX, projY, world)) {
                continue;
            }
            
            double score = calculateSafetyScore(projX, projY, zombies, world);
            
            if (score > bestScore) {
                bestScore = score;
                bestAngle = testAngle;
            }
        }
        
        if (bestAngle != -1) {
            return new MovementResult(bestAngle, speed);
        }
        
        if (speed == MIN_SPEED) {
            for (int i = 0; i < DIRECTION_SAMPLES; i++) {
                int testAngle = (360 * i) / DIRECTION_SAMPLES;
                
                double radians = Math.toRadians(testAngle - 90);
                int projX = (int)(getX() + 0.5 * Math.cos(radians));
                int projY = (int)(getY() + 0.5 * Math.sin(radians));
                
                if (!world.isValidPosition(projX, projY)) {
                    continue;
                }
                
                if (wall && wouldCollideWithWall(projX, projY, world)) {
                    continue;
                }
                
                double score = calculateSafetyScore(projX, projY, zombies, world);
                
                if (score > bestScore) {
                    bestScore = score;
                    bestAngle = testAngle;
                }
            }
            
            if (bestAngle != -1) {
                return new MovementResult(bestAngle, 1);
            }
        }
        
        return new MovementResult(-1, 0);
    }
    
    private MovementResult findEmergencyMovement(List<Zombie> zombies, GameWorld world) {
        Zombie nearest = findNearestZombie(zombies);
        
        if (nearest != null) {
            int awayAngle = (calculateAngle(nearest.getX(), nearest.getY()) + 180) % 360;
            
            for (int offset = 0; offset <= 180; offset += 5) {
                for (int sign : new int[]{-1, 1}) {
                    int testAngle = (awayAngle + offset * sign + 360) % 360;
                    
                    double radians = Math.toRadians(testAngle - 90);
                    int projX = (int)(getX() + MIN_SPEED * Math.cos(radians));
                    int projY = (int)(getY() + MIN_SPEED * Math.sin(radians));
                    
                    if (!world.isValidPosition(projX, projY)) {
                        continue;
                    }
                    
                    if (wall && wouldCollideWithWall(projX, projY, world)) {
                        continue;
                    }
                    
                    return new MovementResult(testAngle, MIN_SPEED);
                }
            }
        }
        
        for (int angle = 0; angle < 360; angle += 5) {
            double radians = Math.toRadians(angle - 90);
            int projX = (int)(getX() + MIN_SPEED * Math.cos(radians));
            int projY = (int)(getY() + MIN_SPEED * Math.sin(radians));
            
            if (!world.isValidPosition(projX, projY)) {
                continue;
            }
            
            if (wall && wouldCollideWithWall(projX, projY, world)) {
                continue;
            }
            
            return new MovementResult(angle, MIN_SPEED);
        }
        
        for (int angle = 0; angle < 360; angle += 5) {
            double radians = Math.toRadians(angle - 90);
            double microSpeed = MIN_SPEED * 0.5;
            int projX = (int)(getX() + microSpeed * Math.cos(radians));
            int projY = (int)(getY() + microSpeed * Math.sin(radians));
            
            if (!world.isValidPosition(projX, projY)) {
                continue;
            }
            
            if (wall && wouldCollideWithWall(projX, projY, world)) {
                continue;
            }
            
            return new MovementResult(angle, 1);
        }
        
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        int angleToCenter = calculateAngle(centerX, centerY);
        
        for (int offset = -45; offset <= 45; offset += 5) {
            int testAngle = (angleToCenter + offset + 360) % 360;
            double radians = Math.toRadians(testAngle - 90);
            
            double tinySpeed = 0.5;
            int projX = (int)(getX() + tinySpeed * Math.cos(radians));
            int projY = (int)(getY() + tinySpeed * Math.sin(radians));
            
            if (!world.isValidPosition(projX, projY)) {
                continue;
            }
            
            if (wall && wouldCollideWithWall(projX, projY, world)) {
                continue;
            }
            
            return new MovementResult(testAngle, 1);
        }
        
        return new MovementResult(getRotation(), 0);
    }
    
    private boolean wouldCollideWithWall(int x, int y, GameWorld world) {
        List<Wall> walls = world.getObjects(Wall.class);
        
        for (Wall wallObj : walls) {
            double distance = Math.sqrt(
                Math.pow(wallObj.getX() - x, 2) + 
                Math.pow(wallObj.getY() - y, 2)
            );
            
            int wallRadius = wallObj.getImage().getWidth() / 2;
            int survivorRadius = getImage().getWidth() / 2;
            
            if (distance < (wallRadius + survivorRadius)) {
                return true;
            }
        }
        
        return false;
    }
    
    private double calculateSafetyScore(int x, int y, List<Zombie> zombies, GameWorld world) {
        double totalScore = 0;
        int nearbyCount = 0;
        
        for (Zombie zombie : zombies) {
            double threat = getZombieThreat(zombie);
            double speed = getZombieSpeed(zombie);
            
            double currentDist = distance(x, y, zombie.getX(), zombie.getY());
            
            int zombieAngle = calculateAngle(zombie.getX(), zombie.getY());
            double predRadians = Math.toRadians(zombieAngle - 90);
            int predX = (int)(zombie.getX() + speed * Math.cos(predRadians));
            int predY = (int)(zombie.getY() + speed * Math.sin(predRadians));
            double predictedDist = distance(x, y, predX, predY);
            
            double effectiveDist = Math.min(currentDist, predictedDist * PREDICTION_WEIGHT);
            if (effectiveDist < 1) effectiveDist = 1;
            
            double dangerScore = (ZOMBIE_DANGER_WEIGHT / (effectiveDist * effectiveDist)) * threat;
            double distanceBonus = effectiveDist * DISTANCE_BONUS_WEIGHT;
            
            totalScore += distanceBonus - dangerScore;
            
            if (currentDist < 80) {
                nearbyCount++;
            }
        }
        
        if (nearbyCount > 2) {
            totalScore -= (nearbyCount - 2) * SWARM_PENALTY_BASE;
        }
        
        if (wall) {
            List<Wall> walls = world.getObjects(Wall.class);
            for (Wall wallObj : walls) {
                double wallDist = distance(x, y, wallObj.getX(), wallObj.getY());
                
                if (wallDist < 100) {
                    double wallPenalty = (100 - wallDist) * WALL_PENALTY_WEIGHT;
                    totalScore -= wallPenalty;
                }
            }
        }
        
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        // Replace the edge penalty section with this:
        int distToLeftEdge = x;
        int distToRightEdge = world.getWidth() - x;
        int distToTopEdge = y;
        int distToBottomEdge = world.getHeight() - y;
        
        int minEdgeDist = Math.min(
            Math.min(distToLeftEdge, distToRightEdge),
            Math.min(distToTopEdge, distToBottomEdge)
        );
        
        // Apply massive penalty when within 80 pixels of any edge
        if (minEdgeDist < 80) {
            double edgePenalty = (80 - minEdgeDist) * 500; // Strong exponential
            totalScore -= edgePenalty * edgePenalty; // Make it quadratic
        }
        return totalScore;
    }
    
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
     * for survivors to take damage from zombies
     * @param damage, amount of damage to be taken
     */
    public abstract void takeDamage(int damage);
    /**
     * gets current hp
     */
    public int getHP() {
        return hp;
    }
    /**
     * gets maximum hp
     */    
    public int getMaxHP() {
        return startHP;
    }
    /**
     * for bandages to call to heal hp
     */
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
            Bat bat = new Bat(40, 35, 80, this);
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
    
    private void returnToCenter() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;
        
        double distance = Math.sqrt(
            Math.pow(centerX - getX(), 2) + 
            Math.pow(centerY - getY(), 2)
        );
        
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