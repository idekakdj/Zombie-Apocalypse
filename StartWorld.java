import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartWorld here.
 * 
 * @author Paul
 * 
 */
public class StartWorld extends World
{
    
    GreenfootImage world = new GreenfootImage("startworld.png");
    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {    
        super(1024, 700, 1); 
        setBackground(world);
        prepare();
    }
    private void prepare(){
        Button chooseworld = new Button("Start", 100, 400, Color.GREEN, 5, Color.BLACK, 70,Color.WHITE,"choose",false);
        addObject(chooseworld, getWidth()/2, (getHeight()/4) * 3);
        
    }
    public void act(){
        
    }
}
