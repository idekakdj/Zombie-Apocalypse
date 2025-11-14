import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * SurvivorTwo - Fast survivor with lower HP, named Jayden
 * 
 * @author Paul
 * 
 */
public class SurvivorTwo extends Survivors
{
    private int speed = 6;
    private GreenfootImage p2 = new GreenfootImage("jayden.png");
    private SuperStatBar hpBar;
    
    public SurvivorTwo(){
        enableStaticRotation();
        startHP = 50;
        hp = startHP;
        setImage(p2);
        // Create HP bar that stays at fixed position (null = don't follow)
        hpBar = new SuperStatBar(startHP, hp, null, 300, 40, 0, Color.CYAN, Color.DARK_GRAY, false, Color.YELLOW, 1);
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
        if(super.shield){
            hp -= (damage/2);
        } else {
            hp -= damage;
        }
        if (hp < 0){
            hp = 0;
        } 
        hpBar.update(hp);
    }
}
