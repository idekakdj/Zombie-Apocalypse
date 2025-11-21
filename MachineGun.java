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
    private int cooldown = 1;
    private int reloadTimer = 60;
    Survivors owner;
    double closestDistance;
    Zombie closest;
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
        
        List<Zombie> zombies = getObjectsInRange(300, Zombie.class);
        if(!zombies.isEmpty()){
            for(Zombie zombie : zombies){
                double distance = Math.hypot(zombie.getX() - this.getX(),zombie.getY() - this.getY());
                
                if (distance < closestDistance){
                    closestDistance = distance;
                    closest = zombie;
                }
            }
            attack();
        }
        
    }
    public void attack(){
        Projectile p = new Projectile(damage,closest);
        getWorld().addObject(p, getX() + 20, getY());
        
    }
}
