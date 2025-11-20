import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Wall class that creates a barrier. Can be damaged by zombies.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Actor
{
    private int health;
    private int maxHealth;
    
    public Wall()
    {
        maxHealth = 100;
        health = maxHealth;
        GreenfootImage img = new GreenfootImage("wall.png");
        setImage(img);
    }
    
    public void act() {
        checkHitSurvivor();
    }
    
    private void checkHitSurvivor() {
        if (isTouching(Zombie.class)) {
            takeDamage(10);
        }
    }
    
    private void takeDamage(int damage) {
        health -= damage;
        updateAppearance();
        
        if (health <= 0) {
            getWorld().removeObject(this);
        }
    }
    
    private void updateAppearance() {
        GreenfootImage img = getImage();
        
        double healthPercent = (double) health / maxHealth;
        int transparency = (int)(healthPercent * 200) + 55;
        img.setTransparency(transparency);
    }
    
    private void draw() {
        
    }
}