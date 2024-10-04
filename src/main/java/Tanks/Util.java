package Tanks;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Utility class for Tanks game providing various helper methods.
 */
public class Util {

    private String fuelPath = "src/main/resources/Tanks/fuel.png";
    private String parasuatePath = "src/main/resources/Tanks/parachute.png";
    private String leftWind = "src/main/resources/Tanks/wind-1.png";
    private String rightWind = "src/main/resources/Tanks/wind.png";
    private String winner = "src/main/resources/Tanks/winner.gif";

    /**
     * Default Constructor
     */
    public Util() {
    }

    /**
     * Draws a fuel image on the given PApplet.
     *
     * @param app The PApplet instance to draw on.
     */
    public void drawFuel(PApplet app) {
        PImage img = app.loadImage(fuelPath);
        app.image(img, 150, 5, 25, 25);
    }

    /**
     * Draws a parasuate image on the given PApplet.
     *
     * @param app The PApplet instance to draw on.
     */
    public void drawParachute(PApplet app) {
        PImage img = app.loadImage(parasuatePath);
        app.image(img, 150, 40, 25, 25);
    }

    /**
     * Draws a left wind image on the given PApplet.
     *
     * @param app The PApplet instance to draw on.
     */
    public void drawLeftWind(PApplet app) {
        PImage img = app.loadImage(leftWind);
        app.image(img, 30, 40, 40, 40);
    }

    /**
     * Draws a winner GIF image on the given PApplet.
     *
     * @param app The PApplet instance to draw on.
     */
    public void getWinnerGif(PApplet app) {

        PImage img = app.loadImage(winner);
        app.image(img, 350, 100, 150, 150);
    }

    /**
     * Draws a right wind image on the given PApplet.
     *
     * @param app The PApplet instance to draw on.
     */
    public void drawRightWind(PApplet app) {
        PImage img = app.loadImage(rightWind);
        app.image(img, 30, 40, 40, 40);
    }

    /**
     * Buys fuel for the specified tank if the score is sufficient.
     *
     * @param currentActiveTank The tank to buy fuel for.
     * @param soundManager      The sound manager to play sounds.
     * @param type              Type of the item that player wants to by e.g parachute or fuel
     */
    public void buyThings(Tank currentActiveTank, SoundManager soundManager, String type) {
        // Check if the current tank can afford the repair kit (cost: 10)
        if (currentActiveTank.getScore() >= 15) {
            if (type.equals("parasuate")) {
                currentActiveTank.setParasuate(1);
            } 
            // Deduct the cost of the fuel from the tank's score
            currentActiveTank.setScore(currentActiveTank.getScore() - 15);
        } else if(currentActiveTank.getScore() >= 10)
        {
            if (type.equals("fuel")) {
                float remaningFuel = currentActiveTank.getFuel();
                // Set the tank's fuel to the new value
                currentActiveTank.setFuel(remaningFuel + 200);
            }
            // Deduct the cost of the fuel from the tank's score
            currentActiveTank.setScore(currentActiveTank.getScore() - 10);
        } 
        
        else {
            soundManager.playErrorSound();
        }
    }

    public boolean repairTank(Tank currentActiveTank)
    {
        if(currentActiveTank.getScore() >= 20)
        {
                // Increase the tank's health by 20
                int currentHealth = currentActiveTank.getHealth();
                int maxHealth = currentActiveTank.getMaxHealth();
                int repairAmount = 20;

                // Calculate new health after repair
                int newHealth = Math.min(currentHealth + repairAmount, maxHealth);

                // Deduct the cost of the repair kit from the tank's score
                currentActiveTank.setScore(currentActiveTank.getScore() - 20);

                // Set the tank's health to the new value
                currentActiveTank.setHealth(currentHealth+20);
            
            // Deduct the cost of the fuel from the tank's score
            currentActiveTank.setScore(currentActiveTank.getScore() - 20);
            return true;
        }
        return false;
    }

    // /**
    // * Displays the explosion effect on the PApplet canvas.
    // *
    // * @param app The PApplet instance used for rendering.
    // */
    // public void displayExplosion(PApplet app,Tank tank) {

    // app.noStroke();
    // app.fill(255, 0, 0);
    // app.ellipse(tank.getPosition().x, tank.getPosition().y, 3f, 3f);

    // app.fill(255, 165, 0);
    // app.ellipse(tank.getPosition().x, tank.getPosition().y, 2f, 2f);

    // app.fill(255, 255, 0);
    // app.ellipse(tank.getPosition().x, tank.getPosition().y, 1.5f, 1.5f);

    // }

}
