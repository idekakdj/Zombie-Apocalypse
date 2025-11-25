import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Super class for gun and machine gun
 * 
 * @author Paul assisted by Jayden.
 * 
 */
public abstract class Ranged extends Actor
{
    protected int damage;
    protected int fireRate;
    protected int cooldown = 0;
    Survivors owner;
    private GreenfootSound shot = new GreenfootSound ("shot.mp3");
    public Ranged(int damage, int fireRate, Survivors owner) {
        this.damage = damage;
        this.fireRate = fireRate;
        this.owner = owner;
        this.cooldown = 0;
    }
    
    public void act()
    {
        if (owner == null || owner.getWorld() == null) return;
        setLocation(owner.getX() + 35, owner.getY());
        
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
    
    public void attack(Zombie target){
        if (target != null && target.getWorld() != null) {
            Projectile p = new Projectile(damage, target);
            getWorld().addObject(p, getX() + 20, getY());
            shot.play();
        }
    }
    
}
