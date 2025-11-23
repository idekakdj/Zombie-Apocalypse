import greenfoot.*;
/*
 * @author Jayden assisted by Paul
 */
public class Projectile extends Actor
{
    private int speed = 10;
    private int damage;
    private Zombie target;
    
    public Projectile(int damage, Zombie target)
    {
        this.damage = damage;
        this.target = target;
        GreenfootImage img = new GreenfootImage("bullet.png");
        img.scale(10, 10);
        setImage(img);
    }
    
    public void act()
    {
        // Check if target still exists
        if (target == null || target.getWorld() == null) {
            getWorld().removeObject(this);
            return;
        }
        
        // Turn toward target and move
        turnTowards(target.getX(), target.getY());
        move(speed);
    
        // Check for collision with any zombie (not just Regular)
        Zombie z = (Zombie)getOneIntersectingObject(Zombie.class);
        if (z != null && z.getWorld() != null)
        {
            z.takeDamage(damage);
            if (z.isDead()) {
                z.killZombie();
            }
            getWorld().removeObject(this);
            return;
        }
        
        // Remove if at edge
        if (isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}
