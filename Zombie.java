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
    
    protected abstract void attack();
    
    protected abstract void checkHitSurvivor();
    
    protected abstract GreenfootImage getLeftImage();
    
    protected abstract GreenfootImage getRightImage();
    
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
       // Remove HP bar first, then zombie
        if (hpBar != null && getWorld() != null) {
            getWorld().removeObject(hpBar);
        }
        if (getWorld() != null) {
            getWorld().removeObject(this);
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