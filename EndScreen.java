import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Triggers when survivor dies and has a button to link back to the StartWorld
 * 
 * @author Paul
 */
public class EndScreen extends World
{
    private GreenfootImage zombie = new GreenfootImage("Zombie.png");
    private GreenfootImage right = new GreenfootImage("rightbubble.png");
    /**
     * Constructor for objects of class EndScreen.
     * 
     */
    public EndScreen()
    {    
        super(1024, 700, 1); 
        setBackground("diedworld.png");
        prepareButtons();
        prepareDialogue();
    }
    //Button back to start
    private void prepareButtons(){
        Button returnToStart = new Button ("Back to Start", 80, 300, Color.WHITE, 5, Color.BLACK, 40, Color.BLACK,"backtostart",false);
        addObject(returnToStart, getWidth()/2, 655);
    }
    //Dialogue
    private void prepareDialogue(){
        setBackground("diedworld.png");
        zombie.scale(200,200);
        getBackground().drawImage(zombie, (getWidth()/2) - 100, 450);
        right.scale(300, 300);
        getBackground().drawImage(right, 180, 220);
        // Draw text in speech bubble
        getBackground().setColor(Color.BLACK);
        getBackground().setFont(new Font("Arial", 50));
        getBackground().drawString("...", 290, 390);
    }
}
