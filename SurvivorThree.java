import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SurvivorThree here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SurvivorThree extends Survivors
{
    private int speed = 2;
    private int hp = 150;

    public SurvivorThree (){
        hp = super.startHP * 2;
        this.setImage("jayden.png");

    }
    public void act()
    {
        
    }
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
