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
    
    protected int attackCooldown;
    
    // Jayden added code superstatbar:
    
    protected void updateHpBar()
    {
        
    }
    
    
    protected abstract void attack();
    
    protected abstract void checkHitSurvivor();
    
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
        int x = getWorld().getWidth() / 2;
        int y = getWorld().getHeight() / 2;
        
        turnTowards(x, y);
        move(speed);
    }
}
