import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * SurvivorOne - Balanced survivor
 * 
 * @author Paul 
 * @version 1.0
 */
public class SurvivorOne extends Survivors
{
    private int speed = 4;
    private GreenfootImage p1 = new GreenfootImage("chiu.png");
    private SuperStatBar hpBar;
    
    public SurvivorOne(){
        startHP = 100;
        hp = startHP;
        p1.scale(75,75);
        setImage(p1);
        // Create HP bar that stays at fixed position (null = don't follow)
        hpBar = new SuperStatBar(startHP, hp, null, 300, 40, 0, Color.GREEN, Color.RED, false, Color.YELLOW, 1);
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
        if(hp <= 0){
            Greenfoot.setWorld(new EndScreen());
            return;
        }
    }
    
    public void takeDamage(int damage){
        hp -= damage;
        if (hp <= 80 && !hasBat) {
            spawnBat();
            hasBat = true; // prevents multiple bats
        }
        if (hp < 0){
            hp = 0;  
                        Greenfoot.stop(); // Stops the scenario execution

        }
        hpBar.update(hp);
    }
}
