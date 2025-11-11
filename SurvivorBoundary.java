import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Rectangle;
/**
 * Invisible boundaries for survivors.
 * 
 * @author Paul 
 */
public class SurvivorBoundary extends Actor
{
    Rectangle bounds;
    Color transparent = new Color (0,0,0,0);
    public SurvivorBoundary(int x, int y, int width, int height){
        bounds = new Rectangle(x, y, width, height);
        setImage(new GreenfootImage(1,1));
    }
    //To be called to check if survivor is within bounds
    public boolean contains(int x, int y){
        return bounds.contains(x, y);
    }
}
