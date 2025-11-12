import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * 
 * 
 * @author Paul assisted by Claude
 */
public class SurvivorOne extends Survivors
{
    private int hp;
    private int speed = 4;
    GreenfootImage p1 = new GreenfootImage("chiu.png");
    public SurvivorOne(){
        hp = super.startHP;
        p1.scale(75,75);
        setImage(p1);
        
    }
    public void act()
    {
        super.act();
        
    }
    
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
