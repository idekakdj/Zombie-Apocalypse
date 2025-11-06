import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * @author Paul 
 */
public class Button extends Actor
{
    public Button(String text,int height,int width,Color color,int borderWidth, Color borderColor){
        GreenfootImage button = new GreenfootImage(width,height);
        button.setColor(color);
        button.fill();
    }
    public void act()
    {
        
    }
}
