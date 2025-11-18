import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Paul
 * 
 * 
 * Heals the user by 25hp when used. Has two uses.
 * 
 */
public class Bandages extends Actor
{
    private Survivors owner;
    private int usageTime = 100;
    private int useCounter;
    GreenfootImage bandages = new GreenfootImage("bandages.png");
    private final int offsetX = 20;
    private boolean daytime;
    public Bandages(Survivors owner){
        this.owner = owner;
        setImage(bandages);
        useCounter = 2;
    }
    public void act()
    {
        World world = getWorld();
        followOwner();
        if(daytime){
            show();
            usageTime--;
            useCounter--;
            if(usageTime == 0 && useCounter != 0){
            owner.heal();            
            } 
        }
        
        usageTime = 100;
    }
    public void hide(){
        setImage(new GreenfootImage(1,1));
    }
    public void show (){
        setImage(bandages);
    }
    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX() - offsetX, owner.getY());
        }
    }
}
