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
    private final static double REG_SPEED = 2;
    private final static int REG_DAMAGE = 1;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;
    
    public void act()
    {
        super.act();
        
    }
    
    public Regular() {
        health = REG_HEALTH;
        maxHealth = REG_HEALTH;

        speed = REG_SPEED;
        damage = REG_DAMAGE;
        
        enableStaticRotation();
        
        leftImage = new GreenfootImage("Zombie.png");
        leftImage.scale(70, 90);
        
        rightImage = new GreenfootImage("Zombie.png");
        rightImage.mirrorHorizontally();
        rightImage.scale(70, 90);
        
                hpBar = new SuperStatBar(maxHealth, health, this, 50, 5, 0, Color.GREEN, Color.RED, true);

        
        setImage(leftImage);
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
    
    protected GreenfootImage getLeftImage() {
        return leftImage;
    }
    
    protected GreenfootImage getRightImage() {
        return rightImage;
    }
}
