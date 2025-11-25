import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Screen when you win after surviving 5 waves.
 * 
 * @author Paul
 * 
 */
public class WinScreen extends World
{
    GreenfootImage bg = new GreenfootImage("winscreen");
    public WinScreen()
    {    
        super(1024, 700, 1);
        setBackground(bg);
    }
}
