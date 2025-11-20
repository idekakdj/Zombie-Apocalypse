import greenfoot.*;
import java.util.List;

public class Gun extends Weapon
{
    private int damage;
    private int fireRate;     // frames between shots
    private int shootCooldown;
    private Survivors owner;

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

        // Follow owner
        setLocation(owner.getX() + 20, owner.getY());

        // Cooldown check
        if (shootCooldown > 0)
        {
            shootCooldown--;
            return;
        }

        // Look for zombies in a radius
        List<Regular> zombies = getObjectsInRange(250, Regular.class);

        // If zombies are near, fire straight
        if (!zombies.isEmpty())
        {
            shootStraight();
            shootCooldown = fireRate;
        }
    }

    private void shootStraight()
    {
        Projectile p = new Projectile(damage);

        // Spawn in front of gun (adjust as needed)
        getWorld().addObject(p, getX() + 20, getY());

        // No targeting needed ― projectile manages its own movement
    }
    
    public void attack()
    {
        
    }
    
    public void playSound()
    {
        
    }
}
