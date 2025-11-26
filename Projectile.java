import greenfoot.*;
/**
 * Projectile for gun and machine gun
 * 
 * @autho Jayden assisted by Paul
 */
public class Projectile extends Actor
{
    private int speed = 7;
    private int damage;
    private Zombie target;
    /**
     * constructor for projectile
     * @param damage, damage to deal 
     * @param target, nearest zombie that it targets first
     */
    public Projectile(int damage, Zombie target)
    {
        this.damage = damage;
        this.target = target;

        GreenfootImage img = new GreenfootImage("bullet.png");
        img.scale(20, 20);
        setImage(img);
    }
    /**
     * targets nearest zombie and moves towards it, deals damage when touching zombies
     */
    public void act()
    {
        // If target exists and is still in world → move toward it
        if (target != null && target.getWorld() != null)
        {
            turnTowards(target.getX(), target.getY());
            move(speed);
        } else {
            if(getWorld() != null){
                getWorld().removeObject(this);
            }
            return;
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
            if(getWorld() != null){
                getWorld().removeObject(this);
            }
            return;
        }
    }
}
