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
    
    protected int attackCooldown;
    
    protected abstract void attack();
    
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
    
    protected void checkHitSurvivor() {  
        if (isTouching(Survivors.class) && attackCooldown == 0) {
            attack();
            attackCooldown = 30; 
        }
    }
    
    protected void killZombie() {
        getWorld().removeObject(this);
    }
    
    protected void moveZombie() {
        int centerX = getWorld().getWidth() / 2;
        int centerY = getWorld().getHeight() / 2;
        
        turnTowards(centerX, centerY);
        move(speed);
    }
}
