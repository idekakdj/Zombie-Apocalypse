import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Base zombie moderate health, speed, and damage
 * 
 * @author Cayden 
 * @version (a version number or a date)
 */
public class Regular extends Zombie
{
    
    private final static int REG_HEALTH = 100;
    private final static double REG_SPEED = 2;
    private final static int REG_DAMAGE = 2;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;
    
    // Removed the act() method - parent class handles everything
    
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
        
        hpBar = new SuperStatBar(maxHealth, health, this, 40, 8, -60, Color.GREEN, Color.RED, false, Color.ORANGE, 1);
        
        setImage(leftImage);
    }
    
    public void addedToWorld(World w)
    {
        if (hpBar != null)
        {
            w.addObject(hpBar, getX(), getY());
            hpBar.update(health);
        }
    }
    
    protected GreenfootImage getLeftImage() {
        return leftImage;
    }
    
    protected GreenfootImage getRightImage() {
        return rightImage;
    }
    
    protected String getZombieType() {
        return "Regular";
    }
}