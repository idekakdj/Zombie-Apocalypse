import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Class for walls that stop zombies from reaching the survivor
 * 
 * @author Cayden and hp bars by Jayden
 * @version (a version number or a date)
 */
public class Wall extends Actor
{
    private int health;
    private int maxHealth;
    /**
     * constructor for wall, sets health and image
     */
    public Wall() {
        maxHealth = 100;
        health = maxHealth;
        GreenfootImage img = new GreenfootImage("wall.png");
        setImage(img);
    }
    
    /**
     * Makes wall take damage and update its appearance 
     * 
     * @param dmg amount of damage taken by wall
     */
    public void takeDamage(int dmg) {
        health -= dmg;
        updateAppearance();
        
        if (health <= 0) {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Method used by survivor to repair the walls 
     * 
     * @param amount amount of health that wall gains 
     */
    public void repair(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        updateAppearance();
    }
    
    /**
     * Getter method for if the wall is at max health 
     * 
     * @return boolean true if health is greater or equal to maxHealth false if otherwise 
     */
    public boolean isFullyRepaired() {
        return health >= maxHealth;
    }
    
    /**
     * Getter method to get the current health of the wall
     * 
     * @return int health the current health 
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Method to make the walls become more transparent the less health they have (Assisted by Claude)
     */
    private void updateAppearance() {
        GreenfootImage img = new GreenfootImage("wall.png");
        setImage(img);
        
        double healthPercent = (double) health / maxHealth;
        int transparency = (int)(healthPercent * 200) + 55;
        img.setTransparency(transparency);
    }
}