import greenfoot.*;
import java.util.List;

public class Wall extends Actor
{
    private int health;
    private int maxHealth;
    
    public Wall() {
        maxHealth = 100;
        health = maxHealth;
        GreenfootImage img = new GreenfootImage("wall.png");
        setImage(img);
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        updateAppearance();
        
        if (health <= 0) {
            getWorld().removeObject(this);
        }
    }
    
    public void repair(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        updateAppearance();
    }
    
    public boolean isFullyRepaired() {
        return health >= maxHealth;
    }
    
    public int getHealth() {
        return health;
    }
    
    private void updateAppearance() {
        GreenfootImage img = new GreenfootImage("wall.png");
        setImage(img);
        
        double healthPercent = (double) health / maxHealth;
        int transparency = (int)(healthPercent * 200) + 55;
        img.setTransparency(transparency);
    }
}