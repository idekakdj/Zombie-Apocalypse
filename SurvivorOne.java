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
    /**
     * getter for hp bar for this survivor
     */
    public SuperStatBar getHPBar(){
        return hpBar;
    }
    /**
     * constructor for survivor one, sets hp value and hp bar
     */
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
        w.addObject(hpBar, w.getWidth() / 3, 30);
        hpBar.update(hp);
    }
    /**
     * updates hp bar, sets world to end screen if survivor dies and calls super act method for movement
     */
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
    /**
     * calculates damages taken, take into account if shield is selected and updates hp bar
     * @param damage, amount of damage to be taken
     */
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
