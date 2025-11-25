import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TitleScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TitleScreen extends World
{

    GreenfootImage bg = new GreenfootImage ("titlescreen.png");
    private int countdown = 180;
    /**
     * New title screen 
     */
    public TitleScreen()
    {    
        super(1024, 700, 1); 
        setBackground(bg);
        countdown = 180;
    }
    /**
     * Automatically changes to StartWorld after 3 seconds.
     */
    public void act(){
        countdown--;
        if(countdown == 0 ){
            Greenfoot.setWorld(new StartWorld());
        }
    }
}
