import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Special here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Penguin extends Zombie
{
    private final static int PENG_HEALTH = 50;
    private final static double PENG_SPEED = 4;
    private final static int PENG_DAMAGE = 1;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;
    
    public Penguin() {
        health = PENG_HEALTH;
        speed = PENG_SPEED;
        damage = PENG_DAMAGE;
        
        enableStaticRotation();
        
        leftImage = new GreenfootImage("Penguin.png");
        leftImage.scale(56, 61);
        
        rightImage = new GreenfootImage("Penguin.png");
        rightImage.mirrorHorizontally();
        rightImage.scale(56, 61);
        
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
