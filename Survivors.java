import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Survivors here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Survivors extends Actor
{
    protected final int startHP = 100;
    
    public void act()
    {
        
    }
    public boolean checkHit(){
        this.getInteresectingObects(Zombies.class);
        ArrayList<Zombies> zombies = 
    }
}
