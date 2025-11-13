import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Rectangle;
/**
 * Invisible boundaries for survivors.
 * 
 * @author Paul assisted by Claude
 */


public class SurvivorBoundary extends Actor
{
    Rectangle bounds;
    Color transparent = new Color (0,0,0,0);
    
    public SurvivorBoundary(int centerX, int centerY, int width, int height){
        bounds = new Rectangle(centerX - width/2, centerY - height/2, width, height);
        setImage(new GreenfootImage(1,1));
    }
    
    public boolean contains(int x, int y){
        return bounds.contains(x, y);
    }
}
