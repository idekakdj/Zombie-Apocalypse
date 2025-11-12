import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * SurvivorThree - Tank survivor with high HP
 * 
 * @author Paul
 * @version 1.0
 */
public class SurvivorThree extends Survivors
{
    private int speed = 2;
    private GreenfootImage p3 = new GreenfootImage("survivorthree.png");
    private SuperStatBar hpBar;
    
    public SurvivorThree(){
        startHP = 200;
        hp = startHP;
        setImage(p3);
        // Create HP bar that stays at fixed position (null = don't follow)
        hpBar = new SuperStatBar(startHP, hp, null, 300, 40, 0, Color.ORANGE, Color.BLACK, false, Color.YELLOW, 1);
    }
    
    @Override
    protected void addedToWorld(World w) {
        // Add HP bar at top center of world
        w.addObject(hpBar, w.getWidth() / 2, 30);
        hpBar.update(hp);
    }
    
    public void act()
    {
        super.act();
        
        // Update HP bar
        hpBar.update(hp);

        List<Zombie> nearbyZombies = this.getObjectsInRange(DETECTION, Zombie.class);
        for(Zombie z : nearbyZombies){
            moveAway(getAngleTowards(z), speed);
        }
    }
    
    public void takeDamage(int damage){
        hp -= damage;
        if (hp < 0) hp = 0;
        hpBar.update(hp);
    }
}
