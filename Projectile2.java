import greenfoot.*;

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
        // If target exists and is still in world → move toward it
        if (target != null && target.getWorld() != null)
        {
            moveTowards(target.getX(), target.getY(), speed);
        }
        else
        {
            // If target died, move straight ahead in current direction
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

        // Remove if off screen
        if (isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}
