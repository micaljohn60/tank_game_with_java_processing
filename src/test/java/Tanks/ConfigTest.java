package Tanks;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class ConfigTest {

    @Test
    public void testConfigConstructor() throws IOException {
       
        PApplet app = new MockPApplet();
        String configPath = "config.json";
        Config config = new Config(app, configPath);
        JSONArray levels = config.getLevels();
        JSONObject playerColors = config.getPlayerColors();

        assertNotNull(levels, "Levels array should not be null");
        assertNotNull(playerColors, "Player colors object should not be null");
    }

    // MockPApplet class for testing (simplified implementation)
    private static class MockPApplet extends PApplet {
        
        @Override
        public JSONObject loadJSONObject(String filename) {
           
            JSONObject dummyData = new JSONObject();
            dummyData.setJSONArray("levels", new JSONArray());
            JSONObject playerColors = new JSONObject();
            playerColors.setInt("A", 255);
            playerColors.setInt("B", 200); 
            dummyData.setJSONObject("player_colours", playerColors);
            return dummyData;
        }
    }

}