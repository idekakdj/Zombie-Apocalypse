import greenfoot.*;

/**
 * Screen when you win after surviving 5 waves.
 * 
 * @author Paul
 * 
 */
public class WinScreen extends World
{
    GreenfootImage bg = new GreenfootImage("winscreen.png");
    private GreenfootImage right = new GreenfootImage("rightbubble.png");
    private GreenfootImage paul = new GreenfootImage("chapman.png");
    private GreenfootImage cayden = new GreenfootImage("chiu.png");
    private GreenfootImage jayden = new GreenfootImage("jayden.png");
    private GreenfootImage zombie = new GreenfootImage("Zombie.png");
    
    private boolean survivorOne;
    private boolean survivorTwo;
    private boolean survivorThree;
    
    private int countdown = 180;
    /**
     * Constructor with survivor selection
     * @param s1 - true if Paul (SurvivorThree) was selected
     * @param s2 - true if Jayden (SurvivorTwo) was selected
     * @param s3 - true if Cayden (SurvivorOne) was selected
     */
    public WinScreen(boolean s1, boolean s2, boolean s3)
    {    
        super(1024, 700, 1);
        this.survivorOne = s1;
        this.survivorTwo = s2;
        this.survivorThree = s3;
        setBackground(bg);
        drawDialogue();
        countdown = 180;
    }
    /**
     * Changes dialogue from survivor to zombie after he tells us hes been infected.
     */
    public void act(){
        countdown--;
        if(countdown == 0){
            setBackground("winscreen.png");
            zombie.scale(200,200);
            getBackground().drawImage(zombie, (getWidth()/2) - 100, 450);
            right.scale(300, 300);
            getBackground().drawImage(right, 180, 220);
            // Draw text in speech bubble
            getBackground().setColor(Color.BLACK);
            getBackground().setFont(new Font("Arial", 50));
            getBackground().drawString("...", 290, 390);
            Button returnToStart = new Button ("Back to Start", 80, 300, Color.BLACK, 5, Color.BLACK, 40, Color.WHITE,"backtostart",false);
            addObject(returnToStart, 850, 655);
        }
    }
    private void drawDialogue()
    {
        // Scale and position speech bubble (moved more to center)
        right.scale(500, 500);
        getBackground().drawImage(right, -20, 80);
        
        // Draw text in speech bubble
        getBackground().setColor(Color.BLACK);
        getBackground().setFont(new Font("Arial", 20));
        getBackground().drawString("So we did survive in the end...", 70, 260);
        getBackground().drawString("Or did we...?", 70, 310);
        getBackground().drawString("It looks like I've been bitten...", 70, 360);
        // Draw the selected survivor in the middle-bottom of screen
        drawSelectedSurvivor();
    }
    
    private void drawSelectedSurvivor()
    {
        GreenfootImage selectedSurvivor = null;
        
        // Determine which survivor was selected
        if (survivorThree) {
            selectedSurvivor = paul;
        } else if (survivorTwo) {
            selectedSurvivor = jayden;
        } else if (survivorOne) {
            selectedSurvivor = cayden;
        }
        
        // If a survivor was selected, draw them
        if (selectedSurvivor != null) {
            selectedSurvivor.scale(200, 200);
            getBackground().drawImage(selectedSurvivor, (getWidth()/2) - 100, 450);
        }
    }
}
