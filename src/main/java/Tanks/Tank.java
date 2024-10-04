package Tanks;

import java.util.ArrayList;

import Tanks.interfaces.TankCallBack;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * This is the initialization of player with the interpolating terrain
 * firing the Projectile and moving mechanism
 */
public class Tank extends PApplet {

    private float tankSize;
    private float x;
    private float y;
    private int[] color;
    private String playerLabel;
    protected String[][] terrainLayout;
    protected float[][] terrainCurve;
    protected int speed = 1;

    private PVector position;
    private float moveSpeed = 1;
    private ArrayList<PVector> curvePoints;
    private float turretRotationSpeed;
    private float turretAngle;
    private int powerLevel;
    private Terrain terrain;
    private int parachuteCount;
    private int maxHealth;
    private HealthBar healthBar;
    private int score;
    private boolean alive;
    private int health;
    private float fuel;
    private float initialFuel = 250;
    private int initialPower = 50;
    private float windMagnitude;

    private Projectile projectile; // Reference to the current projectile
    public TankCallBack turnCallback;
    // private ArrayList<Projectile> projectiles;
    private boolean isHit;
    protected ArrayList<Tank> mytanks;

    private boolean isLargerProjectile;
    private boolean hasShield = false;

    /**
     * Constructs a Tank object with the specified attributes.
     *
     * @param playerLabel   The label identifying the player associated with the
     *                      tank.
     * @param maxHealth     The maximum health value of the tank.
     * @param tankSize      The size of the tank.
     * @param x             The initial x-coordinate of the tank's position.
     * @param y             The initial y-coordinate of the tank's position.
     * @param color         The RGB color array representing the tank's color.
     * @param terrainLayout The layout of the terrain where the tank is placed.
     * @param curvePoints   The list of curve points defining the terrain curve.
     * @param terrain       The Terrain object representing the game environment.
     * @param turnCallback  The callback interface for handling turn changes.
     * @param windMagnitude The magnitude of the wind affecting the tank's actions.
     */
    public Tank(String playerLabel, int maxHealth, float tankSize, float x, float y, int[] color,
            String[][] terrainLayout, ArrayList<PVector> curvePoints, Terrain terrain, TankCallBack turnCallback,
            float windMagnitude) {
        this.terrainLayout = terrainLayout;
        this.parachuteCount = 1;
        this.playerLabel = playerLabel;
        this.terrain = terrain;
        this.tankSize = tankSize;
        this.isLargerProjectile = false;
        this.x = x;
        this.y = y;
        this.color = color;
        this.score = 0;
        this.turretAngle = -30;
        this.curvePoints = curvePoints;
        this.powerLevel = initialPower;
        this.health = 100;
        this.turretRotationSpeed = 0.3f;
        this.position = new PVector(x, y);
        this.alive = true;
        this.maxHealth = maxHealth;
        this.isHit = false;
        this.fuel = initialFuel;
        this.turnCallback = turnCallback;
        this.windMagnitude = windMagnitude;
        this.healthBar = new HealthBar(x, y - 10, 50, 5, color, color, this.health);
    }

    /**
     * Increases the current parachute count by the specified amount.
     * 
     * @param count The number of parachutes to add to the current count.
     */
    public void setParasuate(int count) {
        this.parachuteCount += count;
    }

    /**
     * Retrieves the current number of parachutes available.
     * 
     * @return The current parachute count.
     */
    public int getParasuate() {
        return this.parachuteCount;
    }

    /**
     * 
     * @param projectile - set the projectile
     */
    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    /**
     * 
     * @param largerProjectile set the larger projectile
     */
    public void setLargetProjectile(boolean largerProjectile) {
        this.isLargerProjectile = largerProjectile;
    }

    /**
     * Responsiale for current tank's max health
     * 
     * @return retur the maxHealth of the Tank
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * set current health with the parameter
     * 
     * @param health set the current health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * decide current project is larger or normal one
     * 
     * @return true if the projecteile is set and false to normal one
     */
    public boolean getLargetProjectile() {
        return this.isLargerProjectile;
    }

    /**
     * get the fuel of the gank
     * 
     * @return return current fuel
     */
    public float getFuel() {
        return fuel;
    }

