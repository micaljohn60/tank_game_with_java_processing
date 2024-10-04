package Tanks;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import Tanks.interfaces.TankCallBack;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class TestScoreBoard {

    private Tank tank;
    private String playerLabel;
    private int maxHealth;
    private float tankSize;
    private float x;
    private float y;
    private int[] color;
    private String[][] terrainLayout;
    private ArrayList<PVector> curvePoints;
    private float windMagnitude;
    private TankCallBack mockedTurnCallback;
    MockPApplet app;
    
    @Test
    public void testScoreBoard()
    {
        Terrain mockedTerrain = mock(Terrain.class);
        
        Terrain terrain = new Terrain();
        PApplet appMock = Mockito.mock(PApplet.class);
        JSONObject playerColors = new JSONObject();
        MockPApplet mockData = new MockPApplet();
        ArrayList<Tank> tanks = mockData.getTanks();

        playerColors.put("A", "0,0,255");
        playerColors.put("B", "255,0,0");
        playerColors.put("C", "0,255,255");
        playerColors.put("D", "255,255,0");
        playerColors.put("E", "0,255,0");

        playerLabel = "A";
        maxHealth = 100;
        tankSize = 20.0f;
        x = 100.0f;
        y = 200.0f;
        color = new int[] { 255, 0, 0 };
        terrainLayout = new String[][] { { "...", "..." }, { "...", "..." } };
        curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(50.0f, 150.0f));
        curvePoints.add(new PVector(100.0f, 200.0f));
        curvePoints.add(new PVector(150.0f, 250.0f));
        windMagnitude = 30.0f;

        terrain.terrainLayout = terrainLayout;

        ScoreBoard scoreBoard = new ScoreBoard(terrain, playerColors, tanks);
        scoreBoard.setPlayerColor(playerColors);
        scoreBoard.update(tanks.get(0));
        scoreBoard.updateScoreboard(playerLabel, playerLabel);
        scoreBoard.display(appMock);
        scoreBoard.displayFinalScore(appMock);

        assertNotNull(curvePoints);

    }
}
