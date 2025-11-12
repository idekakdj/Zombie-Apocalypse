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
    boolean wasClicked = false;
    private String buttonID;
    public Button(String text,int height,int width,Color color,int borderWidth, Color borderColor,int fontSize, String id){
        this.buttonID = id;
        GreenfootImage button = new GreenfootImage(width,height);
        if(borderWidth > 0 ){
            button.setColor(borderColor);
            button.fill();
            
            button.setColor(color);
            button.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        } else {
            button.setColor(color);
            button.fill();
        }
        button.setColor(Color.WHITE); 
        button.setFont(new Font("Arial", fontSize));  
        button.drawString(text, width/2 - text.length() * 5, height/2 + 7); 
        
        setImage(button);
    }
    public void act()
    {
        if(Greenfoot.mouseClicked(this)){
            wasClicked = true;
        }
    }
    public String getButtonID(){
        return buttonID;
    }
    public boolean wasClicked(){
        return wasClicked;
    }
}
