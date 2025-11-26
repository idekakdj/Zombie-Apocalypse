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
    GreenfootSound slash = new GreenfootSound("slash.mp3");
    /**
     * Sword constructor
     * @param damage, damage to deal to zombies
     * @param coolDown, attack cooldown
     * @param range, attack range
     * @param owner, owner to follow
     */
    public Sword(int damage, int coolDown, int range, Survivors owner)
    {
        
        super(damage, coolDown, range);
        this.owner = owner;
        this.range = range;
        this.attackCooldown = 0;
        attackCooldown = 0;
        setImage(sword);
        
    }
    /**
     * follows owner and attacks, uses thick red slash animation
     */
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
            setLocation(owner.getX() + 25, owner.getY());
        }
    }
    private void attackNearbyZombies()
    {
        List<Zombie> zombies = getObjectsInRange(range, Zombie.class);

        if (!zombies.isEmpty()) {
            //Slash animation
            SwordSlash slash = new SwordSlash();
            Greenfoot.playSound("slash.mp3");
            getWorld().addObject(slash, getX() + 30, getY());
            // Deal damage
            for (Zombie z : zombies) {
                z.takeDamage(damage);
                if (z.isDead()) z.killZombie();
            }

        }
    }
}
