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
    GreenfootSound heal = new GreenfootSound("heal.mp3");
    /**
     * Spawns the bandages, sets the use counter to 2 and checks which survivor was selected because they have different hp values.
     * @param owner, keeps track of the owner to follow the survivor
     */
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
    /**
     * Checks for updated daytime status first because it only heals during the day, shows during the day to heal
     * otherwise has a method to turn the image invisible while still following the survivor.
     */
    public void act()
    {
        updateDaytimeStatus();
        World world = getWorld();
        followOwner();
       
        if(daytime && owner.getHP() < ownerStartHP && !used && useCounter > 0){
            show();
            usageTime--;
            if(usageTime == 0 && useCounter > 0 ){
                owner.heal();
                heal.play();
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
    /**
     * hides the image by turning it invisible.
     */
    public void hide(){
        setImage(new GreenfootImage(1,1));
    }
    
    /**
     * Shows the original image when being used.
     */
    public void show (){
        setImage(bandages);
    }
    //follows survivor
    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX(), owner.getY() - offsetY);
        }
    }
}