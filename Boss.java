import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Zombie
{
    private final static int BOSS_HEALTH = 250;
    private final static double BOSS_SPEED = 0.5;
    private final static int BOSS_DAMAGE = 10;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;

    private SuperStatBar hpBar;

    public Boss()
    {
        // fill in stats in brackets to give attributes to boss
        hpBar = new SuperStatBar(); 
       
        health = BOSS_HEALTH;
        speed = BOSS_SPEED;
        damage = BOSS_DAMAGE;
        
        enableStaticRotation();
        
        leftImage = new GreenfootImage("Big Zombie.png");
        leftImage.scale(100, 160);
        
        rightImage = new GreenfootImage("Big Zombie.png");
        rightImage.mirrorHorizontally();
        rightImage.scale(100, 160);
        
        setImage(leftImage);
    }
    
    public void act()
    {
        super.act();
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
