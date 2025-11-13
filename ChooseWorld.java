import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ChooseWorld here.
 * 
 * @author Paul 
 * 
 * The world to choose your survivor and items for the simulation.
 * You can pick on survivor and three items.
 */
public class ChooseWorld extends World
{

    /**
     * Constructor for objects of class ChooseWorld.
     * 
     */
    private int numSurvivors;
    public boolean MELEE;
    public boolean GUN;
    public boolean SHIELD;
    public boolean BANDAGES;
    public boolean SURVIVOR_ONE;
    public boolean SURVIVOR_TWO;
    public boolean SURVIVOR_THREE;
    public boolean WALL;
    public ChooseWorld()
    {    
        super(1024, 700, 1); 
        setBackground("chooseworld.png");
        prepareButtons();
    }
    //Adds all the buttons for choosing survivors and items as well at the button to proceed to simulation
    private void prepareButtons(){
        Button simulation = new Button ("Simulation", 80, 200, Color.GRAY, 5, Color.BLACK, 40, Color.WHITE,"simulation",false);
        addObject(simulation,getWidth()/2,650);
        Button survivorone = new Button("Choose Paul", 50,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"survivorone",true);
        addObject(survivorone, getWidth()/3 - 25, getHeight()/3);
        Button survivortwo = new Button("Choose Jayden", 50,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"survivortwo",true);
        addObject(survivortwo, getWidth()/2 + 15, getHeight()/3);
        Button survivorthree = new Button("Choose Cayden", 50,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"survivorthree",true);
        addObject(survivorthree, ((getWidth()/3) * 2) + 60, getHeight()/3);
        Button gun = new Button("Choose Gun", 50 ,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"gun",true);
        addObject(gun, (getWidth()/2) - 120, (getHeight()/2) + 40); 
        Button melee = new Button("Choose Bat", 50,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"melee",true);
        addObject(melee, (getWidth()/2) + 140, (getHeight()/2) + 40);
        Button bandages = new Button("Choose Bandages", 50,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"bandages",true);
        addObject(bandages, getWidth()/3 - 45, 550);
        Button shield = new Button("Choose Shield", 50 ,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"shield",true);
        addObject(shield, getWidth()/2 , 550);
        Button wall = new Button("Choose Wall", 50,150, Color.GRAY, 5, Color.BLACK, 20, Color.WHITE,"wall",true);
        addObject(wall, ((getWidth()/3) * 2) + 45, 550 );
    }
    /*Buttons are number 1-9 on the choose world starting top left with Survivor One and going row by row like this.
     * 1, 2, 3
     * 4, 5, 6
     * 7, 8, 9
     */
    public void updateBoolean(int buttonNum, boolean value){
        if(buttonNum == 1){
            SURVIVOR_ONE = value;
        } else if (buttonNum == 2){
            SURVIVOR_TWO = value;
        } else if (buttonNum == 3){
            SURVIVOR_THREE = value;
        } else if (buttonNum == 4){
            GUN = value;
        } else if (buttonNum == 5){
            MELEE = value;
        } else if(buttonNum == 6){
            BANDAGES = value;
        } else if(buttonNum == 7){
            SHIELD = value;
        } else if (buttonNum == 8){
            WALL = value;
        }
    }
}
