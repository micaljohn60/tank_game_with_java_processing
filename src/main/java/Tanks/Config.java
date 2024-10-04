package Tanks;

import java.io.IOException;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Represents a configuration object containing JSON data for game settings.
 * This class encapsulates JSON objects and arrays used to configure game levels
 * and player colors.
 */
public class Config {
    private JSONObject json;
    private JSONArray levels;
    private JSONObject playerColors;

    /**
     * Constructs a new Config object with specified JSON data.
     * 
     * @param app         pass the PApplet to constructor
     * @param configPath  config.json path
     * @throws IOException If an error occurs while reading the layout file.
     */
    public Config(PApplet app, String configPath) throws IOException {

        this.json = app.loadJSONObject(configPath);
        this.levels = json.getJSONArray("levels");
        this.playerColors = json.getJSONObject("player_colours");
    }

    /**
     * Retrieves the JSON object containing player colors configuration.
     * @return The JSON object representing player colors configuration.
     */
    public JSONObject getPlayerColors() {
        return this.playerColors;
    }

    /**
     * Retrieves the JSONArray containing configurations for game levels.
     * @return The JSONArray representing game levels configurations.
     */
    public JSONArray getLevels() {
        return this.levels;
    }

}
