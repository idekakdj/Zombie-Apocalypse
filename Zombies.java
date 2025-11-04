import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Zombies here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Zombies extends SuperSmoothMover
{
    protected int damage;
    protected int health;
    protected int speed;
    protected int spawnRate;
    
    protected abstract void attack();
    
    public void act()
    {
        
    }
    
    protected void takeDamage(int dmg) {
        health -= dmg;
    }
    
    protected boolean isDead() {
        if (health <= 0) {
            return true;
        }
        
        return false;
    }
}
