import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class Zombies here.
 * 
 * @author Cayden 
 * @version (a version number or a date)
 */
public abstract class Zombie extends SuperSmoothMover
{
    protected int damage;
    protected int health;
    protected double speed;
    protected int attackCooldown;
    
    // Jayden added code superstatbar:
    protected void updateHpBar()
    {
        
    }
    
    protected abstract void attack();
    
    protected abstract void checkHitSurvivor();
    
    protected abstract GreenfootImage getLeftImage();
    
    protected abstract GreenfootImage getRightImage();
    
    public void act()
    {
        // Test code: Press SPACE to kill zombies in order (Penguin -> Regular -> Giant -> Boss)
        if ("space".equals(Greenfoot.getKey())) {
            World world = getWorld();
            if (world != null) {
                Penguin penguin = (Penguin) world.getObjects(Penguin.class).stream().findFirst().orElse(null);
                if (penguin != null) {
                    penguin.health = 0;
                }
                else {
                    Regular regular = (Regular) world.getObjects(Regular.class).stream().findFirst().orElse(null);
                    if (regular != null) {
                        regular.health = 0;
                    }
                    else {
                        Giant giant = (Giant) world.getObjects(Giant.class).stream().findFirst().orElse(null);
                        if (giant != null) {
                            giant.health = 0;
                        }
                        else {
                            Boss boss = (Boss) world.getObjects(Boss.class).stream().findFirst().orElse(null);
                            if (boss != null) {
                                boss.health = 0;
                            }
                        }
                    }
                }
            }
        }
        
        if (!isDead()) {
            moveZombie();
            checkHitSurvivor(); 
            if (attackCooldown > 0) {
                attackCooldown--;
            }
        } else {
            killZombie();
        }
    }
    
    protected void takeDamage(int dmg) {
        health -= dmg;
    }
    
    protected boolean isDead() {
        return health <= 0;
    }
    
    protected void killZombie() {
        getWorld().removeObject(this);
    }
    
    protected void moveZombie() {  
        ArrayList<Survivors> survivors = (ArrayList<Survivors>)getWorld().getObjects(Survivors.class);
        
        if (!survivors.isEmpty()) {
            Survivors survivor = survivors.get(0);
            int x = survivor.getX();
            int y = survivor.getY();
            
            turnTowards(x, y);
            
            if (getX() < x) {
                setImage(getLeftImage()); 
            } else {
                setImage(getRightImage()); 
            }
            
            move(speed);
        }
    }
}