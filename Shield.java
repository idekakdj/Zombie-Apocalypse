import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Shield class for constructor
 * 
 * @author Paul
 * 
 */
public class Shield extends Actor
{
    
    GreenfootImage shield = new GreenfootImage("shield.png");
    private final int offsetX = 20;
    private Survivors owner;
    /**
     * Shield constructor
     * @param owner, tracks owner to follow
     */
    public Shield (Survivors owner){
        this.owner = owner;
        shield.scale(50,50);
        setImage(shield);
        
    }
    /**
     * follows owner and updates position every act
     */
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
