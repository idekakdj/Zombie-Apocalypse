import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Melee here.
 * 
 * @author Jayden
 * @version (a version number or a date)
 */
public abstract class Melee extends Weapon
{
    protected int damage;
    protected int range;
    protected int coolDown;
    /**
     * Act - do whatever the Melee wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public Melee(int damage, int coolDown, int range)
    {
        this.range = range;
        this.coolDown = coolDown;
        this.damage = damage;
    }
    
    public void act()
    {
        // Add your action code here.
    }
    
   
}