    /**
     * to activate shield
     */
    public void activateShield() {
        this.hasShield = true;
    }

    /**
     * to activate shield
     */
    public void removeShield() {
        this.hasShield = false;
    }

    /**
     * Method to check the current has shild or nor
     * 
     * @return ture if a shield is activated, flase for no shield
     */
    public boolean hasActiveShield() {
        return this.hasShield;
    }

    /**
     * take damage and reduce health if a tank is hit by a projectile
     */
    public void handleHit() {
        if (this.hasShield) {
            // Shield absorbs the hit, so deactivate the shield
            this.hasShield = false;
        } else {
            // Handle tank damage when shield is not active
            takeDamage(10);
        }
    }

    /**
     * get the current tank's fire power levels
     * 
     * @return return current tank's power level
     */
    public int getPowerLevel() {
        return this.powerLevel;
    }

    /**
     * increase a new power level
     * 
     * @param powerLevel set a new powerlevel with the parameter
     */
    public void setPowerLevelUp(int powerLevel) {
        if (this.powerLevel <= this.getHealth()) {
            this.powerLevel += powerLevel;
        }

    }

    /**
     * decrease a new power level
     * 
     * @param powerLevel set a new powerlevel with the parameter
     */
    public void setPowerLevelDown(int powerLevel) {
        if (this.powerLevel > 0) {
            this.powerLevel -= powerLevel;
        }

    }

    /**
     * set new fuel capacity
     * 
     * @param fuel set a new fuel capacity with the parameter
     */
    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    /**
     * decrease fuel by the distance
     * 
     * @param distance travel distance by a tank
     */
    public void consumeFuel(float distance) {
        this.fuel -= distance;
        if (this.fuel < 0) {
            this.fuel = 0; // Ensure fuel doesn't go negative
        }
    }

    /**
     * Increases the tank's score by the specified amount.
     *
     * @param score The amount to increase the tank's score by.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves the current score of the tank.
     *
     * @return The current score of the tank.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Retrieves the player label associated with the tank.
     *
     * @return The player label of the tank.
     */
    public String getPlayerLabel() {
        return this.playerLabel;
    }

    /**
     * Checks if the tank is currently alive.
     *
     * @return `true` if the tank is alive; `false` otherwise.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Retrieves the current health of the tank.
     *
     * @return The current health of the tank.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Retrieves the color of the tank.
     *
     * @return The RGB color array representing the tank's color.
     */
    public int[] getColor() {
        return this.color;
    }

    /**
     * Take damage by the current tank
     * 
     * @param damageAmount the amount to decrease tank's health
     * @return return the current health of the tank
     */
    public int takeDamage(int damageAmount) {
        this.health -= damageAmount;
        if (this.health < 0) {
            this.health = 0;
            this.alive = false;

        }
        // healthBar.setCurrentHealth(health);
        return health;
    }

    /**
     * Checks if the tank is hit by a projectile based on the projectile's position.
     *
     * @param projectilePosition The position vector of the projectile.
     * @return `true` if the tank is hit by the projectile; `false` otherwise.
     */
    public boolean isHit(PVector projectilePosition) {
        // Check if the tank is hit by a projectile
        float distance = calculateDistanceToProjectile(projectilePosition.x, projectilePosition.y);
        return distance < 40;
    }

    public float calculateDistanceToProjectile(float projectileX, float projectileY) {
        // Calculate distance using the Euclidean distance formula
        float dx = projectileX - position.x;
        float dy = projectileY - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance;
    }

    public Tank findTankByLabel(String label, ArrayList<Tank> tanks) {
        for (Tank tank : tanks) {
            if (tank.getPlayerLabel().equals(label)) {
                return tank; // Return the tank with the specified label
            }
        }
        return null; // Return null if no tank with the specified label is found
    }

    public void updatePosition(boolean moveLeft, boolean moveRight, boolean moveUp, boolean moveDown, boolean fire,
            ArrayList<Tank> tanks) {
        // Update tank position based on movement flags
        if (moveLeft) {
            moveLeft(); // Move tank left
        } else if (moveRight) {
            moveRight(); // Move tank right
        } else if (moveUp) {
            turrentMoveUp();
        } else if (moveDown) {
            turrentMoveDown();

        } else if (fire) {
            fireProjectile(tanks);

        }
    }

