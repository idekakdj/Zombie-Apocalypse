import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Big zombie, slower speed but higher damage and higher health
 * 
 * @author Cayden
 * 
 */
public class Giant extends Zombie
{
    private final static int GIANT_HEALTH = 150;
    private final static double GIANT_SPEED = 1;
    private final static int GIANT_DAMAGE = 5;
    
    private GreenfootImage leftImage;
    private GreenfootImage rightImage;

    private SuperStatBar hpBar;
    /**
     * constructor for giant zombie, makes new hp bar, has lower speed, higher damage and higher hp
     * 
     */
    public Giant()
    {
        // fill in stats in brackets to give attributes to Giant
        hpBar = new SuperStatBar(); 
       
        health = GIANT_HEALTH;
        maxHealth = GIANT_HEALTH;  
 
        hpBar = new SuperStatBar(maxHealth, health, this, 50, 5, -85, Color.GREEN, Color.RED, false, Color.ORANGE, 1);

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
    /**
     * updates hp bar and calls super act method
     */
    public void act()
    {
        super.act();
        hpBar.update(health);
    }
    /**
     * adds zombie to world
     * @param w, world to add to
     */
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
        return "Giant";
    }
}
