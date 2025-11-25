import greenfoot.*;
import java.util.List;
/*
 * @author Jayden
 */
public class Gun extends Ranged
{
    
    public Gun(int damage, int fireRate, Survivors owner)
    {
        super(damage,fireRate, owner);

        setImage("gun.png");
    }

    public void act()
    {
        super.act();
    }
    
}

