import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Regular here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Regular extends Zombie
{
    private final static int REG_HEALTH = 100;
    private final static int REG_SPEED = 2;
    private final static int REG_DAMAGE = 1;
    
    public void act()
    {
        super.act();
        
    }
    
    public Regular() {
        health = REG_HEALTH;
        speed = REG_SPEED;
        damage = REG_DAMAGE;
        
        GreenfootImage img = new GreenfootImage("Zombie.png");
        setImage(img);
        
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
}
