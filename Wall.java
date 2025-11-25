import greenfoot.*;
import java.util.List;

public class Wall extends Actor
{
    private int health;
    private int maxHealth;
    
    public Wall() {
        maxHealth = 50;
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
    
    private void updateAppearance() {
        GreenfootImage img = getImage();
        
        double healthPercent = (double) health / maxHealth;
        int transparency = (int)(healthPercent * 200) + 55;
        img.setTransparency(transparency);
    }
}