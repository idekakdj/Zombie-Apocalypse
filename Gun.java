import greenfoot.*;
import java.util.List;

public class Gun extends Weapon
{
    private int damage;
    private int fireRate;
    private int shootCooldown;
    private Survivors owner;
    private GreenfootSound shot = new GreenfootSound ("shot.mp3");

    public Gun(int damage, int fireRate, Survivors owner)
    {
        this.damage = damage;
        this.fireRate = fireRate;
        this.owner = owner;
        this.shootCooldown = 0;

        setImage("gun.png");
    }

    public void act()
    {
        if (owner == null || owner.getWorld() == null) return;

        // Position attached to survivor
        setLocation(owner.getX() + 20, owner.getY());

        if (shootCooldown > 0)
        {
            shootCooldown--;
            return;
        }

        Zombie target = getClosestZombie();

        if (target != null)
        {
            shootAt(target);
            shootCooldown = fireRate;
        }
    }

    /** Returns the closest zombie in range, or null */
    private Zombie getClosestZombie()
    {
        List<Zombie> zombies = getObjectsInRange(250, Zombie.class);

        if (zombies.isEmpty()) return null;

        Zombie closest = zombies.get(0);
        double bestDistance = distanceTo(closest);

        for (Zombie z : zombies)
        {
            double d = distanceTo(z);
            if (d < bestDistance)
            {
                bestDistance = d;
                closest = z;
            }
        }

        return closest;
    }

    private double distanceTo(Actor a)
    {
        int dx = a.getX() - getX();
        int dy = a.getY() - getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    /** Fire a projectile that auto-aims at the chosen target */
    private void shootAt(Zombie target)
    {
        Projectile p = new Projectile(damage, target);
        getWorld().addObject(p, getX() + 20, getY());
        playSound();
    }
    
    public void playSound()
    {
              Greenfoot.playSound("shot.mp3");  
    }
    
    public void attack()
    {
        
    }
}

