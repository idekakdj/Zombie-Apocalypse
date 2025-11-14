import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Is the main game world thats starts by spawning suvivovr in the middle and called zombie constructors to 
 * spawn zombies on edges of the screen and move them towards the survivor.
 * 
 * @author Paul and Cayden 
 */
public class GameWorld extends World
{
    /**
     * Constructor for objects of class MyWorld.
     * Spawns survivor in middle of world and zombies on edges.
     */
    SurvivorBoundary boundary;
    GreenfootImage world = new GreenfootImage("gameworld.png");
    public boolean s1 = false;
    public boolean s2 = false;
    public boolean s3 = false;
    public boolean melee = false;
    public boolean gun = false;
    public boolean shield = false;
    public boolean bandages = false;
    public boolean wall = false;
    public boolean daytime;
    public boolean nighttime;
    private final int DAY_COOLDOWN = 600;
    private final int NIGHT_COOLDOWN = 1200;
    public GameWorld(boolean s1,boolean s2, boolean s3, boolean melee, boolean gun, boolean shield, boolean bandages, boolean wall)
    {    
        // Create a new world with 1024x700 cells with a cell size of 1x1 pixels.
        super(1024, 700, 1);
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.melee = melee;
        this.gun = gun;
        this.shield = shield;
        this.bandages = bandages;
        this.wall = wall;
        setBackground(world);
        prepare();
        
    }
    
    private void prepare() {
        
        //spawn survivor's movement boundary
        boundary = new SurvivorBoundary(this.getWidth()/2,this.getHeight()/2, 400,300 );
        //spawn survivors in middle
        if(s1){
            SurvivorOne s1 = new SurvivorOne();
            addObject(s1, getWidth()/2, getHeight()/2);
        } else if(s2){
            SurvivorTwo s2 = new SurvivorTwo();
            addObject(s2, getWidth()/2, getHeight()/2);
        } else if( s3){
            SurvivorThree s3 = new SurvivorThree();
            addObject(s3, getWidth()/2, getHeight()/2);
        }
        spawnRegular();
        spawnGiant();
        spawnPenguin();
        spawnBoss();
    }
    
    public boolean isValidPosition(int x, int y){
        if (boundary == null){
            return true;
        }
        return boundary.contains(x,y);
    }
    
    private void spawnRegular() {
        Regular zombie = new Regular();
        
        // Randomly choose which edge (0=top, 1=right, 2=bottom, 3=left)
        int edge = Greenfoot.getRandomNumber(4);
        int x, y;
        
        if (edge == 0) {  
            x = Greenfoot.getRandomNumber(getWidth());
            y = 0;
        } else if (edge == 1) {
            x = getWidth() - 1;
            y = Greenfoot.getRandomNumber(getHeight());
        } else if (edge == 2) { 
            x = Greenfoot.getRandomNumber(getWidth());
            y = getHeight() - 1;
        } else { 
            x = 0;
            y = Greenfoot.getRandomNumber(getHeight());
        }
        
        addObject(zombie, x, y);
    }
    
    private void spawnGiant() {
        Giant zombie = new Giant();
        
        // Randomly choose which edge (0=top, 1=right, 2=bottom, 3=left)
        int edge = Greenfoot.getRandomNumber(4);
        int x, y;
        
        if (edge == 0) {  
            x = Greenfoot.getRandomNumber(getWidth());
            y = 0;
        } else if (edge == 1) {
            x = getWidth() - 1;
            y = Greenfoot.getRandomNumber(getHeight());
        } else if (edge == 2) { 
            x = Greenfoot.getRandomNumber(getWidth());
            y = getHeight() - 1;
        } else { 
            x = 0;
            y = Greenfoot.getRandomNumber(getHeight());
        }
        
        addObject(zombie, x, y);
    }
    
    private void spawnPenguin() {
        Penguin zombie = new Penguin();
        
        // Randomly choose which edge (0=top, 1=right, 2=bottom, 3=left)
        int edge = Greenfoot.getRandomNumber(4);
        int x, y;
        
        if (edge == 0) {  
            x = Greenfoot.getRandomNumber(getWidth());
            y = 0;
        } else if (edge == 1) {
            x = getWidth() - 1;
            y = Greenfoot.getRandomNumber(getHeight());
        } else if (edge == 2) { 
            x = Greenfoot.getRandomNumber(getWidth());
            y = getHeight() - 1;
        } else { 
            x = 0;
            y = Greenfoot.getRandomNumber(getHeight());
        }
        
        addObject(zombie, x, y);
    }
    
    private void spawnBoss() {
        Boss zombie = new Boss();
        
        // Randomly choose which edge (0=top, 1=right, 2=bottom, 3=left)
        int edge = Greenfoot.getRandomNumber(4);
        int x, y;
        
        if (edge == 0) {  
            x = Greenfoot.getRandomNumber(getWidth());
            y = 0;
        } else if (edge == 1) {
            x = getWidth() - 1;
            y = Greenfoot.getRandomNumber(getHeight());
        } else if (edge == 2) { 
            x = Greenfoot.getRandomNumber(getWidth());
            y = getHeight() - 1;
        } else { 
            x = 0;
            y = Greenfoot.getRandomNumber(getHeight());
        }
        
        addObject(zombie, x, y);
    }
}
