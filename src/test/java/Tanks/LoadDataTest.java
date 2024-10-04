package Tanks;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class LoadDataTest {
    
    @Test
    public void testLoadDataConstructor() throws IOException
    {
        PApplet app = new MockPApplet();
        String configPath = "config.json";
        Config config = new Config(app, configPath);
        JSONArray levels = config.getLevels();
        
        JSONObject levelData = levels.getJSONObject(0);
        String levelFile = levelData.getString("layout");
        
        LoadData loadData = new LoadData(levelFile);
        assertNotNull(loadData,"File Path is Null or File is not Exist");

    }

    @Test
    public void testLoadTerrain() throws IOException
    {
        PApplet app = new MockPApplet();
        String configPath = "config.json";
        Config config = new Config(app, configPath);
        JSONArray levels = config.getLevels();
        
        JSONObject levelData = levels.getJSONObject(0);
        String levelFile = levelData.getString("layout");
        
        LoadData loadData = new LoadData(levelFile);

        String[][] loadTerrainData = loadData.loadTerrain(0);

        assertNotNull(loadTerrainData, "Terrain return null");
    }

   
}
