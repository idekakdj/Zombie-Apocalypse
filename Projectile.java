import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectile extends Actor
{
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        move (5);
        Zombie z = (Zombie) getOneIntersectingObject(Zombie.class);
        if (z != null)
        {
            z.takeDamage(10);    // reduce zombie's health
            if (z.isDead()) {
                z.killZombie();      // remove dead zombie
            }
                        getWorld().removeObject(this); // remove the projectile after hitting

        
        }
}
}