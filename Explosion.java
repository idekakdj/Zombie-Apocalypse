import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * 
 * Taken from my vehicle sim with colours changed 
 * 
 * @author Cayden
 * @version (a version number or a date)
 */
public class Explosion extends Effect
{
    private int maxDiameter;
    private int currentDiameter;
    private int growthRate;
    
    public Explosion() {
        maxDiameter = 240; // Maximum explosion size
        currentDiameter = 20; // Start small
        growthRate = 12; // How fast it expands
        
        actCount = 45; // Lasts 1.5 seconds
        totalFadeTime = 30; // Fades over last 1 second
        
        drawExplosion();
    }
    
    public void act() {
        // Expand the explosion
        if (currentDiameter < maxDiameter) {
            currentDiameter += growthRate;
            if (currentDiameter > maxDiameter) {
                currentDiameter = maxDiameter;
            }
            drawExplosion();
        }
        
        // Call parent's act to handle fading and removal
        super.act();
    }
    
    /**
     * Draws the green explosion circle
     */
    private void drawExplosion() {
        image = new GreenfootImage(maxDiameter, maxDiameter);
        
        // Draw multiple circles for a layered explosion effect
        // Outer green glow
        image.setColor(new Color(0, 255, 100, 150)); // Bright green with transparency
        int offset = (maxDiameter - currentDiameter) / 2;
        image.fillOval(offset, offset, currentDiameter, currentDiameter);
        
        // Inner bright lime core
        if (currentDiameter > 40) {
            int coreSize = currentDiameter * 2 / 3;
            int coreOffset = (maxDiameter - coreSize) / 2;
            image.setColor(new Color(150, 255, 50, 200)); // Bright lime green
            image.fillOval(coreOffset, coreOffset, coreSize, coreSize);
        }
        
        // Very bright center
        if (currentDiameter > 60) {
            int centerSize = currentDiameter / 3;
            int centerOffset = (maxDiameter - centerSize) / 2;
            image.setColor(new Color(200, 255, 150, 255)); // Very bright yellow-green
            image.fillOval(centerOffset, centerOffset, centerSize, centerSize);
        }
        
        setImage(image);
    }
}