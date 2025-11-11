import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * 
 * 
 * @author Paul 
 */
public class SurvivorOne extends Survivors
{
    private int hp;
    private int speed = 4;
    GreenfootImage p1 = new GreenfootImage("survivorone.png");
    private SuperStatBar hpBar;
    public SurvivorOne(){
        hp = super.startHP;
        hpBar = new SuperStatBar (hp, hp, this, 100, 50, 0, Color.ORANGE, Color.BLUE, true, Color.YELLOW, 1);
        setImage(p1);
    }
    @Override
    protected void addedToWorld(World w) {
       w.addObject(hpBar, getX(), getY());
        hpBar.update(hp);
    }
    public void act()
    {
        super.act();
        
        // Keep HP bar above Survivor as they move
        if (hpBar.getWorld() != null) {
            hpBar.setLocation(getX(), getY() - 50);
        }

        List<Zombie> nearbyZombies = this.getObjectsInRange(super.DETECTION, Zombie.class);
        for(Zombie z : nearbyZombies){
            int x = z.getX();
            int y = z.getY();
            super.moveAway(getAngleTowards(z),speed);
        }
    }
    public void takeDamage(int damage){
        hp = hp - damage;
    }
    public SuperStatBar getHPBar() {
    return hpBar;
}

}
