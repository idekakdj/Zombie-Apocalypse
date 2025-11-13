import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * @author Paul assisted by Claude
 * 
 * Takes in paramaters to customize your button almost completely
 * You can input the text you want for this, the height and width of the button (borders will subtract from 
 * total area instead of adding more area), font size can be customized (make sure its not larger than the button)
 * also takes a paramater for a button id to reference later and store values when specific buttons get clicked.
 * 
 */


public class Button extends Actor
{
    private String text;
    private String buttonID;
    private int width, height;
    private Color color; 
    private Color borderColor;
    private int borderWidth;
    private int fontSize;
    private Color fontColor;
    GreenfootSound click = new GreenfootSound("mouseclick.mp3");
    public Button(String text, int height, int width, Color color, int borderWidth, Color borderColor, int fontSize, Color fontColor,String id){
        this.text = text;
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.buttonID = id;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        createButtonImage();
    }
    
      private void createButtonImage() {
        GreenfootImage button = new GreenfootImage(width, height);
        
        button.setColor(borderColor);
        button.fillRect(0, 0, width, height);
        
        button.setColor(color);
        button.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        
        Font font = new Font("Arial", true, false, fontSize);
        GreenfootImage textImage = new GreenfootImage(text, fontSize, fontColor, new Color(0, 0, 0, 0));
        
        int x = (width - textImage.getWidth()) / 2;
        int y = (height - textImage.getHeight()) / 2;
        
        button.drawImage(textImage, x, y);
        
        setImage(button);
    }

    public void act()
    {
        if(Greenfoot.mouseClicked(this)){
            handleClick();
            click.play();
        }
    }
    public String getButtonID(){
        return buttonID;
    }
    public void handleClick(){
        if(buttonID.equals("choose")){
            Greenfoot.setWorld(new ChooseWorld());
            return;
        }
        if(buttonID.equals("simulation")){
            Greenfoot.setWorld(new GameWorld());
            return;
        }
        World currentWorld = getWorld();
        if(currentWorld instanceof ChooseWorld){
            ChooseWorld world = (ChooseWorld) currentWorld;
            if (buttonID.equals("survivorone")){
                world.updateBoolean(1);
            } else if (buttonID.equals("survivortwo")){
                world.updateBoolean(2);
            } else if (buttonID.equals("survivorthree")){
                world.updateBoolean(3);
            } else if (buttonID.equals("gun")){
                world.updateBoolean(4);
            } else if (buttonID.equals("melee")){
                world.updateBoolean(5);
            } else if (buttonID.equals("bandages")){
                world.updateBoolean(6);
            } else if (buttonID.equals("shield")){
                world.updateBoolean(7);
            } else if (buttonID.equals("wall")){
                world.updateBoolean(8);
            }
        }
    }   
}
