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
        // fill in stats in brackets to give attributes to Boss
        hpBar = new SuperStatBar(); 
       
        health = BOSS_HEALTH;
        speed = BOSS_SPEED;
        damage = BOSS_DAMAGE;
        
        enableStaticRotation();
        
        leftImage = new GreenfootImage("Big Boss Boy.png");
        leftImage.scale(230, 230);
        
        rightImage = new GreenfootImage("Big Boss Boy.png");
        rightImage.mirrorHorizontally();
        rightImage.scale(230, 230);
        
        setImage(leftImage);
    }
    
    public void act()
    {
        super.act();
        
        if (Greenfoot.isKeyDown("k")) {
            health = 0;
        }
    }
    
    protected void killZombie() {
        World world = getWorld();
        int x = getX();
        int y = getY();
        
        int[] xOffsets = {-40, 0, 40};  // Left, center, right
        int[] yOffsets = {-30, 30, 0};  // Top, bottom, center
        
        if (world != null) {
            world.addObject(new Explosion(), x, y);
        }
        
        for(int i = 0; i < 3; i++) {
            int spawnX = x + xOffsets[i];
            int spawnY = y + yOffsets[i];
            
            world.addObject(new Penguin(), spawnX, spawnY);
        }
        
        world.removeObject(this);
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
