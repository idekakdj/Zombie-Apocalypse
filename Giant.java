import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Giant here.
 * 
 * @author Cayden
 * @version (a version number or a date)
 */
public class Giant extends Zombie
{
    private final static int GIANT_HEALTH = 250;
    private final static double GIANT_SPEED = 1;
    private final static int GIANT_DAMAGE = 10;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;

    private SuperStatBar hpBar;

    public Giant()
    {
        // fill in stats in brackets to give attributes to Giant
        hpBar = new SuperStatBar(); 
       
        health = GIANT_HEALTH;
        maxHealth = GIANT_HEALTH;  
 
    hpBar = new SuperStatBar(maxHealth, health, this, 50, 5, 0, Color.GREEN, Color.RED, true);

        speed = GIANT_SPEED;
        damage = GIANT_DAMAGE;
        
        enableStaticRotation();
        
        leftImage = new GreenfootImage("Big Zombie.png");
        leftImage.scale(80, 140);
        
        rightImage = new GreenfootImage("Big Zombie.png");
        rightImage.mirrorHorizontally();
        rightImage.scale(80, 140);
        
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
