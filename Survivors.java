import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Super class for survivors
 * 
 * @author Paul with assistance from Claude 
 * 
 */
public abstract class Survivors extends Actor
{
    protected int startHP;
    protected int hp;

    protected final int DETECTION = 100;
    protected boolean melee = false;
    protected boolean gun = false;
    protected boolean armor = false;
    protected boolean bandages = false;
    protected boolean hasBat = false; // ensures bat is only spawned once

    
    public void act()
    {
        //getUserItems();
    }
    
    public void getUserItems(){
        ChooseWorld world = (ChooseWorld) getWorld();
        if (world != null){
            melee = world.MELEE;
            gun = world.GUN;
            armor = world.SHIELD;
            bandages = world.BANDAGES; 
        }
    }
    
    //From Claude
    public int getAngleTowards(Actor target)
    {
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        
        // Convert to degrees (0 = right, 90 = down, 180 = left, 270 = up)
        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) Math.toDegrees(angleInRadians);
        
        // Adjust to Greenfoot's coordinate system (0 = up/north)
        angleInDegrees = (angleInDegrees + 90) % 360;
        if (angleInDegrees < 0) angleInDegrees += 360;
        
        return angleInDegrees;
    }
    
    public void moveAway(int angleApproaching, int speed){
        int escapeAngle = (angleApproaching + 180) % 360;
        setRotation(escapeAngle);
        move(speed);
    }
    
    public abstract void takeDamage(int damage);
    
    public int getHP() {
        return hp;
    }
    
    public int getMaxHP() {
        return startHP;
    }
    
     protected void spawnBat() {
        if (hasBat) return; // make sure we only spawn it once

        World w = getWorld();
        if (w != null) {
            Bat bat = new Bat(10, 100, 30, this);
            w.addObject(bat, getX(), getY());
            hasBat = true;
        }
    }

}