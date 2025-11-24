import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Upgrade to Bat triggers when user hits a certain score.
 * 
 * @author Paul
 */
public class Sword extends Melee
{
    private int range;

    private Survivors owner;     
    private int attackCooldown;   // cooldown timer
    GreenfootImage sword = new GreenfootImage("sword.png");
    public Sword(int damage, int coolDown, int range, Survivors owner)
    {
        
        super(damage, coolDown, range);
        this.owner = owner;
        this.range = range;
        this.attackCooldown = 0;
        attackCooldown = 0;
        setImage(sword);
        
    }
    public void act(){
        followOwner();
        if (attackCooldown > 0) {
            attackCooldown--;
        } 
        else {
            attackNearbyZombies();
            attackCooldown = coolDown;  // reset cooldown
        }
    }
    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX() + 20, owner.getY());
        }
    }
    private void attackNearbyZombies()
    {
        List<Zombie> zombies = getObjectsInRange(range, Zombie.class);

        if (!zombies.isEmpty()) {
            // Deal damage
            for (Zombie z : zombies) {
                z.takeDamage(damage);
                if (z.isDead()) z.killZombie();
            }

        }
    }
}
