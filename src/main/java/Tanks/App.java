package Tanks;

import Tanks.interfaces.TankCallBack;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
 * The App class serves as the entry point for the application.
 * It initializes the configuration file path.
 */
public class App extends PApplet implements TankCallBack {

    private JSONObject json;
    private JSONArray levels;
    private int currentLevelIndex = 0;
    // public static boolean last = false;

    public static final int CELLSIZE = 32; // 8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; // CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; // BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;
    public static PImage bg = null;
    public static final int FPS = 30;

    private String configPath;

    public static Random random = new Random();
    private String[][] terrainLayout = null;
    private Config config;
    private Terrain terrain;

    // private ArrayList<PVector> tankSpawnPoints;
    private Map<String, PVector> tankSpawnPoints;

    private JSONObject playerColorsObject;
    private JSONObject currentLevel;
    private int currentTurnIndex = 0;

    private ArrayList<PVector> curvePoints;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;
    private boolean fire;

    private ScoreBoard scoreBoard;
    private SoundManager soundManager;
    private HealthBar healthBar;

    private Tank currentActiveTank = null;
    private ArrayList<Tank> tanks;
    private ArrayList<Tank> tanksLabel;

    private int[] color;
    private int maxHealth = 100;
    private String hitObject = "Nothing";
    private boolean isGameFinished = false;

    private int windMagnitude;
    private boolean isNextLevle = false;

    Util util = new Util();

    /**
     * 
     * Constructor for the App class. Initializes the configPath with a default
     * value.
     * 
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player
     * and map elements.
     */
    @Override
    public void setup() {

        frameRate(FPS);
        smooth();

        try {

            config = new Config(this, this.configPath);
            levels = config.getLevels();
            currentLevel = levels.getJSONObject(currentLevelIndex); // Change index as needed
            String layoutFile = currentLevel.getString("layout");
            String background = currentLevel.getString("background");
            String foregroundColor = currentLevel.getString("foreground-colour");
            String tree = currentLevel.getString("trees");
            playerColorsObject = config.getPlayerColors();

            terrain = new Terrain(currentLevelIndex, layoutFile, background, foregroundColor, tree,
                    WIDTH, HEIGHT);
            LoadData loadNewData = new LoadData(layoutFile);
            terrainLayout = loadNewData.loadTerrain(currentLevelIndex);

            tankSpawnPoints = terrain.findTankSpawnPointsTwo();

            curvePoints = terrain.generateCurvePoints(terrainLayout, width, height);

            tanks = new ArrayList<>();
            if (!isNextLevle) {
                tanksLabel = new ArrayList<>();
            }

            spawnTanks();

            scoreBoard = new ScoreBoard(terrain, playerColorsObject, tanksLabel);
            soundManager = new SoundManager(this);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            exit();
        }

    }

    /**
     * Removes a tank with zero health from the list of tanks if it matches the
     * specified hitObject.
     * 
     * @param hitObject the identifier of the tank to check and potentially remove
     */
    public void removeTankWithZeroHealth(String hitObject) {
        // Iterate over the tanks list to find a tank with health <= 0
        for (int i = 0; i < this.tanks.size(); i++) {
            Tank tank = this.tanks.get(i);
            if (tank.getHealth() <= 0 && tank.getPlayerLabel().equals(hitObject)) {
                this.tanks.remove(i);
                this.hitObject = "Nothing";
                // Adjust currentTurnIndex after removal
                if (tanks.isEmpty()) {
                    // No tanks left, set currentTurnIndex to an appropriate value (e.g., -1)
                    currentTurnIndex = -1;
                } else {
                    // Adjust currentTurnIndex to remain within valid bounds
                    currentTurnIndex = currentTurnIndex % tanks.size();
                }
                // Exit the loop after removing the tank
                break;
            }
        }
    }

    /**
     * Reduces the health of a tank with a specified hitObject label by the given
     * health value.
     * Updates the health bar display accordingly.
     *
     * @param hitObject The label of the tank that is hit.
     * @param health    The amount of health to subtract from the tank.
     * @param realTank  current acive tank
     */
    public void tankIsHit(String hitObject, int health, Tank realTank) {
        for (int i = 0; i < tanks.size(); i++) {
            Tank hitTank = tanks.get(i);
            if (realTank.getPlayerLabel().equals(hitTank.getPlayerLabel())) {

            } else if (hitTank.getPlayerLabel().equals(hitObject)) {
                System.out.println("Hit Object : " + hitObject);
                scoreBoard.update(realTank);
                if (hitTank.hasActiveShield()) {
                    hitTank.removeShield();
                } else {
                    hitTank.setHealth(hitTank.getHealth() - health);
                }

            }
        }
        healthBar = new HealthBar((width - 200) / 2, 20, 200, 20, currentActiveTank.getColor(),
                currentActiveTank.getColor(), currentActiveTank.getHealth());

    }

