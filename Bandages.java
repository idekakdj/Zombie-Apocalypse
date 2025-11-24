import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Paul
 * 
 * 
 * Heals the user by 25hp when used. Has two uses and can only be used
 * once at a time during the day.
 * 
 */
public class Bandages extends Actor
{
    private Survivors owner;
    private int usageTime = 25;
    private int useCounter;
    GreenfootImage bandages = new GreenfootImage("bandages.png");
    private final int offsetY = -40;
    private boolean daytime;
    private boolean nighttime;
    private int ownerStartHP;
    private boolean used = false;
    public Bandages(Survivors owner){
        this.owner = owner;
        setImage(bandages);
        useCounter = 2;
        if (owner instanceof SurvivorOne) {
            ownerStartHP = ((SurvivorOne) owner).getMaxHP();
        } else if (owner instanceof SurvivorTwo) {
            ownerStartHP = ((SurvivorTwo) owner).getMaxHP();
        } else if (owner instanceof SurvivorThree) {
            ownerStartHP = ((SurvivorThree) owner).getMaxHP();
        }
    }
    public void act()
    {
        updateDaytimeStatus();
        World world = getWorld();
        followOwner();
        
        if(daytime && owner.getHP() < ownerStartHP && !used){
            show();
            usageTime--;
            if(usageTime == 0 && useCounter > 0 ){
                owner.heal();
                if (owner instanceof SurvivorOne) {
                    ((SurvivorOne) owner).getHPBar().update(owner.getHP());
                } else if (owner instanceof SurvivorTwo) {
                    ((SurvivorTwo) owner).getHPBar().update(owner.getHP());
                } else if (owner instanceof SurvivorThree) {
                    ((SurvivorThree) owner).getHPBar().update(owner.getHP());
                }
                useCounter--;
                usageTime = 50; 
                used = true;
            } 
        } else {
            hide();
        }
        if(nighttime){
            used = false;
        }
    }
    private void updateDaytimeStatus() {
        World world = getWorld();
        if (world != null && world instanceof GameWorld) {
            GameWorld gameWorld = (GameWorld) world;
            this.daytime = gameWorld.daytime;
            this.nighttime = gameWorld.nighttime;
        }
    }
    public void hide(){
        setImage(new GreenfootImage(1,1));
    }
    public void show (){
        setImage(bandages);
    }
    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX(), owner.getY() - offsetY);
        }
    }
}
