import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartWorld here.
 * 
 * @author Paul
 * @version (a version number or a date)
 */
public class StartWorld extends World
{
    private int numSurvivors;
    boolean MELEE;
    boolean GUN;
    boolean ARMOR;
    boolean BANDAGES;
    GreenfootImage world = new GreenfootImage("startworld.png");
    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {    
        super(1024, 700, 1); 
        setBackground(world);
    }
}
