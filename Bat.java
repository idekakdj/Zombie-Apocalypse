import greenfoot.*;
import java.util.List;
/**
 *  Bat class, spawns bat that spins when attacking and uses slash animation
 *  @author Jayden
 *  whack01 by freesound_community on pixabay
 */
public class Bat extends Melee
{
  

    private int spinTimer = 0;   // how long to spin
    private int spinSpeed = 25;  // degrees per frame
    private int range;

    private Survivors owner;      // Survivor holding this bat
    private int attackCooldown;   // cooldown timer
    GreenfootImage bat = new GreenfootImage("baseballbat.png");
    private GreenfootSound whack = new GreenfootSound ("whack.mp3");
    /**
     * Bat constructor
     * @param damage, damage bat deals
     * @param coolDown, cooldown before attacking
     * @param range, range for attacking
     * @param owner, tracks the survivor owner to follow it
     */
    public Bat(int damage, int coolDown, int range, Survivors owner)
    {
        
        super(damage, coolDown, range);
        this.owner = owner;
        this.range = range;
        this.attackCooldown = 0;
        attackCooldown = 0;
        setImage(bat);
        
    }
    /**
     * follows survivor owner, spins when attacking and detects nearby zombies to attack
     */
    public void act()
    {
        followOwner();

        // Handle spin animation every frame
        if (spinTimer > 0) {
            spinTimer--;
            turn(spinSpeed);
        }

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
            BatSlash slash = new BatSlash();
            getWorld().addObject(slash, getX() + 30, getY());
            // Deal damage
            for (Zombie z : zombies) {
                z.takeDamage(damage);
                playSound();
                if (z.isDead()) z.killZombie();
            }

            // Start spin animation
            spinTimer = 10;   // spin for 10 frames (adjust if you want)
        }
    }
    /**
     * plays sound when attacking
     */
    public void playSound()
    {
        Greenfoot.playSound("whack.mp3");  

    }
}

