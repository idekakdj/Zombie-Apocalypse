import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    protected int speed;
    protected int spawnRate;
    
    // Jayden added code superstatbar:
    
    protected void updateHpBar()
    {
        
    }
    
    
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
