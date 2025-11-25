import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Effect here.
 * 
 * @author Mr Cohen
 * @version (a version number or a date)
 */
public class Effect extends SuperSmoothMover
{
    protected int actCount;
    protected int totalFadeTime;
    protected GreenfootImage image;
    
    protected void fade (int timeLeft, int totalFadeTime){
        double percent = timeLeft / (double)totalFadeTime;
        if (percent > 1.00) return;
        int newTransparency = (int)(percent * 255);
        image.setTransparency (newTransparency);
    }

    /**
     * Act - do whatever the Effect wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        actCount--;
        fade (actCount, totalFadeTime);
        if (actCount == 0){
            getWorld().removeObject(this);
            return;
        }
    }
}