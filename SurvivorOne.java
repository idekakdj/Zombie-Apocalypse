import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * The survivor named Cayden, is the default for speed and hp values.
 * 
 * @author Paul 
 * 
 */
public class SurvivorOne extends Survivors
{
    private int speed = 4;
    private GreenfootImage p1 = new GreenfootImage("chiu.png");
    private SuperStatBar hpBar;
    
    public SurvivorOne(){
        enableStaticRotation();
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
        if(hp <= 0){
            Greenfoot.setWorld(new EndScreen());
            return;
        }
        
        super.act();
        
        // Update HP bar
        hpBar.update(hp);
        
        
        moveIntelligently(speed);
    }
    
    public void takeDamage(int damage){
        hp -= damage;
        if (hp < 0){
            hp = 0;  
        }
        
        
        
        hpBar.update(hp);
    }
}
