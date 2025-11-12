import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SurvivorTwo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SurvivorTwo extends Survivors
{
    private int speed = 6;
    private int hp = 75;
    GreenfootImage p2 = new GreenfootImage("chapman.png");
    public SurvivorTwo (){
        hp = super.startHP/2;
        p2.scale(75,75);
        this.setImage(p2);
    }
    public void act()
    {
        super.act();
    }
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
