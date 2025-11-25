import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class Zombies here.
 * 
 * @author Cayden 
 * @version (a version number or a date)
 */
public abstract class Zombie extends SuperSmoothMover
{
    protected int startHP;
    protected int hp;
    protected int damage;
    protected int health;
    protected int maxHealth;
    protected double speed;
    protected int attackCooldown = 0;  // Initialize here to 0
    protected SuperStatBar hpBar; // Each zombie will have its own HP bar
    
    /**
     * Method automatically called by Greenfoot when zombie is added to world
     * This ensures the HP bar gets added to the world
     */
    
    // Updated method to keep HP bar positioned above zombie
    
    protected void updateHpBar()
    {
        if (hpBar != null && getWorld() != null)
        {
            hpBar.update(health);
            // Keep it floating above the zombie's head
            hpBar.setLocation(getX(), getY() - 40);
        }
    }
    
    protected void attack() {
        Survivors s = (Survivors) getOneIntersectingObject(Survivors.class);
        if (s != null) {
            s.takeDamage(damage);  
        }
    }
    
    protected void checkHitSurvivor() {
        if (isTouching(Survivors.class) && attackCooldown == 0) {
            attack();
            attackCooldown = 30; 
        }
    }
    
    protected abstract GreenfootImage getLeftImage();
    
    protected abstract GreenfootImage getRightImage();
    
    /**
     * Returns the zombie type for score tracking
     * Override this in each zombie subclass
     */
    protected abstract String getZombieType();
    
    public void act()
    {
        if (!isDead()) {
            moveZombie();  // This now handles wall collision AND attacking
            checkHitSurvivor();
            updateHpBar();
            if (attackCooldown > 0) {
                attackCooldown--;
            }
        } else {
            killZombie();
        }
    }
    
    protected void checkHitWall() {
        // Check if touching a wall or very close to one
        List<Wall> walls = getObjectsInRange(20, Wall.class);
        if (!walls.isEmpty()) {
            Wall wall = walls.get(0);
            if (attackCooldown == 0) {
                wall.takeDamage(damage);
                attackCooldown = 5;
            }
        }
    }
    
    public void takeDamage(int dmg) {
        health -= dmg;
        if (hpBar != null) {
            hpBar.update(health);
        }
    }
    
    protected boolean isDead() {
        return health <= 0;
    }
    
    protected void killZombie() {
        // Update score before removing zombie
        updateScoreTracker();
        
        // Remove HP bar first, then zombie
        if (hpBar != null && getWorld() != null) {
            getWorld().removeObject(hpBar);
        }
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }
    
    private void updateScoreTracker() {
            if (getWorld() != null) {
                List<ScoreTracker> trackers = getWorld().getObjects(ScoreTracker.class);
                if (trackers != null && !trackers.isEmpty()) {
                    ScoreTracker tracker = trackers.get(0);
                    String type = getZombieType();
                    if (type.equals("Regular")) {
                        tracker.numRegular++;
                    } else if (type.equals("Penguin")) {
                        tracker.numPenguin++;
                    } else if (type.equals("Boss")) {
                        tracker.numBoss++;
                    } else if (type.equals("Giant")) {
                        tracker.numGiant++;
                    } else if (type.equals("Special")) {
                        tracker.numSpecial++;
                    }
                    tracker.updateScore(); 
                }
            }
    }
    
    protected void moveZombie() {  
        ArrayList<Survivors> survivors = (ArrayList<Survivors>)getWorld().getObjects(Survivors.class);
        
        if (!survivors.isEmpty()) {
            Survivors survivor = survivors.get(0);
            int x = survivor.getX();
            int y = survivor.getY();
            
            // Calculate distance to survivor
            double distance = Math.sqrt(Math.pow(x - getX(), 2) + Math.pow(y - getY(), 2));
            
            // Only move if we're not close enough (stopping radius of 50 pixels)
            if (distance > 50) {
                turnTowards(x, y);
                
                if (getX() < x) {
                    setImage(getLeftImage()); 
                } else {
                    setImage(getRightImage()); 
                }
                
                // Store current position before moving
                int oldX = getX();
                int oldY = getY();
                
                move(speed);
                
                // Check if we hit a wall after moving
                if (isTouching(Wall.class)) {
                    // Attack the wall BEFORE moving back
                    Wall wall = (Wall) getOneIntersectingObject(Wall.class);
                    if (wall != null && attackCooldown == 0) {
                        wall.takeDamage(damage);
                        attackCooldown = 2;
                    }
                    
                    // Now move back to previous position
                    setLocation(oldX, oldY);
                }
            } else {
                // Still face the survivor even when stopped
                turnTowards(x, y);
                if (getX() < x) {
                    setImage(getLeftImage()); 
                } else {
                    setImage(getRightImage()); 
                }
            }
        }
    }
}