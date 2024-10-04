package Tanks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

/**
 * Track the score of each 
 * player and finalize the score when the game is finished
 */
public class ScoreBoard {

    private Terrain terrain;
    private Map<String, String> players;
    private JSONObject playerColor;
    private ArrayList<Tank> tanks;

    /**
     * Constructs a ScoreBoard instance for managing player scores and colors.
     *
     * @param terrain            The Terrain object representing the game
     *                           environment.
     * @param playerColorsObject The JSON object containing player colors mapping.
     * @param tanks              The list of tanks participating in the game.
     */
    public ScoreBoard(Terrain terrain, JSONObject playerColorsObject, ArrayList<Tank> tanks) {
        this.playerColor = playerColorsObject;
        this.terrain = terrain;
        this.players = new HashMap<>();
        this.tanks = tanks;
        initializePlayers(); // Initialize players based on terrain data
    }

    /**
     * Set the color of each plyaer
     * @param playerColors get RGB value of the player
     */
    public void setPlayerColor(JSONObject playerColors) {
        this.playerColor = playerColors;
    }

    /**
     * update the sorce of current active tank if its
     * hit to the enemy tank
     * @param currentActiveTank instance of the current player
     */
    public void update(Tank currentActiveTank) {
        currentActiveTank.getPlayerLabel();
        currentActiveTank.setScore(currentActiveTank.getScore() + 10);
    }

    /**
     * Initializaw the player base on the layout of the errain
     */
    public void initializePlayers() {
        // Retrieve tank spawn points from the terrain
        Map<String, PVector> spawnPoints = terrain.findTankSpawnPointsTwo();

        // Assign player names based on spawn point labels (A-L)
        char playerLabel = 'A';
        for (Map.Entry<String, PVector> entry : spawnPoints.entrySet()) {
            String playerName = "Player " + playerLabel;
            players.put(entry.getKey(), playerName);
            playerLabel++;
        }
    }

    /**
     * Sort the score of the player
     */
    public void sortScore() {
        Collections.sort(tanks, (t1, t2) -> Integer.compare(t2.getScore(), t1.getScore()));
    }

    /**
     * 
     * This will display final socre of the player
     * whe the game is finised
     * @param applet Instance of the PApplet
     */
    public void displayFinalScore(PApplet applet) {
        sortScore();
        applet.textSize(16);
        applet.textAlign(PApplet.CENTER); // Set text alignment to center horizontally
        int yPos = 290; // Starting Y position for displaying player info

        // Draw a rectangle around the score display area
        applet.stroke(0);
        applet.strokeWeight(3);
        applet.fill(255, 255, 255);
        applet.rectMode(PApplet.CENTER);
        applet.rect(applet.width / 2, 330, 260, tanks.size() * 30 + 20); // Adjust height based on number of players

        // Display player scores centered horizontally
        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            String playerLabel = tank.getPlayerLabel();
            String colorStr = playerColor.getString(playerLabel);
            String[] rgbValues = colorStr.split(",");
            int red = Integer.parseInt(rgbValues[0].trim());
            int green = Integer.parseInt(rgbValues[1].trim());
            int blue = Integer.parseInt(rgbValues[2].trim());

            // Display player label and score centered horizontally
            applet.fill(red, green, blue);
            applet.text("Player " + playerLabel + ": " + tank.getScore(), applet.width / 2, yPos);
            yPos += 30; // Increment Y position for the next player
        }
    }

    /**
     * This will display the currant socre during the run time
     * @param applet Instance of the PApplet
     */
    public void display(PApplet applet) {
        applet.textSize(16);
        applet.fill(255);
        applet.textAlign(PApplet.LEFT);
        int yPos = 50;

        applet.noFill();
        applet.stroke(0, 0, 0);
        applet.strokeWeight(3);
        applet.rect(740, 15, 130, 22);
        applet.text("Score", 700, 20);

        applet.noFill();
        applet.stroke(0, 0, 0);
        applet.strokeWeight(3);
        applet.rect(740, 90, 130, 130);
        // Initial Y position for displaying player info

        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            String playerLabel = tank.getPlayerLabel();
            String colorStr = playerColor.getString(playerLabel);
            String[] rgbValues = colorStr.split(",");
            int red = Integer.parseInt(rgbValues[0].trim());
            int green = Integer.parseInt(rgbValues[1].trim());
            int blue = Integer.parseInt(rgbValues[2].trim());

            // Display player label and name on the scoreboard
            applet.fill(red, green, blue);
            applet.text("Player " + playerLabel + ": " + tank.getScore(), 700, yPos);
            yPos += 30; // Increment Y position for the next player
        }
    }

    /**
     *  Method to update the scoreboard (e.g., when a player's name changes)
     * @param playerLabel Player Label A,B,C,D
     * @param playerName Player Label A,B,C,D // I don't know why i need this i am afraid to remove this :(
     */
    public void updateScoreboard(String playerLabel, String playerName) {
        players.put(playerLabel, playerName);
    }

}
