import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * 
 * @author Paul assisted by Claude
 * 
 * Keeps track of waves and score.
 * Score is calculated on number of Zombies killed (you don't have to kill all the 
 * zombies to move to another wave just last until day)
 */

    
public class ScoreTracker extends Actor
{
    private int score = 0;
    private int waveNum = 0;
    private GreenfootImage rect;
    private Color color;
    private Color borderColor;
    private int borderWidth;
    private int width;
    private int height;
    public int numRegular = 0;
    public int numPenguin = 0;
    public int numBoss = 0;
    public int numSpecial = 0;
    
    public ScoreTracker(int width, int height, Color color, int borderWidth, Color borderColor){
        this.color = color;
        this.width = width;
        this.height = height;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        
        rect = new GreenfootImage(width, height);
        createTrackerImage();
        setImage(rect);
    }
    
    private void createTrackerImage(){
        rect = new GreenfootImage(width, height);
        if (borderWidth > 0) {
            rect.setColor(borderColor);
            rect.fillRect(0, 0, width, height);
            
            rect.setColor(color);
            rect.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        } else {
            rect.setColor(color);
            rect.fill();
        }
        
        rect.setColor(Color.WHITE);
        rect.setFont(new Font("Arial", 20));
    }
    
    public void act()
    {
        updateScore();
    }
    
    public void updateScore(){
        if (getWorld() instanceof GameWorld){
            GameWorld world = (GameWorld) getWorld();
            score = (numRegular * 10) + (numPenguin * 20) + (numSpecial * 20) + (numBoss * 100);
            createTrackerImage();
            rect.drawString("Wave: " + world.wavesCounter, this.width/5, 28);
            rect.drawString("Score: " + score, (this.width/5) * 3, 28);
            setImage(rect);
        }
    }
}
