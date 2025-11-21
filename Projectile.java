import greenfoot.*;
/*
 * @author Jayden assisted by Paul
 */
public class Projectile extends Actor
{
    private int speed = 10;    // straight-line speed
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
        // Move straight right (change to move(…) if you want direction)
        setLocation(getX() + speed, getY());
        turnTowards(target.getX(), target.getY());
        move(speed);
        Regular z = (Regular)getOneIntersectingObject(Regular.class);
        if (z != null)
        {
            z.takeDamage(damage);
            return;
        }

        if (isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}
