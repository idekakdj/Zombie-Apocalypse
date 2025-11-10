import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Rectangle;
/**
 * Invisible boundaries for survivors.
 * 
 * @author Paul 
 * @version (a version number or a date)
 */
public class SurvivorBoundary extends Actor
{
    Rectangle bounds;
    Color transparent = new Color (0,0,0,0);
    public SurvivorBoundary(int x, int y, int width, int height){
        bounds = new Rectangle(x, y, width, height);
        setImage(new GreenfootImage(1,1));
    }
    public boolean contains(int x, int y){
        return bounds.contains(x, y);
    }
}
