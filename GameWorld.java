import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameWorld extends World
{

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1024, 700, 1); 
        
        
        prepare();
        
    }
    
    private void prepare() {
        spawnZombieAtEdge();
        
        //spawn survivors in middle
        SurvivorOne s1 = new SurvivorOne();
        addObject(s1, getWidth()/2, getHeight()/2);
    }
    
    private void spawnZombieAtEdge() {
        Regular zombie = new Regular();
        
        // Randomly choose which edge (0=top, 1=right, 2=bottom, 3=left)
        int edge = Greenfoot.getRandomNumber(4);
        int x, y;
        
        if (edge == 0) {  // Top
            x = Greenfoot.getRandomNumber(getWidth());
            y = 0;
        } else if (edge == 1) {  // Right
            x = getWidth() - 1;
            y = Greenfoot.getRandomNumber(getHeight());
        } else if (edge == 2) {  // Bottom
            x = Greenfoot.getRandomNumber(getWidth());
            y = getHeight() - 1;
        } else {  // Left
            x = 0;
            y = Greenfoot.getRandomNumber(getHeight());
        }
        
        addObject(zombie, x, y);
    }
}
