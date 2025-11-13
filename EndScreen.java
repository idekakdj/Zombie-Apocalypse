import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Triggers when survivor dies and has a button to link back to the StartWorld
 * 
 * @author Paul
 */
public class EndScreen extends World
{

    /**
     * Constructor for objects of class EndScreen.
     * 
     */
    public EndScreen()
    {    
        super(1024, 700, 1); 
        setBackground("diedworld.png");
        prepareButtons();
    }
    private void prepareButtons(){
        Button returnToStart = new Button ("Back to Start", 80, 300, Color.WHITE, 5, Color.BLACK, 40, Color.BLACK,"backtostart",false);
        addObject(returnToStart, getWidth()/2, 655);
    }
}
