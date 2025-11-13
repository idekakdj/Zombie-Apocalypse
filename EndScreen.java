import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
        setBackground("diedworld");
        prepareButtons();
    }
    private void prepareButtons(){
        Button returnToStart = new Button ("Back to Start", 80, 300, Color.WHITE, 5, Color.BLACK, 40, Color.BLACK,"backtostart",false);
        addObject(returnToStart, getWidth()/2, 650);
    }
}
