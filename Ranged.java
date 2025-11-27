import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Super class for gun and machine gun
 * 
 * @authors Paul and Jayden
 * 
 */
public abstract class Ranged extends Actor
{
    protected int damage;
    protected int fireRate;
    protected int cooldown = 0;
    Survivors owner;
    private ArrayList<GreenfootSound> soundQueue = new ArrayList<GreenfootSound>();
    private int soundCooldown = 0;
    private final int SOUND_DELAY = 5;
    
    /**
     * superclass constructor that works for both gun subclasses
     * @param damage, damage to deal
     * @param fireRate, how fast it shoots, faster for machine gun
     * @param owner, tracks owner to follow
     */
    public Ranged(int damage, int fireRate, Survivors owner) {
        this.damage = damage;
        this.fireRate = fireRate;
        this.owner = owner;
        this.cooldown = 0;
    }
    
    /**
     * follows owner and finds closest zombie to target and then shoot at
     */
    public void act()
    {
        if (owner == null || owner.getWorld() == null) return;
        setLocation(owner.getX() + 35, owner.getY());
        
        soundFix();
        
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        
        List<Zombie> zombies = getObjectsInRange(300, Zombie.class);
        if(!zombies.isEmpty()){
            Zombie closest = findClosestZombie(zombies);
            
            if (closest != null && closest.getWorld() != null) {
                attack(closest);
                cooldown = fireRate;
            }
        }
    }
    
    private void soundFix() {
        if (soundCooldown > 0) {
            soundCooldown--;
            return;
        }
        
        if (!soundQueue.isEmpty()) {
            GreenfootSound sound = soundQueue.remove(0);
            sound.play();
            soundCooldown = SOUND_DELAY;
        }
    }
    
    private Zombie findClosestZombie(List<Zombie> zombies) {
        Zombie closest = null;
        double closestDistance = Double.MAX_VALUE;
        
        for(Zombie zombie : zombies){
            if (zombie.getWorld() == null) continue;
            
            double distance = Math.hypot(
                zombie.getX() - getX(),
                zombie.getY() - getY()
            );
            
            if (distance < closestDistance){
                closestDistance = distance;
                closest = zombie;
            }
        }
        
        return closest;
    }
    
    /**
     * sends projectile to zombie targets
     * @param target, nearest zombie that is found in the method above
     */
    public void attack(Zombie target){
        if (target != null && target.getWorld() != null) {
            Projectile p = new Projectile(damage, target);
            getWorld().addObject(p, getX() + 20, getY());
            
            GreenfootSound shot = new GreenfootSound("shot.mp3");
            soundQueue.add(shot);
        }
    }
}