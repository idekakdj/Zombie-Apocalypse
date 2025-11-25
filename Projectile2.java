import greenfoot.*;
/*
 * Author Jayden 
 * 
 * Projectile constructor takes in two parameters
 * moveTowards method is inherited from the superclass SuperSmoothMover
 * The actor (projectile) is moved the third parameter number of pixels (speed)
 * to the target location (first two parameters x and y)
 * 
 * 
 * To use for anything other than the original Zombie Apocolypse game alter the 
 * handleClick() method, delete GreenfootImages and add your own click sound.
 */
public class Projectile2 extends SuperSmoothMover
{
    private int speed = 7;
    private int damage;
    private Zombie target;

    public Projectile2(int damage, Zombie target)
    {
        this.damage = damage;
        this.target = target;

        GreenfootImage img = new GreenfootImage("bullet.png");
        img.scale(20, 20);
        setImage(img);
    }

    public void act()
    {
        if (target != null && target.getWorld() != null)
        {
            moveTowards(target.getX(), target.getY(), speed);
        }
        else
        {
            move(speed);
        }

        // Hit detection
        Zombie z = (Zombie)getOneIntersectingObject(Zombie.class);
        if (z != null)
        {
            z.takeDamage(damage);
            getWorld().removeObject(this);
            return;
        }

        // Removes if projectile goes off screen
        if (isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
    
}
