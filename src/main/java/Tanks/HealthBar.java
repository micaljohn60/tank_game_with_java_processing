package Tanks;

import processing.core.PApplet;

/**
 * Represents a health bar used to display the health status of an entity.
 * The health bar consists of a filled portion that visually represents the current health level.
 */
public class HealthBar{
    private float x;
    private float y;
    private float width;
    private float height;
    private int maxHealth;
    private int currentHealth;
    private int[] fillColor;
    private int[] borderColor;

    /**
     * Constructs a new HealthBar instance with specified parameters.
     *
     * @param x            The x-coordinate of the health bar's position.
     * @param y            The y-coordinate of the health bar's position.
     * @param width        The width of the health bar.
     * @param height       The height of the health bar.
     * @param fillColor    An array representing the RGB color values of the filled portion.
     * @param borderColor  An array representing the RGB color values of the border.
     * @param currentHealth The initial current health value of the entity.
     */
    public HealthBar(float x, float y, float width, float height, int[] fillColor, int[] borderColor,int currentHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = 100;
        this.currentHealth = currentHealth;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    /**
     * Sets the current health value of the entity.
     *
     * @param currentHealth The new current health value to set.
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Displays the health bar on the specified PApplet canvas.
     *
     * @param applet The PApplet instance on which to render the health bar.
     */
    public void display(PApplet applet) {
        // Draw border
        applet.stroke(0, 0, 0);
        applet.fill(0,0,0);
        

        // Draw filled portion representing current health
        float healthPercentage = (float) currentHealth / maxHealth;
        float filledWidth = width * healthPercentage;
        applet.rect(400, y, width, height);
        applet.fill(fillColor[0], fillColor[1], fillColor[2]);
        applet.rect(400 , y, filledWidth, height);
    }
}
