import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A simple automatic melee weapon: the Bat.
 * Swings automatically on a timer, no player input.
 * 
 * Author Jayden assisted by Paul
 */
public class Bat extends Melee
{
    private Survivors owner;     // Survivor this bat follows
    private boolean isSwinging = false;
    private int swingAngle = 0;
    private int rotationSpeed = 10;  // degrees per frame
    private int coolDownTimer = 0;   // frame counter
    private int startRotation = 0;
    private int offsetX = 30;        // position offset from survivor
    private int offsetY = 0;
    GreenfootImage bat = new GreenfootImage("baseballbat.png");

    public Bat(int damage, int coolDown, int range, Survivors owner)
    {
        super(damage, coolDown, range);
        this.owner = owner;
        startRotation = 0;
        bat.scale(30,50);
        setImage(bat);
    }

    public void act()
    {
        followOwner();

        if (coolDownTimer > 0) {
            coolDownTimer--;
        }

        // Start swing automatically when cooldown finishes
        if (!isSwinging && coolDownTimer == 0 && isTouching(Zombie.class)) {
            startSwing();
        }

        if (isSwinging) {
            performSwing();
        }
    }

    private void followOwner()
    {
        if (owner != null && getWorld() != null) {
            setLocation(owner.getX() + offsetX, owner.getY() + offsetY);
        }
    }

    private void startSwing()
    {
        isSwinging = true;
        swingAngle = -90;
        coolDownTimer = coolDown;
        playSound();
    }

    private void performSwing()
    {
        setRotation(startRotation + swingAngle);
        swingAngle += rotationSpeed;

        if (swingAngle >= 90) {
            isSwinging = false;
            setRotation(startRotation);
        }
    }

    public void playSound()
    {
        // Optional: add sound file if desired
        // Greenfoot.playSound("bat_hit.wav");
    }

    public void attack()
    {
        // (Optional) automatic hit detection, not needed if visual only
    }
}
