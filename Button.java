import greenfoot.*;
/*
 * Author Paul assisted by Claude
 * 
 * Takes in paramaters to fully customize the button
 * text is the button text, height and width are the button dimensions,
 * color is the button color, border width determines the border (subtracted
 * from the main dimensions not added), borderColor will fill the border,
 * fontSize and fontColor adjust the text on the button, id is an identification
 * that can be called elsewhere to trigger an event and toggleable determines
 * if you want the button to have a checkmark when clicked or not (true to have 
 * this feature and false to not) used to show selections from the user.
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
    private boolean isSelected = false;
    private boolean toggleable;
    private GreenfootImage normalImage;
    private GreenfootImage selectedImage;
    GreenfootSound click = new GreenfootSound("mouseclick.mp3");
    GreenfootImage chiu = new GreenfootImage("chiu.png");
    GreenfootImage jayden = new GreenfootImage("jayden.png");
    GreenfootImage paul = new GreenfootImage("chapman.png");
    public Button(String text, int height, int width, Color color, int borderWidth, Color borderColor, int fontSize, Color fontColor, String id, boolean toggleable){
        this.text = text;
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.buttonID = id;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.toggleable = toggleable;
        
        normalImage = createNormalImage();
        if(toggleable) {
            selectedImage = createSelectedImage();
        }
        setImage(normalImage);
    }
    
    private GreenfootImage createNormalImage() {
        GreenfootImage button = new GreenfootImage(width, height);
        
        if (borderWidth > 0) {
            button.setColor(borderColor);
            button.fillRect(0, 0, width, height);
            
            button.setColor(color);
            button.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        } else {
            button.setColor(color);
            button.fillRect(0, 0, width, height);
        }
        
        Font font = new Font("Arial", true, false, fontSize);
        GreenfootImage textImage = new GreenfootImage(text, fontSize, fontColor, new Color(0, 0, 0, 0));
        
        int x = (width - textImage.getWidth()) / 2;
        int y = (height - textImage.getHeight()) / 2;
        
        button.drawImage(textImage, x, y);
        
        return button;
    }
    
    private GreenfootImage createSelectedImage() {
        GreenfootImage button = new GreenfootImage(width, height);
        
        button.setColor(color);
        button.fillRect(0, 0, width, height);
        
        Font font = new Font("Arial", true, false, fontSize);
        GreenfootImage textImage = new GreenfootImage(text, fontSize, fontColor, new Color(0, 0, 0, 0));
        
        int x = (width - textImage.getWidth()) / 2;
        int y = (height - textImage.getHeight()) / 2;
        
        button.drawImage(textImage, x, y);
        
        drawCheckmark(button);
        
        return button;
    }
    
    private void drawCheckmark(GreenfootImage img) {
        img.setColor(Color.GREEN);
        int checkSize = Math.min(width, height) / 4;
        int checkX = width - checkSize - 10;
        int checkY = 10;
        
        img.fillOval(checkX - 5, checkY - 5, checkSize + 10, checkSize + 10);
        
        img.setColor(Color.WHITE);
        int startX = checkX;
        int startY = checkY + checkSize / 2;
        int midX = checkX + checkSize / 3;
        int midY = checkY + checkSize - 5;
        int endX = checkX + checkSize;
        int endY = checkY;
        
        img.drawLine(startX, startY, midX, midY);
        img.drawLine(midX, midY, endX, endY);
        img.drawLine(startX + 1, startY, midX + 1, midY);
        img.drawLine(midX + 1, midY, endX + 1, endY);
        img.drawLine(startX, startY + 1, midX, midY + 1);
        img.drawLine(midX, midY + 1, endX, endY + 1);
    }
    
    public void act()
    {
        if(Greenfoot.mouseClicked(this)){
            if(toggleable) {
                toggleSelection();
            }
            handleClick();
            click.play();
        }
    }
    
    private void toggleSelection() {
        isSelected = !isSelected;
        if(isSelected) {
            setImage(selectedImage);
        } else {
            setImage(normalImage);
        }
    }
    
    public String getButtonID(){
        return buttonID;
    }
    
    public boolean isSelected(){
        return isSelected;
    }
    
    public void handleClick(){
        if(buttonID.equals("choose")){
            World world = getWorld();
            getWorld().getBackground().drawImage(chiu, getWorld().getWidth()/3, getWorld().getHeight()/2);
            getWorld().getBackground().drawImage(jayden, getWorld().getWidth()/2, getWorld().getHeight()/2);
            getWorld().getBackground().drawImage(paul, (getWorld().getWidth()/3) * 2, getWorld().getHeight()/2);
            getWorld().removeObject(this);
            return;
        }
        if(buttonID.equals("simulation")){
            Greenfoot.setWorld(new GameWorld());
            return;
        }
        if(buttonID.equals("returntostart")){
            Greenfoot.setWorld(new StartWorld());
            return;
        }
        World currentWorld = getWorld();
        if(currentWorld instanceof ChooseWorld){
            ChooseWorld world = (ChooseWorld) currentWorld;
            if (buttonID.equals("survivorone")){
                world.updateBoolean(1, isSelected);
            } else if (buttonID.equals("survivortwo")){
                world.updateBoolean(2, isSelected);
            } else if (buttonID.equals("survivorthree")){
                world.updateBoolean(3, isSelected);
            } else if (buttonID.equals("gun")){
                world.updateBoolean(4, isSelected);
            } else if (buttonID.equals("melee")){
                world.updateBoolean(5, isSelected);
            } else if (buttonID.equals("bandages")){
                world.updateBoolean(6, isSelected);
            } else if (buttonID.equals("armor")){
                world.updateBoolean(7, isSelected);
            } else if (buttonID.equals("wall")){
                world.updateBoolean(8, isSelected);
            }
        }
    }
}