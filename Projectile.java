import greenfoot.*;

public class Projectile extends Actor
{
    private int speed = 7;    // straight-line speed
    private int damage;

    public Projectile(int damage)
    {
        this.damage = damage;

        GreenfootImage img = new GreenfootImage("bullet.png");
        img.scale(10, 10);
        setImage(img);
    }

    public void act()
    {
        // Move straight right (change to move(…) if you want direction)
        setLocation(getX() + speed, getY());

        Regular z = (Regular)getOneIntersectingObject(Regular.class);
        if (z != null)
        {
            z.takeDamage(damage);
            getWorld().removeObject(this);
            return;
        }

        if (isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}
