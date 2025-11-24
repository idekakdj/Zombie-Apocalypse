import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
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
    protected int attackCooldown;
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
            moveZombie();
            checkHitSurvivor(); 
            updateHpBar(); // Update HP bar every act
            if (attackCooldown > 0) {
                attackCooldown--;
            }
        } else {
            killZombie();
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
    
    /**
     * Notify the ScoreTracker that this zombie has been killed
     */
    private void updateScoreTracker() {
        if (getWorld() != null) {
            java.util.List<ScoreTracker> trackers = getWorld().getObjects(ScoreTracker.class);
            if (trackers != null && !trackers.isEmpty()) {
                ScoreTracker tracker = trackers.get(0);
                String type = getZombieType().toLowerCase();
                if (type.equals("regular")) {
                    tracker.numRegular++;
                } else if (type.equals("penguin")) {
                    tracker.numPenguin++;
                } else if (type.equals("boss")) {
                    tracker.numBoss++;
                } else if (type.equals("giant")) {
                    tracker.numGiant++;
                } else if (type.equals("special")) {
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
            
            turnTowards(x, y);
            
            if (getX() < x) {
                setImage(getLeftImage()); 
            } else {
                setImage(getRightImage()); 
            }
            
            move(speed);
        }
    }
}