import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Upgrade to gun trigger when certain score is reached if gun is selected.
 * 
 * @author Paul 
 */
public class MachineGun extends Ranged
{
    
    private int reloadTimer = 60;
    Survivors owner;
    GreenfootImage machinegun = new GreenfootImage("machinegun.png");
    
    public MachineGun(int damage, int fireRate, Survivors owner){
        super(damage,fireRate, owner);
        setImage(machinegun);
    }
    
    public void act(){
        super.act();
    }
}