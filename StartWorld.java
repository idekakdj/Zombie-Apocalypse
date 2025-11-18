import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * ZOMBIE APOCALYPSE SIMULATION:
 * 
 * In this dystopian world there are few survivors who have yet to become zombies
 * from the radiation in the world. The survivors Paul, Jayden and Cayden must choose
 * items to help them survive as long as possible.
 * 
 * The items affect the length of the simulation as you are allowed one weapon 
 * and two support items. Some combinations are a lot better than others. 
 * 
 * Features:
 * - Each survivor has different hp and speed values that also affect success in the
 * simulation. They have their own hp bars at the top left of the screen.
 * 
 * - Weapons will upgrade after a certain score milestone and help with later waves.
 * 
 * - The game ends if the survivor dies or survives past wave 5 and there are two ending
 * screens and dialogue if the survivor wins.
 * 
 * - There is dialogue at the start to explain a brief story and instructions for
 * the simulation.
 * 
 * - Has a fully reusable Button class that can be imported to other projects and is 
 * used to move worlds and select all items to be used.
 * 
 * 
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
        Button chooseworld = new Button("Start", 100, 400, Color.GREEN, 5, Color.BLACK, 70,Color.WHITE,"start",false);
        addObject(chooseworld, getWidth()/2, (getHeight()/4) * 3);
        
    }
    
}
