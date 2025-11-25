import greenfoot.*;
/*
 * 
 * @author Paul assisted by Claude
 * 
 */
public class UpgradeProgressBar extends Actor
{
    private int width;
    private int height;
    private Color fillColor;
    private Color emptyColor;
    private int borderWidth;
    private Color borderColor;
    private String text;
    private int targetScore = 100;
    private int currentScore = 0;
    private boolean isComplete = false;
    private int glowCounter = 0;
    private GreenfootImage rect;
    boolean melee;
    boolean gun;
    public UpgradeProgressBar(int width, int height, Color fillColor, Color emptyColor, int borderWidth, Color borderColor, String text, boolean melee, boolean gun){
        this.width = width;
        this.height = height;
        this.fillColor = fillColor;
        this.emptyColor = emptyColor;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.text = text;
        this.melee = melee;
        this.gun = gun;
        drawBar();
        setImage(rect);
    }
    
    public void act()
    {
        updateProgress();
        
        if (isComplete) {
            glow();
        }
        if(isComplete){
            Survivors survivor = (Survivors)getWorld().getObjects(Survivors.class).get(0);
            if(gun && survivor != null){
                getWorld().removeObjects(getWorld().getObjects(Gun.class));
                MachineGun mg = new MachineGun (50,30, survivor);
                getWorld().addObject(mg, survivor.getX(), survivor.getY());
            } else if(melee && survivor != null){
                getWorld().removeObjects(getWorld().getObjects(Bat.class));
                Sword sword = new Sword(100, 40, 60, survivor);
                getWorld().addObject(sword, survivor.getX(), survivor.getY());
            }
            
        }
    }
    
    private void updateProgress(){
        if (getWorld() instanceof GameWorld) {
            GameWorld world = (GameWorld) getWorld();
            ScoreTracker tracker = world.getObjects(ScoreTracker.class).get(0);
            
            if (tracker != null) {
                currentScore = tracker.numRegular * 10 + tracker.numPenguin * 20 + tracker.numSpecial * 20 + tracker.numBoss * 100 + tracker.numGiant * 50;
                
                if (currentScore >= targetScore && !isComplete) {
                    isComplete = true;
                }
                
                drawBar();
                setImage(rect);
            }
        }
    }
    
    private void drawBar(){
        rect = new GreenfootImage(width, height);
        
        if(borderWidth > 0){
            rect.setColor(borderColor);
            rect.fillRect(0, 0, width, height);
            
            rect.setColor(emptyColor);
            rect.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
            
            int progressWidth = (int)((currentScore / (double)targetScore) * (width - (borderWidth * 2)));
            if (progressWidth > width - (borderWidth * 2)) {
                progressWidth = width - (borderWidth * 2);
            }
            
            rect.setColor(fillColor);
            rect.fillRect(borderWidth, borderWidth, progressWidth, height - (borderWidth * 2));
        } else {
            rect.setColor(emptyColor);
            rect.fillRect(0, 0, width, height);
            
            int progressWidth = (int)((currentScore / (double)targetScore) * width);
            if (progressWidth > width) {
                progressWidth = width;
            }
            
            rect.setColor(fillColor);
            rect.fillRect(0, 0, progressWidth, height);
        }
        
        rect.setColor(Color.WHITE);
        rect.setFont(new Font("Arial", 16));
        
        GreenfootImage textImg = new GreenfootImage(text + ": " + currentScore + "/" + targetScore, 16, Color.WHITE, new Color(0,0,0,0));
        int textX = (width - textImg.getWidth()) / 2;
        int textY = (height + textImg.getHeight()) / 2 - 3;
        rect.drawString(text + ": " + currentScore + "/" + targetScore, textX, textY);
    }
    
    private void glow(){
        glowCounter++;
        
        int transparency = (int)(128 + 127 * Math.sin(glowCounter * 0.1));
        
        GreenfootImage glowImg = new GreenfootImage(width + 10, height + 10);
        glowImg.setColor(new Color(255, 255, 0, transparency));
        glowImg.fillOval(0, 0, width + 10, height + 10);
        
        getWorld().getBackground().drawImage(glowImg, getX() - width/2 - 5, getY() - height/2 - 5);
        
        getWorld().getBackground().drawImage(rect, getX() - width/2, getY() - height/2);
    }
    
    public boolean isUpgradeReady() {
        return isComplete;
    }
    
    public void reset() {
        currentScore = 0;
        isComplete = false;
        glowCounter = 0;
    }
}
