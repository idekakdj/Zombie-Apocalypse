import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Weapon here.
 * 
 * @author Jayden 
 * @version (a version number or a date)
 */
public abstract class Weapon extends Actor
{
    /**
     * Act - do whatever the Weapon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    //protected int damage; // number of hitpoints taken away per shot
    //protected int ammo; // number of shots available
    //protected int maxAmmo; // maximum number of shots per magazine
    //protected int fireRate; // number of shots fired per unit of time
   //protected int reloadTime; // time taken to reload
   
   // constructor for weapon
   // for every subclass of weapon, it will call upon the weapon parameters wit hits own values
   
    public Weapon()
    {
        
       
    }
    
    public void act()
    {
        
    }
    
  
    
    public abstract void attack();
    

    
}
