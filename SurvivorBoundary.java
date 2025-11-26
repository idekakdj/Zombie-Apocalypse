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
    /**
     * constructor to make invisible regtanle
     * @param centerX, x position to spawn
     * @param centerY, y position to spawn
     * @param width, rectangle width
     * @param height, rectangle height
     */
    public SurvivorBoundary(int centerX, int centerY, int width, int height){
        bounds = new Rectangle(centerX - width/2, centerY - height/2, width, height);
        setImage(new GreenfootImage(1,1));
    }
    /**
     * helps determine if survivor is inside the boundary
     * @param x, x value in box
     * @param y, y value in box
     * 
     * @returns true if position is within the rectangle
     */
    public boolean contains(int x, int y){
        return bounds.contains(x, y);
    }
}
