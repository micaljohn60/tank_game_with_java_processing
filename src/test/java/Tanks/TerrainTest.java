package Tanks;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class TerrainTest {
    

    @Test
    public void testIsHit()
    {
        String[][] terrainLayout = new String[][] { { "...", "..." }, { "X", "..." } };
        PVector position = new PVector(10.0f, 10.0f);
        Terrain terrain = new Terrain();
        terrain.terrainLayout = terrainLayout;
        assertNotNull(terrain.isHit(position));    
    }

    @Test
    public void testIsHitTwo()
    {
        String[][] terrainLayout = new String[][] { { "...", "..." }, { "X", "..." } };
        PVector position = new PVector(100.0f, 100.0f);
        Terrain terrain = new Terrain();
        terrain.terrainLayout = terrainLayout;
        assertFalse(terrain.isHit(position));  
    }

    @Test
    public void findTankSpwanPoint()
    {
        String[][] terrainLayout = new String[][] { { "A", "..." }, { "B", "..." } };
        Terrain terrain = new Terrain();
        terrain.terrainLayout = terrainLayout;
        assertNotNull(terrain.findTankSpawnPointsTwo());  
    }

    @Test
    public void testGenerateCurvePoint()
    {
        String[][] terrainLayout = new String[][] { { "...", "..." }, { "X", "..." } };
        PVector position = new PVector(100.0f, 100.0f);
        Terrain terrain = new Terrain();
        terrain.terrainLayout = terrainLayout;
        assertNotNull(terrain.generateCurvePoints(terrainLayout, 860, 640));  
    }

    @Test
    public void testGraphic()
    {
        PApplet appMock = Mockito.mock(PApplet.class);
        Terrain terrain = new Terrain();
        terrain.drawTree(appMock, 0, 0);
        terrain.foregroundColor = "255,255,255";
        ArrayList<PVector> curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(100, 200));
        curvePoints.add(new PVector(150, 250));
        terrain.renderGraphic(appMock);
        terrain.setWidth(864);
        terrain.setHeight(640);
        terrain.curvePoints = curvePoints;
        terrain.terrainLayout =  new String[][] { { "...", "..." }, { "X", "T" } };
        terrain.drawTerrain(appMock);

        assertNotNull(terrain.terrainLayout);
    }
      
}
