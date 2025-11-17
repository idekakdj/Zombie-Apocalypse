import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * @author Paul
 * 
 * 
 * Takes in parameters to be reusable and make different upgrade progress bars.
 * width is the width of the bar
 * height is the height of the bar
 * color is the main portion of the bar's color
 * borderWidth is how many px you want your border ( won't draw if = 0)
 * borderColor sets the border color
 * text is the text on the progress bar
 */
public class UpgradeProgressBar extends Actor
{
    private int width;
    private int height;
    private Color color;
    private int borderWidth;
    private Color borderColor;
    private String text;
    GreenfootImage rect;
    public UpgradeProgressBar(int width, int height, Color color, int borderWidth, Color borderColor, String text){
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.text = text;
        drawBar();
    }
    public void drawBar(){
        rect = new GreenfootImage(width, height);
        if(borderWidth > 0){
            rect.setColor(borderColor);
            rect.fillRect(0,0,width,height);
            
            rect.setColor(color);
            rect.fillRect(borderWidth, borderWidth,width - (borderWidth * 2), height - (borderWidth * 2));
        } else{
            rect.setColor(color);
            rect.fillRect(0, 0,width, height);
        }
        
    }
    public void act()
    {
        
    }
}
