import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A simple melee weapon: the Bat.
 */
public class Bat extends Melee
{
    public Bat(int damage, int coolDown, int range)
    {
        super(damage, coolDown, range); // Call Melee constructor
    }

    public void act()
    {
        // Bat-specific behavior
    }

    public void playSound()
    {
        Greenfoot.playSound("bat_hit.wav"); // Example (make sure file exists)
    }

    public void attack()
    {
        
    }
}