    /**
     * Spawns tanks at designated spawn points based on configuration and terrain
     * layout.
     * Initializes tanks with player labels, health, positions, colors, and other
     * attributes.
     * Sets the current active tank and initializes the health bar accordingly.
     */
    public void spawnTanks() {
        if (tankSpawnPoints != null) {
            try {
                // Parse player colors from the configuration file
                Map<String, int[]> playerColors = parsePlayerColors();

                if (playerColors != null && !playerColors.isEmpty()) {
                    for (Map.Entry<String, PVector> entry : tankSpawnPoints.entrySet()) {
                        String playerLabel = entry.getKey();
                        PVector point = entry.getValue();

                        // Determine the row and column indices based on the point coordinates
                        int col = (int) (point.x / CELLSIZE);
                        int row = (int) (point.y / CELLSIZE);

                        if (row >= 0 && row < terrainLayout.length && col >= 0 && col < terrainLayout[row].length) {
                            // Retrieve the color corresponding to the player label
                            color = playerColors.get(playerLabel);

                            if (color != null) {

                                Tank newTank = new Tank(playerLabel, maxHealth, 20, point.x, point.y, color,
                                        terrainLayout, curvePoints, terrain, this, windMagnitude);
                                tanks.add(newTank);
                                if (!isNextLevle) {
                                    tanksLabel.add(newTank);
                                }

                                if (currentActiveTank == null) {
                                    currentActiveTank = newTank;
                                    healthBar = new HealthBar((width - 200) / 2, 20, 200, 20,
                                            currentActiveTank.getColor(), currentActiveTank.getColor(), 100);
                                    System.out.println("Current active tank set: " + currentActiveTank);
                                }
                            } else {
                                System.out.println("No color defined for player label: " + playerLabel);
                            }
                        } else {
                            System.out.println("Invalid tank spawn point: " + point);
                        }
                    }
                } else {
                    System.out.println("Player colors map is empty or null");
                }
            } catch (Exception e) {
                System.err.println("Error spawning tanks: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Tank spawn points map is null");
        }
    }

    /**
     * Ends the current player's turn and advances to the next player's turn.
     *
     * @param hitObject The object hit during the turn (e.g., "Terrain", "Nothing",
     *                  or a tank's label).
     * @param health    The health value associated with the hit object (used if
     *                  hitting a tank).
     * @return The index of the new current turn player in the tanks list.
     */
    @Override
    public int endTurn(String hitObject, int health) {
        // Set the hitObject to indicate the target of the last shot
        this.hitObject = hitObject;

        // Retrieve the current active tank before ending the turn
        Tank realCurrentActiveTank = tanks.get(currentTurnIndex);

        // Move to the next player's turn by advancing the turn index
        currentTurnIndex = (currentTurnIndex + 1) % tanks.size();
        currentActiveTank = tanks.get(currentTurnIndex);

        // Update wind after each turn
        windMagnitude += (int) random(-20, 20);
        // Clamp wind magnitude within -35 to 35 range
        windMagnitude = constrain(windMagnitude, -35, 35);

        // Update the health bar based on the hit object
        if (hitObject.equals("Terrain") || hitObject.equals("Nothing")) {
            healthBar = new HealthBar((width - 200) / 2, 20, 200, 20,
                    currentActiveTank.getColor(), currentActiveTank.getColor(), currentActiveTank.getHealth());
        } else {

            tankIsHit(hitObject, health, realCurrentActiveTank);
        }
        realCurrentActiveTank.setLargetProjectile(false);
        return currentTurnIndex;
    }

    /**
     * Parses player colors from a JSON configuration object and stores them in a
     * map.
     * The JSON configuration should contain player labels as keys and RGB color
     * strings as values.
     *
     * @return A map associating player labels with integer arrays representing RGB
     *         colors (e.g., {red, green, blue}).
     */
    @SuppressWarnings("unchecked")
    public Map<String, int[]> parsePlayerColors() {
        // Initialize a new map to store player colors
        Map<String, int[]> playerColors = new HashMap<>();

        // Retrieve player colours from the JSON configuration
        Set<String> keys = playerColorsObject.keys();

        // Iterate over each player label in the keys set
        if (playerColorsObject != null) {
            for (String playerLabel : keys) {
                String colorStr = playerColorsObject.getString(playerLabel);

                String[] rgbValues = colorStr.split(",");

                if (rgbValues.length == 3) {
                    try {
                        int red = Integer.parseInt(rgbValues[0].trim());
                        int green = Integer.parseInt(rgbValues[1].trim());
                        int blue = Integer.parseInt(rgbValues[2].trim());
                        int[] color = { red, green, blue };
                        playerColors.put(playerLabel, color);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid RGB values for player label: " + playerLabel);
                    }
                } else {
                    System.out.println("Invalid color format for player label: " + playerLabel);
                }
            }
        } else {
            System.out.println("Player color configuration is null");
        }
        return playerColors;
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        // Set movement flags based on which keys are pressed
        int key = event.getKeyCode();
        if (key == 37) {
            moveLeft = true;
            soundManager.playTankMovingSOund();
        } else if (key == 39) {
            moveRight = true;
            soundManager.playTankMovingSOund();
        } else if (key == 38) {
            moveUp = true;
        } else if (key == 40) {
            moveDown = true;
        } else if (key == 32) {
            fire = true;
            soundManager.playFireSound();
        } else if (key == 87) {
            currentActiveTank.setPowerLevelUp(1);
        } else if (key == 83) {
            currentActiveTank.setPowerLevelDown(1);
        } else if (key == 88) {
            if (currentActiveTank.getScore() < 20)
                soundManager.playErrorSound();
            currentActiveTank.setScore(currentActiveTank.getScore() - 20);
            currentActiveTank.setLargetProjectile(true);
        } else if (key == 72) {
            if (currentActiveTank.getScore() < 20) {
                soundManager.playErrorSound();
            } else {
                currentActiveTank.setScore(currentActiveTank.getScore() - 20);
                currentActiveTank.activateShield();
            }

        } else if (key == 82) {
            if (isGameFinished) {
                currentLevelIndex = 0;
                setup();
                isGameFinished = false;
            } else {
                boolean result = util.repairTank(currentActiveTank);
                if (result) {
                    healthBar = new HealthBar((width - 200) / 2, 20, 200, 20, currentActiveTank.getColor(),
                            currentActiveTank.getColor(), currentActiveTank.getHealth());
                } else {
                    soundManager.playErrorSound();
                }
            }

        } else if (key == 70) {
            util.buyThings(currentActiveTank, soundManager, "fuel");
        } else if (key == 80) {
            util.buyThings(currentActiveTank, soundManager, "parasuate");
        } else if (key == 78) {
            for (int i = 0; i < tanks.size(); i++) {
                if (tanks.size() == 1) {
                    loadNextLevel();
                } else {
                    tanks.remove(i);
                    currentTurnIndex = currentTurnIndex % tanks.size();
                    break;
                }
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyReleased() {
        // Reset movement flags when keys are released
        if (keyCode == LEFT) {
            moveLeft = false;
            soundManager.stopTankMoving();
        } else if (keyCode == RIGHT) {
            soundManager.stopTankMoving();
            moveRight = false;

        } else if (keyCode == UP) {
            moveUp = false;
        } else if (keyCode == DOWN) {
            moveDown = false;
        } else if (keyCode == ' ') {
            fire = false;
        }
    }

    /**
     * Sets the list of tanks to the specified ArrayList of Tank objects.
     * This method replaces the existing list of tanks with the provided list.
     * 
     * @param tanks The ArrayList of Tank objects to set.
     */
    public void setTanks(ArrayList<Tank> tanks) {
        this.tanks = tanks;
    }

    /**
     * Loads the next level configuration and performs setup for the new level.
     * If there are no more levels left, sets the game to finished state.
     */
    public void loadNextLevel() {
        // Increment the level index to load the next level
        currentLevelIndex++;
        isNextLevle = true;
        if (currentLevelIndex < levels.size()) {
            try {
                // Load the next level configuration
                currentLevel = levels.getJSONObject(currentLevelIndex);
                // Perform setup for the new level
                setup();
            } catch (Exception e) {
                System.err.println("Error loading next level: " + e.getMessage());
            }
        } else {
            // No more levels left, end the game
            // end game and play music
            isGameFinished = true;
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {

        // ----------------------------------
        // display HUD:
        // ----------------------------------
        // TODO
        if (tanks.size() == 1) {
            delay(1000);
            loadNextLevel();
        }
        if (currentLevelIndex > levels.size()) {
            isGameFinished = true;
            util.getWinnerGif(this);
            soundManager.playWinner();
            delay(10000);

        }

        terrain.renderGraphic(this);

        if (currentActiveTank.getFuel() > 0) {
            currentActiveTank.updatePosition(moveLeft, moveRight, moveUp, moveDown, fire, tanks);
        }

        terrain.drawTerrain(this);
        fill(0, 0, 0);
        text(currentActiveTank.getFuel(), 170, 25);
        text("Health : " + currentActiveTank.getHealth(), 400, 50);
        text("Power : " + currentActiveTank.getPowerLevel(), 300, 50);
        text("Wind: " + windMagnitude, 30, 100);
        text(currentActiveTank.getParasuate(), 180, 60);
        text("Player's " + currentActiveTank.getPlayerLabel() + " Turn ", 30, 28);
        fill(0, 408, 612);

        scoreBoard.display(this);

        for (Tank tank : tanks) {
            tank.drawTank(this, soundManager);
        }

        removeTankWithZeroHealth(hitObject);

        if (windMagnitude < 0) {
            util.drawLeftWind(this);
        }
        if (windMagnitude > 0) {
            util.drawRightWind(this);
        }
        util.drawParachute(this);
        util.drawFuel(this);
        healthBar.display(this);

        if (isGameFinished) {
            System.out.println("Please take a Screenshot and Post it on ED if you See This MSG");
            scoreBoard.displayFinalScore(this);
            util.getWinnerGif(this);
            Tank winner = tanksLabel.get(0);
            textSize(20);
            fill(0, 0, 0);
            text("Winner is : " + winner.getPlayerLabel(), 432, 450);
        }

    }

    /**
     * Returns the value of the 'moveLeft' flag.
     *
     * @return The current value of 'moveLeft'.
     */
    public boolean isMoveLeft() {
        return moveLeft;
    }

    /**
     * Returns the value of the 'moveRight' flag.
     *
     * @return The current value of 'moveRight'.
     */
    public boolean isMoveRight() {
        return moveRight;
    }

    /**
     * Returns the value of the 'moveUp' flag.
     *
     * @return The current value of 'moveUp'.
     */
    public boolean isMoveUp() {
        return moveUp;
    }

    /**
     * Returns the value of the 'moveDown' flag.
     *
     * @return The current value of 'moveDown'.
     */
    public boolean isMoveDown() {
        return moveDown;
    }

    /**
     * Returns the value of the 'fire' flag.
     *
     * @return The current value of 'fire'.
     */
    public boolean isFire() {
        return fire;
    }

    /**
     * Sets the 'moveLeft' flag to the specified value.
     *
     * @param moveLeft The boolean value to set for 'moveLeft'.
     */
    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    /**
     * Sets the 'moveRight' flag to the specified value.
     *
     * @param moveRight The boolean value to set for 'moveRight'.
     */
    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    /**
     * Sets the 'moveUp' flag to the specified value.
     *
     * @param moveUp The boolean value to set for 'moveUp'.
     */
    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    /**
     * Sets the 'moveDown' flag to the specified value.
     *
     * @param moveDown The boolean value to set for 'moveDown'.
     */
    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    /**
     * Sets the 'fire' flag to the specified value.
     *
     * @param fire The boolean value to set for 'fire'.
     */
    public void setFire(boolean fire) {
        this.fire = fire;
    }

    /**
     * Sets the index of the current turn.
     * 
     * @param index The integer index to set as the current turn.
     */
    public void setCurrentTurnIndex(int index) {
        this.currentTurnIndex = index;
    }

    /**
     * Sets the levels array for the game configuration.
     * 
     * @param levels The JSONArray representing the game levels.
     */
    public void setLevels(JSONArray levels) {
        this.levels = levels;
    }

    /**
     * Sets the currently active tank.
     * 
     * @param tank The Tank object to set as the currently active tank.
     */
    public void setCurrentActiveTank(Tank tank) {
        this.currentActiveTank = tank;
    }

    /**
     * Sets the tank spawn points.
     * 
     * @param point A map of tank spawn points with player labels as keys and
     *              PVector
     *              objects as values.
     */
    public void setTankSpawnPoints(Map<String, PVector> point) {
        this.tankSpawnPoints = point;
    }

    /**
     * Sets the player color configuration object.
     * 
     * @param playerColor The JSONObject containing player color configurations.
     */
    public void setPlayerColorObject(JSONObject playerColor) {
        this.playerColorsObject = playerColor;
    }

    /**
     * Sets the SoundManager for the application.
     * 
     * This method allows the injection of a SoundManager instance, enabling the
     * application
     * to manage and control sound effects and music. The provided SoundManager will
     * be used
     * for playing, stopping, and managing sounds during the game.
     * 
     * @param soundManager The SoundManager instance to be set for the application.
     */
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Entry point of the application.
     * Launches the Processing sketch by calling PApplet.main() with the specified
     * class name.
     *
     * @param args Command-line arguments (not used in this method).
     */
    public static void main(String[] args) {
        // Launch the Processing sketch by calling PApplet.main() with the specified
        // class name
        PApplet.main("Tanks.App");

    }

}
