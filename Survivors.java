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
    protected int hp;
    public void act()
    {
        hp = startHP;
    }
    public void takeDamage(int damage){
        hp = hp - damage;
    }
}
