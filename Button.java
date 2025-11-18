import greenfoot.*;
/*
 * Author Paul assisted by Claude (toggleable feature and drawing the checkmark)
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
 * 
 * To use for anything other than the original Zombie Apocolypse game alter the 
 * handleClick() method, delete GreenfootImages and add your own click sound.
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
    GreenfootImage left = new GreenfootImage("leftbubble.png");
    GreenfootImage right = new GreenfootImage("rightbubble.png");
    GreenfootImage middle = new GreenfootImage ("middlebubble.png");
    private int clickCounter = 0;
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
    public void drawSurvivors(){
        World world = getWorld();
        chiu.scale(200,200);
        paul.scale(200,200);
        jayden.scale(200,200);
        world.getBackground().drawImage(chiu, (getWorld().getWidth()/3) - 100, 500);
        world.getBackground().drawImage(jayden, (getWorld().getWidth()/2) - 100, 500);
        world.getBackground().drawImage(paul, ((getWorld().getWidth()/3) * 2) - 100, 500);
    }
    public void handleClick(){
        //Starts the dialogue for the background story to this simulation and the instructions on choosing items
        if(buttonID.equals("start")){
            World world = getWorld();
            drawSurvivors();
            Button continueButton = new Button ("Continue", 80, 200, Color.GRAY, 5, Color.BLACK, 40, Color.WHITE,"continue",false);
            world.addObject(continueButton, 920, 600);
            Button backButton = new Button("Back", 80,200, Color.GRAY, 5, Color.BLACK, 40, Color.WHITE,"back",false);
            world.addObject(backButton, 104, 600);
            left.scale(700,700);
            world.getBackground().drawImage(left, 320 , -100);
            world.getBackground().setColor(Color.BLACK);
            world.getBackground().setFont(new Font("Arial", 24));
            world.getBackground().drawString("In this reality after World War Three", 530, 180);
            world.getBackground().drawString("the nuclear radiation caused two thirds", 530, 230);
            world.getBackground().drawString("of humanity to turn into zombies,", 530, 280);
            world.getBackground().drawString("leaving the rest of us to survive...", 530, 330);
            world.removeObject(this);
            return;
        }
        // The button in ChooseWorld that leads to the GameWorld
        if(buttonID.equals("simulation")){
            World currentWorld = getWorld();
            if(currentWorld instanceof ChooseWorld){
                ChooseWorld chooseWorld = (ChooseWorld) currentWorld;
                chooseWorld.startSimulation();
            }
            return;
        }
        //The button on the end screen that returns to the start
        if(buttonID.equals("backtostart")){
            Greenfoot.setWorld(new StartWorld());
            return;
        }
        //The button on the StartWorld that continues the dialogue and leads to ChooseWorld after dialogue is done
        if(buttonID.equals("continue")){
            World world = getWorld();
            clickCounter++;
            if(clickCounter == 1){
                world.setBackground("startworld.png");
                drawSurvivors();
                middle.scale(700,700);
                world.getBackground().drawImage(middle, 150 , -100);
                world.getBackground().setColor(Color.BLACK);
                world.getBackground().setFont(new Font("Arial", 24));
                world.getBackground().drawString("Most of us survivors are split up,", (world.getWidth()/2) - 200, 110);
                world.getBackground().drawString("with a mismatch of random items that", (world.getWidth()/2) - 200, 160);
                world.getBackground().drawString("may or may not be of use against the", (world.getWidth()/2) - 200, 210);
                world.getBackground().drawString("swarms of zombies every night...", (world.getWidth()/2) - 200, 260);
            } else if (clickCounter == 2){
                world.setBackground("startworld.png");
                drawSurvivors();
                right.scale(700,700);
                world.getBackground().drawImage(right, 50 , -100);
                world.getBackground().setColor(Color.BLACK);
                world.getBackground().setFont(new Font("Arial", 24));
                world.getBackground().drawString("For this simulation choose one survivor,", (world.getWidth()/2-365) , 180);
                world.getBackground().drawString("one weapon and two support items to", (world.getWidth()/2) - 365, 230);
                world.getBackground().drawString("try and survive as long as possible.", (world.getWidth()/2) - 365, 280);
                world.getBackground().drawString("Good luck. Death is guaranteed...", (world.getWidth()/2) - 365, 330);
            } else if (clickCounter == 3){
                Greenfoot.setWorld(new ChooseWorld());
                return;
            }
            return;
        }
        if (buttonID.equals("back")){
            World world = getWorld();
            clickCounter--;
            if(clickCounter == 1){
                world.setBackground("startworld.png");
                drawSurvivors();
                middle.scale(700,700);
                world.getBackground().drawImage(middle, 150 , -100);
                world.getBackground().setColor(Color.BLACK);
                world.getBackground().setFont(new Font("Arial", 24));
                world.getBackground().drawString("Most of us survivors are split up,", (world.getWidth()/2) - 200, 110);
                world.getBackground().drawString("with a mismatch of random items that", (world.getWidth()/2) - 200, 160);
                world.getBackground().drawString("may or may not be of use against the", (world.getWidth()/2) - 200, 210);
                world.getBackground().drawString("swarms of zombies every night...", (world.getWidth()/2) - 200, 260);
            } else if (clickCounter == 2){
                world.setBackground("startworld.png");
                drawSurvivors();
                right.scale(700,700);
                world.getBackground().drawImage(right, 50 , -100);
                world.getBackground().setColor(Color.BLACK);
                world.getBackground().setFont(new Font("Arial", 24));
                world.getBackground().drawString("For this simulation choose one survivor,", (world.getWidth()/2-365) , 180);
                world.getBackground().drawString("one weapon and two support items to", (world.getWidth()/2) - 365, 230);
                world.getBackground().drawString("try and survive as long as possible.", (world.getWidth()/2) - 365, 280);
                world.getBackground().drawString("Good luck. Death is guaranteed...", (world.getWidth()/2) - 365, 330);
            } else if(clickCounter == 0){
                left.scale(700,700);
                world.getBackground().drawImage(left, 320 , -100);
                world.getBackground().setColor(Color.BLACK);
                world.getBackground().setFont(new Font("Arial", 24));
                world.getBackground().drawString("In this reality after World War Three", 530, 180);
                world.getBackground().drawString("the nuclear radiation caused two thirds", 530, 230);
                world.getBackground().drawString("of humanity to turn into zombies,", 530, 280);
                world.getBackground().drawString("leaving the rest of us to survive...",530,330);
            } else if (clickCounter == -1){
                Greenfoot.setWorld(new StartWorld());
            }
        }
        World currentWorld = getWorld();
        // the buttons on ChooseWorld that allow you to pick a survivor and items. has toggleable set to true to show you selected something
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
            } else if (buttonID.equals("shield")){
                world.updateBoolean(7, isSelected);
            } else if (buttonID.equals("wall")){
                world.updateBoolean(8, isSelected);
            }
        }
    }
}