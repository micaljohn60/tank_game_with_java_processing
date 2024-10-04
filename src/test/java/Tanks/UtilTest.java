package Tanks;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import Tanks.interfaces.TankCallBack;
import processing.core.PApplet;
import processing.core.PVector;


public class UtilTest {

    private Terrain mockedTerrain;
    private TankCallBack mockedTurnCallback;
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
    MockPApplet app;

    @BeforeEach
    public void setup() {
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

        tank = new Tank(playerLabel, maxHealth, tankSize, x, y, color, terrainLayout,
                curvePoints, mockedTerrain, mockedTurnCallback, windMagnitude);
    }

    @Test
    public void utilityTest() {
        PApplet appMock = Mockito.mock(PApplet.class);
        SoundManager soundManager = Mockito.mock(SoundManager.class);
        Util util = new Util();

        tank.setScore(100);
        util.drawFuel(appMock);
        util.drawLeftWind(appMock);
        util.drawRightWind(appMock);
        util.drawParachute(appMock);
        util.getWinnerGif(appMock);
        util.buyThings(tank, soundManager, "fuel");
        util.buyThings(tank, soundManager, "parasuate");
        boolean result = util.repairTank(tank);
        assertTrue(result);
    }


}
