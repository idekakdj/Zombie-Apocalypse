import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Super class for survivors
 * 
 * @author Paul with assistance from Claude 
 * @version (a version number or a date)
 */
public abstract class Survivors extends Actor
{
    protected final int startHP = 100;
    protected int hp;
    private final int DETECTION = 100;
    public void act()
    {
        hp = startHP;
        List<Zombies> nearbyZombies = this.getObjectsInRange(DETECTION, Zombies.class);
        for(Zombies z : nearbyZombies){
            int x = z.getX();
            int y = z.getY();
            moveAway(getAngleTowards(z));
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
    public void moveAway(int angleApproaching){
        int escapeAngle = (angleApproaching + 180) % 360;
        setRotation(escapeAngle);
        move(3);
    }
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
