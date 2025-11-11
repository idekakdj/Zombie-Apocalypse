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
    protected final int startHP = 100;
    protected final int DETECTION = 100;
    protected int hp;
    protected boolean melee = false;
    protected boolean gun = false;
    protected boolean armor = false;
    protected boolean bandages = false;
    public void act()
    {
        //getUserItems();
    }
    
    public void getUserItems(){
        StartWorld world = (StartWorld) getWorld();
        if (world != null){
            melee = world.MELEE;
            gun = world.GUN;
            armor = world.ARMOR;
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
}
