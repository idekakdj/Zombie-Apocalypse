import greenfoot.*;
/**
 * Author Jayden  
 * 
 * speed var is the number of pixels moved in the direction facing
 * damage is the amount of damage taken from the zombie that the projectile hits (int)
 * target zombie that the projectile is moving towards
 */
public class Projectile extends Actor
{
    private int speed = 7;
    private int damage;
    private Zombie target;
/**
     * @param damage is the damage an individual projectile will do against a zombie
     * @param target is the zombie that is being targeted by the projectile
     * 
     * 
     */
    public Projectile(int damage, Zombie target)
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
            turnTowards(target.getX(), target.getY());
            move(speed);
        } else {
            if(getWorld() != null){
                getWorld().removeObject(this);
            }
            return;
        }
        

        // checks if there is an intersecting zombie
        Zombie z = (Zombie)getOneIntersectingObject(Zombie.class);
        if (z != null)
        {
            z.takeDamage(damage);
            getWorld().removeObject(this);
            return;
        }

        // Remove projectile if off screen
        if (isAtEdge())
        {
            if(getWorld() != null){
                getWorld().removeObject(this);
            }
            return;
        }
    }
}
