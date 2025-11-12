import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Special here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Special extends Zombie
{
    private final static int SPEC_HEALTH = 50;
    private final static double SPEC_SPEED = 4;
    private final static int SPEC_DAMAGE = 1;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;
    
    public Special() {
        health = SPEC_HEALTH;
        speed = SPEC_SPEED;
        damage = SPEC_DAMAGE;
        
        enableStaticRotation();
        
        leftImage = new GreenfootImage("Penguin.png");
        
        rightImage = new GreenfootImage("Penguin.png");
        rightImage.mirrorHorizontally();
        
        setImage(leftImage);
    }

    public void act()
    {
        super.act();
    }
    
    protected void attack() {
        
    }
    
    protected void checkHitSurvivor() {
        
    }
    
    protected GreenfootImage getLeftImage() {
        return leftImage;
    }
    
    protected GreenfootImage getRightImage() {
        return rightImage;
    }
}
