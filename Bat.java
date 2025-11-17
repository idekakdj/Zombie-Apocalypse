import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Very simple Bat weapon.
 * Automatically damages any nearby zombies at a set cooldown rate.
 * No user input, no animation — just simple logic.
 */
public class Bat extends Melee
{
    private Survivors owner;      // Survivor holding this bat
    private int attackCooldown;   // cooldown timer

    public Bat(int damage, int coolDown, int range, Survivors owner)
    {
        super(damage, coolDown, range);
        this.owner = owner;
        attackCooldown = 0;
    }

    public void act()
    {
        followOwner();
        if (attackCooldown > 0) attackCooldown--;
        else {
            attackNearbyZombies();
            attackCooldown = coolDown;  // reset timer
        }
    }

    /** Keeps the bat beside the survivor */
    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX() + 20, owner.getY());  // small offset to side
        }
    }

    /** Damages zombies that touch or are close to the bat */
    private void attackNearbyZombies()
    {
        // Find all zombies within the bat's range
        List<Zombie> zombies = getObjectsInRange(range, Zombie.class);

        for (Zombie z : zombies) {
            z.takeDamage(damage);
            if (z.isDead()) {
                z.killZombie();
            }
        }
    }
    
    public void playSound()
    {
        
    }
    
    public void attack()
    {
        
    }
}

