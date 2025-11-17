import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Gun here.
 * 
 * @author Jayden 
 * @version (a version number or a date)
 */
public class Gun extends Weapon
{
    protected int damage;
    protected int ammo;
    protected int maxAmmo;
    protected int fireRate;
    /**
     * Act - do whatever the Gun wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public Gun(int damage, int maxAmmo, int fireRate)
    {
        this.damage = damage;
        this.maxAmmo = maxAmmo;
        this.fireRate = fireRate;
    }
    public void act()
    {
        // Add your action code here.
    }
    
    public void attack()
    {
        
    }
    
    public void playSound()
    {
        
    }
}
