package Tanks;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Represents a projectile fired in the game.
 */
public class Projectile {

    private PVector position;
    private PVector velocity;
    private PVector acceleration;
    private String[][] terrainLayout;
    private boolean exploded;
    private float maxRadius = 20;
    private float gravityAcceleration = 3.6f;
    private Terrain terrain;
    private int radius;
    private ArrayList<Tank> tanks;
    private int projectileSize = 4;
    private boolean isFinish;
    private float windMagnitude;

    /**
     * Constructs a new Projectile with initial parameters.
     *
     * @param projectileSize The size of the projectile.
     * @param position       The initial position of the projectile.
     * @param velocity       The initial velocity of the projectile.
     * @param terrainLayout  The terrain layout represented as a 2D array of
     *                       strings.
     * @param terrain        The terrain object used for collision detection.
     * @param tanks          The list of tanks in the game.
     * @param windMagnitude  The magnitude of wind affecting the projectile's
     *                       trajectory.
     */
    public Projectile(int projectileSize, PVector position, PVector velocity, String[][] terrainLayout, Terrain terrain,
            ArrayList<Tank> tanks, float windMagnitude) {
        this.position = position.copy();
        this.velocity = velocity.copy();
        this.terrainLayout = terrainLayout;
        this.exploded = false;
        this.isFinish = false;
        this.projectileSize = projectileSize;
        this.terrain = terrain;
        this.radius = 0;
        this.acceleration = new PVector(0, gravityAcceleration);
        this.tanks = tanks;
        this.windMagnitude = windMagnitude;
    }

    /**
     * Checks if the projectile hits any tank or terrain.
     *
     * @param tanks      The list of tanks in the game.
     * @param terrain    The terrain object used for collision detection.
     * @param projectile The projectile object to check for collision.
     * @return A string indicating the type of hit ("Tank", "Terrain", or
     *         "Nothing").
     */
    public String hitsTankOrTerrain(ArrayList<Tank> tanks, Terrain terrain, Projectile projectile) {
        // Check if projectile hits any tank or terrain

        for (Tank tank : tanks) {
            if (tank.isHit(projectile.position)) {
                return tank.getPlayerLabel(); // Projectile hits a tank
            }
        }

        if (terrain.isHit(projectile.position)) {
            return "Terrain"; // Projectile hits the terrain
        }

        return "Nothing"; // Projectile does not hit anything
    }

    /**
     * Checks if the given position is out of bounds.
     *
     * @param position The position vector to check.
     * @return True if the position is out of bounds, otherwise false.
     */
    public boolean isOutOfBounds(PVector position) {
        // Check if the given position is out of bounds
        return position.x < 0 || position.y < 0;
    }

    /**
     * Returns the x-coordinate of the projectile's position.
     *
     * @return The x-coordinate of the projectile's position.
     */
    public float getPositionX() {
        return this.position.x;
    }

    /**
     * Returns the y-coordinate of the projectile's position.
     *
     * @return The y-coordinate of the projectile's position.
     */
    public float getPositionY() {
        return this.position.y;
    }

    /**
     * Returns the explosion radius of the projectile.
     *
     * @return The explosion radius of the projectile.
     */
    public float getExplosionRadius() {
        return maxRadius;
    }

    /**
     * Updates the projectile's position and behavior.
     */
    public void update() {
        ArrayList<PVector> curvePoints = terrain.getCurvePoints();

        float windAcceleration = this.windMagnitude * 0.03f; // Wind acceleration in pixels per frame
        velocity.x += windAcceleration; // Adjust horizontal velocity

        // Update position based on velocity
        position.add(velocity);

        if (!exploded) {
            velocity.add(acceleration);
            position.add(velocity);

            if (checkTerrainCollision(curvePoints, position)) {

                explode();
            }
        } else {
            expandExplosion();
        }
    }

    /**
     * Displays the projectile on the PApplet canvas.
     *
     * @param app The PApplet instance used for rendering.
     */

    public void display(PApplet app) {
        if (!exploded && !isFinish) {
            app.stroke(255, 0, 0);
            app.strokeWeight(projectileSize);
            app.point(position.x, position.y);
        } else {
            displayExplosion(app);
        }
    }

     /**
     * Checks if the projectile collides with the terrain or goes out of bounds.
     *
     * @param curvePoints      The list of curve points representing the terrain's shape.
     * @param projectilePosition  The current position of the projectile.
     * @return True if the projectile collides with the terrain or goes out of bounds, otherwise false.
     */
    public boolean checkTerrainCollision(ArrayList<PVector> curvePoints, PVector projectilePosition) {
        float collisionThreshold = 30;

        // Check collision with terrain curve points
        for (PVector point : curvePoints) {
            float distance = PVector.dist(projectilePosition, point);

            if (distance <= collisionThreshold) {

                return true;
            }
        }
        // check the projectile is out of bound
        if (isOutOfBounds(projectilePosition)) {
            return true;
        }

        return false; // No collision detected with terrain or bounds
    }

    /**
     * Initiates the explosion of the projectile.
     * Sets the `exploded` flag to true and records the explosion start time.
     */
    public void explode() {
        exploded = true;
        
    }

    /**
     * Expands the explosion effect of the projectile.
     * Increases the explosion radius until reaching the maximum radius,
     * then resets the projectile's size and radius, and marks the projectile as not exploded.
     */
    public void expandExplosion() {
        if (radius < maxRadius) {
            radius += 1;
        } else {
            projectileSize = 0;
            radius = 0;
            exploded = false;
        }
    }
     /**
     * Checks if the projectile has exploded.
     *
     * @return True if the projectile has exploded, otherwise false.
     */
    public boolean isExploded() {
        return exploded;
    }

    /**
     * Displays the explosion effect on the PApplet canvas.
     *
     * @param app The PApplet instance used for rendering.
     */
    public void displayExplosion(PApplet app) {

        app.noStroke();
        app.fill(255, 0, 0);
        app.ellipse(position.x, position.y, radius * 3f, radius * 3f);

        app.fill(255, 165, 0);
        app.ellipse(position.x, position.y, radius * 2f, radius * 2f);

        app.fill(255, 255, 0);
        app.ellipse(position.x, position.y, radius / 1.5f, radius / 1.5f);

    }

}
