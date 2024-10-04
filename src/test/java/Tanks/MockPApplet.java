package Tanks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mock;

import Tanks.interfaces.TankCallBack;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

class MockPApplet extends PApplet {

    public Tank tank;
    public Tank tank1;
    public Tank tank2;
    public Tank tank3;
    public String playerLabel;
    public int maxHealth;
    public float tankSize ;
    public float x;
    public float y;
    public int[] color;
    public String[][] terrainLayout;
    public ArrayList<PVector> curvePoints;
    public ArrayList<Tank> tanks;
    public float windMagnitude;
    public PApplet app;
    public PVector position;
    public PVector velocity;
    public Terrain terrain;
    public JSONObject playerColors;
    public SoundManager soundManager;
    public Terrain mockedTerrain;
    public TankCallBack mockedTurnCallback;
    public JSONObject mockedJSONObject;
    private JSONArray levelsArray;
    private Map<String, PVector> tankSpawnPoints;

        
    @Override
    public JSONObject loadJSONObject(String filename) {
        JSONObject dummyData = new JSONObject();
        position = new PVector(100.0f, 200.0f);
        velocity = new PVector(10.0f, 5.0f);
        terrain = new Terrain();

        levelsArray = new JSONArray();

        // Level 1
        JSONObject level1 = new JSONObject();
        level1.setString("layout", "level1.txt");
        level1.setString("background", "snow.png");
        level1.setString("foreground-colour", "255,255,255");
        level1.setString("trees", "tree2.png");
        levelsArray.append(level1);

        

        // Level 2
        JSONObject level2 = new JSONObject();
        level2.setString("layout", "level2.txt");
        level2.setString("background", "desert.png");
        level2.setString("foreground-colour", "234,221,181");
        levelsArray.append(level2);

        // Level 3
        JSONObject level3 = new JSONObject();
        level3.setString("layout", "level3.txt");
        level3.setString("background", "basic.png");
        level3.setString("foreground-colour", "120,171,0");
        level3.setString("trees", "tree1.png");
        levelsArray.append(level3);

        dummyData.setJSONArray("levels", levelsArray);

        // Additional data (example: player colors)
        JSONObject playerColors = new JSONObject();
        playerColors.setInt("A", 255);
        playerColors.setInt("B", 200);
        dummyData.setJSONObject("player_colours", playerColors);

        return dummyData;
      
    }

    public Map<String, PVector> getTankSpawnPoints()
    {
        return this.tankSpawnPoints;
    }

    public JSONArray getLevels()
    {
        return this.levelsArray;
    }

    public void tankSetup(){
        position = new PVector(100.0f, 200.0f);
        velocity = new PVector(10.0f, 5.0f);
        terrain = new Terrain();

        tankSpawnPoints = new HashMap<>();

        // Add sample tank spawn points to the map
        tankSpawnPoints.put("A", new PVector(100, 150)); // Example position (100, 100)
        tankSpawnPoints.put("B", new PVector(200, 300)); // Example position (200, 300)
        tankSpawnPoints.put("C", new PVector(400, 200)); // Example position (400, 200)
     
        playerColors = new JSONObject();
        playerColors.put("A", "0,0,255");
        playerColors.put("B", "255,0,0");
        playerColors.put("C", "0,255,255");
        playerColors.put("D", "255,255,0");
        playerColors.put("E", "0,255,0");
        terrain = new Terrain();
        playerLabel = "A";
        maxHealth = 100;
        tankSize = 20.0f;
        x = 100.0f;
        y = 200.0f;
        color = new int[] {255, 0, 0};
        terrainLayout = new String[][] {{"...", "..."}, {"...", "..."}};
        curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(50.0f, 150.0f));
        curvePoints.add(new PVector(100.0f, 200.0f)); 
        curvePoints.add(new PVector(150.0f, 250.0f)); 
        windMagnitude = 30.0f;


    }

    public JSONObject getPlayerJsonObject()
    {
        return this.playerColors;
    }

    public Tank getTank()
    {
        tank = new Tank("E", 0, tankSize, x, y, color, terrainLayout,
                             curvePoints, mockedTerrain, mockedTurnCallback, windMagnitude);
        return tank;
    }

    public ArrayList<Tank> getTanks()
    {
        tank = new Tank("E", maxHealth, tankSize, x, y, color, terrainLayout,
                             curvePoints, mockedTerrain, mockedTurnCallback, windMagnitude);
        
        tank1 = new Tank("A", 0, 20.0f, 100.0f, 200.0f, new int[]{255, 0, 0}, terrainLayout, curvePoints, null, null, 30.0f);
        tank1.setHealth(0);
        tank2 = new Tank("B", 100, 20.0f, 150.0f, 250.0f, new int[]{0, 255, 0}, terrainLayout, curvePoints, null, null, 30.0f);
        tank3 = new Tank("C", 100, 20.0f, 200.0f, 300.0f, new int[]{0, 0, 255}, terrainLayout, curvePoints, null, null, 30.0f);

        tanks = new ArrayList<Tank>();
        tanks.add(tank);
        tanks.add(tank1);
        tanks.add(tank2);
        tanks.add(tank3);

        return this.tanks;
    }
}
