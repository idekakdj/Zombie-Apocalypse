import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * 
 * 
 * @author Paul 
 */
public class SurvivorOne extends Survivors
{
    private int hp;
    private int speed = 4;
    public SurvivorOne(){
        hp = super.startHP;
        setImage(getImage());
    }
    public void act()
    {
        super.act();
        List<Zombie> nearbyZombies = this.getObjectsInRange(super.DETECTION, Zombie.class);
        for(Zombie z : nearbyZombies){
            int x = z.getX();
            int y = z.getY();
            super.moveAway(getAngleTowards(z),speed);
        }
    }
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
