import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Zombies here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Zombie extends SuperSmoothMover
{
    protected int damage;
    protected int health;
    protected double speed;
    protected int attackCooldown;
    
    // Jayden added code superstatbar:
    protected void updateHpBar()
    {
        
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
            if (attackCooldown > 0) {
                attackCooldown--;
            }
        } else {
            killZombie();
        }
    }
    
    protected void takeDamage(int dmg) {
        health -= dmg;
    }
    
    protected boolean isDead() {
        return health <= 0;
    }
    
    protected void killZombie() {
        getWorld().removeObject(this);
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