    /**
     * Fires a projectile from the tank's turret towards the specified direction.
     *
     * @param tanks The list of tanks in the game (used for collision detection).
     */
    public void fireProjectile(ArrayList<Tank> tanks) {

        float initialVelocity = PApplet.map(powerLevel, 0, 14, 1, 9); // Map power level to velocity (1-9 pixels/frame)

        float vx = initialVelocity * PApplet.cos(turretAngle);
        float vy = -initialVelocity * PApplet.sin(turretAngle);

        // Calculate the end point of the turret based on tank size and angle
        float turretEndX = x + (tankSize / 2) * PApplet.cos(turretAngle);
        float turretEndY = y - (tankSize / 2) * PApplet.sin(turretAngle);

        // Offset the initial position of the projectile along the turret direction
        float projectileOffsetX = turretEndX + (tankSize / 2 + 5) * PApplet.cos(turretAngle);
        float projectileOffsetY = turretEndY - (tankSize / 2 + 5) * PApplet.sin(turretAngle);

        // Create a PVector for the projectile's start position
        PVector projectileStartPosition = new PVector(projectileOffsetX, projectileOffsetY);
        PVector velocity = new PVector(vx, vy);

        if (isLargerProjectile) {
            projectile = new Projectile(10, projectileStartPosition, velocity, terrainLayout, terrain, tanks,
                    windMagnitude);

        } else {
            projectile = new Projectile(5, projectileStartPosition, velocity, terrainLayout, terrain, tanks,
                    windMagnitude);

        }
        mytanks = tanks;
    }

    /**
     * Rotates the tank's turret upwards (counter-clockwise) if within allowable
     * angle limits.
     * Decreases the turret angle to move the turret upwards.
     */
    public void turrentMoveUp() {
        // Rotate turret left (counter-clockwise)
        if (turretAngle > -31.5) {
            turretAngle -= turretRotationSpeed / frameRate;
        }
    }

    /**
     * Rotates the tank's turret downwards (clockwise) if within allowable angle
     * limits.
     * Increases the turret angle to move the turret downwards.
     */
    public void turrentMoveDown() {
        if (turretAngle < -28) {
            // Rotate turret left (counter-clockwise)
            turretAngle += turretRotationSpeed / frameRate;
        }
    }

    /**
     * Returns the tank's current position as a PVector.
     *
     * @return A PVector representing the tank's position (x, y).
     */
    public PVector getPosition() {
        return new PVector(x, y); // Return the tank's position as a PVector
    }

    /**
     * Moves the tank to the left along the curve, adjusting its position and
     * consuming fuel.
     * The tank's x-coordinate is shifted to the left by the specified move speed.
     * The tank's y-coordinate is interpolated based on its new x-coordinate
     * position.
     * Fuel is decreased by 1 unit upon moving left.
     */
    private void moveLeft() {
        // Move tank to the left along the curve
        float targetX = x - moveSpeed;
        x = PApplet.constrain(targetX, curvePoints.get(0).x, curvePoints.get(curvePoints.size() - 1).x);
        y = interpolateTankPosition(targetX);
        this.fuel -= 1;

    }

    /**
     * Moves the tank to the right along the curve, adjusting its position and
     * consuming fuel.
     * The tank's x-coordinate is shifted to the right by the specified move speed.
     * The tank's y-coordinate is interpolated based on its new x-coordinate
     * position.
     * Fuel is decreased by 1 unit upon moving right.
     */
    private void moveRight() {
        // Move tank to the right along the curve
        float targetX = x + moveSpeed;
        x = PApplet.constrain(targetX, curvePoints.get(0).x, curvePoints.get(curvePoints.size() - 1).x);
        y = interpolateTankPosition(targetX);
        this.fuel -= 1;
    }

