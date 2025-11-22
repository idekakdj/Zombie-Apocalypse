import greenfoot.*;

public class Projectile extends Actor
{
    private int speed = 7;
    private int damage;

    public Projectile(int damage, Zombie target)
    {
        this.damage = damage;

        GreenfootImage img = new GreenfootImage("bullet.png");
        img.scale(20, 20);
        setImage(img);

        // Aim at target if available
        if (target != null) {
            turnTowards(target.getX(), target.getY());
        }
    }

    public void act()
    {
        move(speed);

        Zombie z = (Zombie)getOneIntersectingObject(Zombie.class);
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
