import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Shield here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shield extends Actor
{
    
    private final int SHIELD_DURABILITY = 100;
    GreenfootImage shield = new GreenfootImage("shield.png");
    private final int offsetX = 20;
    private Survivors owner;
    public Shield (Survivors owner){
        this.owner = owner;
        shield.scale(50,50);
        setImage(shield);
        
    }
    public void act()
    {
        followOwner();;
    }
    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX() - offsetX, owner.getY());
        }
    }
}