    /**
     * Interpolates the y-coordinate of the tank's position based on the given
     * target x-coordinate
     * using linear interpolation between the two closest curve points.
     *
     * @param targetX The target x-coordinate for which to interpolate the
     *                y-coordinate.
     * @return The interpolated y-coordinate corresponding to the given target
     *         x-coordinate.
     *         Returns 0 if interpolation fails (e.g., due to missing curve points).
     */
    float interpolateTankPosition(float targetX) {
        // Find the two closest curve points around targetX for interpolation
        PVector p1 = null, p2 = null;

        for (PVector point : curvePoints) {
            if (point.x <= targetX) {
                p1 = point;
            } else {
                p2 = point;
                break;
            }
        }

        if (p1 != null && p2 != null) {
            // Linear interpolation based on x-distance from p1 to p2
            float t = (targetX - p1.x) / (p2.x - p1.x);
            float interpolatedY = lerp(p1.y, p2.y, t);
            return interpolatedY;
        }
        return 0; // Default fallback if interpolation fails
    }

    /**
     * Draws the health bar for the tank at the specified index.
     * This method delegates the display of the health bar to the associated PApplet
     * instance.
     *
     * @param currentIndex The index of the tank in the collection.
     */
    void drawHealthBar(int currentIndex) {

        healthBar.display(this);
    }

    /**
     * Checks if the tank has been hit.
     *
     * @return true if the tank has been hit; false otherwise.
     */
    public boolean getHit() {
        return this.isHit;
    }

    /**
     * Sets the hit status of the tank.
     *
     * @param hit true to mark the tank as hit; false otherwise.
     */
    public void setHit(boolean hit) {
        this.isHit = hit;
    }

    /**
     * Draws a shield around the tank if an active shield is present.
     *
     * @param app The PApplet instance used for drawing.
     */
    public void drawShield(PApplet app) {
        if (hasActiveShield()) {

            app.fill(0, 0, 255, 100);
            app.ellipse(this.getPosition().x, this.getPosition().y, 20 * 2, 20 * 2);
        }
    }

    /**
     * Draws the tank on the screen using the provided PApplet instance and manages
     * the tank's interactions.
     *
     * @param app The PApplet instance used for drawing.
     * @param sm  The SoundManager instance for managing sound effects.
     */

    void drawTank(PApplet app, SoundManager sm) {

        app.stroke(0);
        app.strokeWeight(2);

        app.rectMode(app.CENTER);
        // Draw tank body (rectangle)
        app.fill(this.color[0], this.color[1], this.color[2]);

        // Draw tank turret (ellipse)
        app.rect(x, y, tankSize, tankSize / 2); // Draw tank body

        float turretLength = tankSize * 1.50f; // Length of the turret
        float turretEndX = x + turretLength * PApplet.cos(turretAngle);
        float turretEndY = y - turretLength * PApplet.sin(turretAngle); // Negative sin for correct direction

        // Draw turret
        app.line(x, y - tankSize / 2, turretEndX, turretEndY);

        drawShield(app);

        app.rect(x, y - 10, tankSize / 2, tankSize / 3);
        int index = 0;
        if (projectile != null) {
            projectile.update();

            if (projectile.isExploded()) {
                sm.playExplosionSound();

                String hitObject = projectile.hitsTankOrTerrain(mytanks, terrain, projectile);

                displayExplosion(app, new PVector(projectile.getPositionX(), projectile.getPositionY()),
                        projectile.getExplosionRadius());

                if (hitObject.equals("Terrain") || hitObject.equals("Nothing")) {
                    isHit = true;
                    index = turnCallback.endTurn(hitObject, 0);

                } else {

                    // int dmg = takeDamage(10);
                    index = turnCallback.endTurn(hitObject, 10);
                }
                projectile = null;
            } else {
                projectile.display(app);
            }

        }
    }

    /**
     * Displays an explosion animation at the specified position with the given
     * radius.
     *
     * @param app      The PApplet instance used for drawing.
     * @param position The position of the explosion as a PVector.
     * @param radius   The radius of the explosion animation.
     */
    void displayExplosion(PApplet app, PVector position, float radius) {
        // Draw explosion animation at the projectile's position
        app.noStroke();
        app.fill(255, 0, 0);
        app.ellipse(position.x, position.y, radius * 3f, radius * 3f);

        app.fill(255, 165, 0);
        app.ellipse(position.x, position.y, radius * 2f, radius * 2f);

        app.fill(255, 255, 0);
        app.ellipse(position.x, position.y, radius / 1.5f, radius / 1.5f);
    }
}
