import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Melee weapons superclass
 * 
 * @author Jayden
 * 
 */
public abstract class Melee extends Actor
{
    protected int damage;
    protected int range;
    protected int coolDown;
    /**
     * Superclass constructor for melee weapons
     * @param damage, damage the weapon deals
     * @param coolDown, cooldown before attacking again
     * @param range, range for attacking zombies
     * 
     */
    
    public Melee(int damage, int coolDown, int range)
    {
        this.range = range;
        this.coolDown = coolDown;
        this.damage = damage;
    }
    
   
}
