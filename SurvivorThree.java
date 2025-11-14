import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * SurvivorThree - Tank survivor with high HP, named Paul
 * 
 * @author Paul
 * 
 */
public class SurvivorThree extends Survivors
{
    private int speed = 2;
    private GreenfootImage p3 = new GreenfootImage("chapman.png");
    private SuperStatBar hpBar;
    
    public SurvivorThree(){
        enableStaticRotation();
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
