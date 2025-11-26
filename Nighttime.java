import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Nighttime/Storm effect based off of class vehicle sim storm demo taken from my vehicle sim project
 * 
 * @author Cayden with some code from Mr. Cohen 
 * @version (a version number or a date)
 */
public class Nighttime extends Effect
{
    private boolean firstAct;
    
    private double speed;
    private final int SKY_HEIGHT = 190;
    private final int FADE_IN_TIME = 90;
    private int fadeInCounter;
    private final int STAR_DENSITY = 200;
    /**
     * World effect constructor for night cycle
     */
    public Nighttime () {
        drawimage();
        actCount = 1200;
        totalFadeTime = 90;
        fadeInCounter = FADE_IN_TIME;
        firstAct = true;
        speed = 1.5;
        
        image.setTransparency(0);
    }
    
    public void addedToWorld (World w){
        if (firstAct){
            firstAct = false;
        }
    }
    /**
     * fades in and out when used
     */
    public void act () {
        if (fadeInCounter > 0) {
            fadeIn(fadeInCounter, FADE_IN_TIME);
            fadeInCounter--;
        }
        super.act();
        
        if (getWorld() == null){
            return;
        }
    }
    
    private void fadeIn(int timeLeft, int totalTime) {
        double percent = 1.0 - (timeLeft / (double)totalTime);
        int newTransparency = (int)(percent * 255);
        image.setTransparency(newTransparency);
    }
    
    private void drawimage (){
        image = new GreenfootImage(2048,800);
        image.setColor (new Color(0, 0, 0, 120));
        image.fill();
        image.setColor(Color.YELLOW);
        for (int i = 0; i < STAR_DENSITY; i++){
            int randX = Greenfoot.getRandomNumber(image.getWidth());
            int randY = Greenfoot.getRandomNumber(SKY_HEIGHT);
            int randSize = 4 + Greenfoot.getRandomNumber(3);
            image.fillOval(randX, randY, randSize, randSize);
        }
        setImage(image);
    }

}