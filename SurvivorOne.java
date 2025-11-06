import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class SurvivorOne here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SurvivorOne extends Survivors
{
    private final int SPEED = 3;
    private int hp;
    private int speed = 4;
    public SurvivorOne(){
        hp = super.startHP;
        
    }
    public void act()
    {
        super.act();
        List<Zombies> nearbyZombies = this.getObjectsInRange(super.DETECTION, Zombies.class);
        for(Zombies z : nearbyZombies){
            int x = z.getX();
            int y = z.getY();
            super.moveAway(getAngleTowards(z),SPEED);
        }
    }
}
