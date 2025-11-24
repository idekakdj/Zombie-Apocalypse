import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Upgrade to gun trigger when 3000 score is reached if gun is selected.
 * 
 * @author Paul 
 */
public class MachineGun extends Weapon
{
    private int damage;
    private int fireRate;
    private int cooldown = 0;
    private int reloadTimer = 60;
    Survivors owner;
    GreenfootImage machinegun = new GreenfootImage("machinegun.png");
    
    public MachineGun(int damage, int fireRate, Survivors owner){
        this.damage = damage;
        this.fireRate = fireRate;
        this.owner = owner;
        setImage(machinegun);
    }
    
    public void act()
    {
        if (owner == null || owner.getWorld() == null) return;
        setLocation(owner.getX() + 20, owner.getY());
        
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
            Projectile2 p = new Projectile2(damage, target);
            getWorld().addObject(p, getX() + 20, getY());
        }
    }
}