import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Is the main game world thats starts by spawning suvivovr in the middle and called zombie constructors to 
 * spawn zombies on edges of the screen and move them towards the survivor.
 * 
 * @author Paul assisted by Cayden 
 */
public class GameWorld extends World
{
    /**
     * Constructor for objects of class MyWorld.
     * Spawns survivor in middle of world and zombies on edges.
     */
    SurvivorBoundary boundary;
    ScoreTracker scoretracker;
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
    private final int DAY_COOLDOWN = 900;  
    private final int NIGHT_COOLDOWN = 1500; 
    public int wavesCounter;
    private int cooldown;
    
    private int actCount;
    private GreenfootSound bgm = new GreenfootSound ("bgm.mp3");
    /**
     * Simulation world where survivor has to survive past 5 waves or die.
     * Takes booleans from ChooseWorld depending on what the user selected.
     * @param s1, survivor one boolean 
     * @param s2, survivor two boolean
     * @param s3, survivor three boolean
     * @param melee, melee weapon boolean
     * @param gun, gun boolean
     * @param shield, shield boolean
     * @param bandages, bandages boolean
     * @param wall, wall boolean
     */
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
        // CRITICAL: Set paint order so HP bars appear on top
        setPaintOrder(ScoreTracker.class, UpgradeProgressBar.class, SuperStatBar.class, Nighttime.class, Bandages.class,Melee.class, Shield.class, Gun.class, MachineGun.class,BatSlash.class, SwordSlash.class, Survivors.class, Zombie.class);
        prepare();
        
        actCount = 0;
        // Initialize to daytime at start
        daytime = true;
        nighttime = false;
        cooldown = DAY_COOLDOWN;
        
        bgm.playLoop();

    }
    /**
     * Cycles through day/night, heals during and repairs wall during day, fights zombeis at night
     */
    public void act() {
        actCount++;
        dayNightCycle();
    }
    //Spawns survivor boundary, score tracker, upgrade progress bar, the selected survivor and sets waves counter to 0 
    private void prepare() {
        //spawn survivor's movement boundary
        boundary = new SurvivorBoundary(this.getWidth()/2,this.getHeight()/2, 400,300 );
        scoretracker = new ScoreTracker(300, 40, Color.BLUE, 3, Color.BLACK);
        addObject(scoretracker,(getWidth()/3) * 2, 30);
        UpgradeProgressBar upgradeBar = new UpgradeProgressBar(300, 50,Color.GREEN,Color.GRAY, 3, Color.BLACK, "Weapon Upgrade",melee,gun);
        addObject(upgradeBar, getWidth()/2, 650);
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
        
        wavesCounter = 0;
    }
    
    //Counter for day and night. Spawns zombie waves at night.
    private void dayNightCycle()
    {
        cooldown--;
        
        if (cooldown <= 0) {
            if (daytime) {
                // Switch to nighttime
                daytime = false;
                nighttime = true;
                cooldown = NIGHT_COOLDOWN;
                
                // Spawn nighttime visual effect
                addObject(new Nighttime(), 512, 400);
                
                // Spawn zombie waves
                waves();  
            } else {
                // Switch to daytime
                nighttime = false;
                daytime = true;
                cooldown = DAY_COOLDOWN;
                
            }
        }
    }
    
    //Progressively harder waves up to 5.
    public void waves(){
        wavesCounter++;
        if (wavesCounter == 1){
            for(int i = 0; i < 5; i++){
                spawnRegular();
            }
            spawnGiant();
        } else if(wavesCounter == 2){
            for(int i = 0; i < 8;i++){
                spawnRegular();
            }
            spawnGiant();
        } else if (wavesCounter == 3 ){
            for(int i = 0;i < 6; i++){
                spawnGiant();
            }
        } else if( wavesCounter == 4){
            for (int i = 0; i < 12 ; i++){
                spawnPenguin();
                spawnRegular();
            }
            spawnGiant();
        } else if ( wavesCounter == 5){
            for (int i = 0; i < 5; i++){
                spawnGiant();
                spawnRegular();
                spawnPenguin();
            }
            spawnBoss();
        } else if (wavesCounter == 6){
            Greenfoot.setWorld(new WinScreen(s1,s2,s3));
        }
    }
    /**
     * Helps survivor determine valid positions to move.
     * @param x, x position in the boundary
     * @param y, y position in the boundary
     */
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
    
    /**
     * Background music setup
     */ 

    public void started()
    {
        if (bgm != null) {
            bgm.setVolume(70);   // optional volume control
            bgm.playLoop();     // loops forever
        }
    }
    /**
     * Stops background music
     */
    public void stopped()
    {
        if (bgm != null) {
            bgm.pause();
        }
    }

    private void drawWalls() {
        int width = 400;
        int height = 300;
        
        // Use world dimensions, not image dimensions
        int worldWidth = getWidth();
        int worldHeight = getHeight();
        
        // Calculate center position
        int centerX = worldWidth / 2;
        int centerY = worldHeight / 2;
        
        // Calculate rectangle boundaries
        int left = centerX - width / 2;
        int right = centerX + width / 2;
        int top = centerY - height / 2;
        int bottom = centerY + height / 2;
        
        // Get wall image size to determine spacing
        Wall tempWall = new Wall();
        int wallSize = tempWall.getImage().getWidth();
        
        // Use smaller spacing to ensure no gaps
        int spacing = wallSize / 2;  // Place walls much closer together
        
        // Draw top wall
        for (int x = left; x <= right; x += spacing) {
            addObject(new Wall(), x, top);
        }
        
        // Draw bottom wall
        for (int x = left; x <= right; x += spacing) {
            addObject(new Wall(), x, bottom);
        }
        
        // Draw left wall - include full height
        for (int y = top; y <= bottom; y += spacing) {
            addObject(new Wall(), left, y);
        }
        
        // Draw right wall - include full height
        for (int y = top; y <= bottom; y += spacing) {
            addObject(new Wall(), right, y);
        }
    }
}