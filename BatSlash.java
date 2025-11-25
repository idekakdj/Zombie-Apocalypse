import greenfoot.*;

/**
 * Bat slashing animation effect
 * 
 * @author Abithan, altered by Paul and Claude
 * 
 */
public class BatSlash extends Actor
{
    private GreenfootImage[] slashFrames;
    private int animationSpeed = 50; // milliseconds per frame
    private int frameIndex = 0;
    private int actCount = 0;
    private int framesPerImage = 3; // acts to wait before changing image
    
    /**
     * Constructor - Simple version with default settings
     */
    public BatSlash()
    {
        this(6, 5); // 4 frames, 50ms each
    }
    
    /**
     * Constructor - Customizable version
     * 
     * @param numFrames Number of animation frames (images)
     * @param speed Acts between frame changes (lower = faster)
     */
    public BatSlash(int numFrames, int speed)
    {
        slashFrames = new GreenfootImage[numFrames];
        this.framesPerImage = speed;
        
        // Load slash animation frames
        for (int i = 0; i < slashFrames.length; i++)
        {
            slashFrames[i] = new GreenfootImage("images/batslash" + i + ".png");
            slashFrames[i].scale(80, 80);
        }
        
        setImage(slashFrames[0]);
    }
    
    /**
     * Animate the sword slash
     */
    public void animateSlash()
    {
        actCount++;
        
        // Change frame every 'framesPerImage' acts
        if (actCount >= framesPerImage)
        {
            actCount = 0;
            frameIndex++;
            
            // Remove when animation completes
            if (frameIndex >= slashFrames.length)
            {
                if (getWorld() != null)
                {
                    getWorld().removeObject(this);
                }
                return;
            }
            
            setImage(slashFrames[frameIndex]);
        }
    }
    
    public void act()
    {
        animateSlash();
    }
}
