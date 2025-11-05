import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class Survivors here.
 * 
 * @author (your name) 
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
            
        }
    }
    
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
