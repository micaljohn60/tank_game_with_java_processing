package Tanks;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import Tanks.interfaces.TankCallBack;
import edu.emory.mathcs.backport.java.util.Arrays;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class TankTest extends PApplet {

    private Terrain mockedTerrain;
    private TankCallBack mockedTurnCallback;
    private JSONObject mockedJSONObject;
    private Tank tank;
    private Tank tank1;
    private Tank tank2;
    private Tank tank3;
    private String playerLabel;
    private int maxHealth;
    private float tankSize;
    private float x;
    private float y;
    private int[] color;
    private String[][] terrainLayout;
    private ArrayList<PVector> curvePoints;
    private ArrayList<Tank> tanks;
    private float windMagnitude;
    private int projectileSize;
    MockPApplet app;

    @BeforeEach
    public void setup() {

        // Mock loadJSONObject behavior

        mockedTerrain = mock(Terrain.class);
        app = new MockPApplet();
        app.tankSetup();

        tanks = app.getTanks();
        projectileSize = 10;

        // Setup player_colours JSON data
        JSONObject playerColors = new JSONObject();
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

        tank = new Tank(playerLabel, maxHealth, tankSize, x, y, color, terrainLayout,
                curvePoints, mockedTerrain, mockedTurnCallback, windMagnitude);

        tank1 = new Tank("A", 100, 20.0f, 100.0f, 200.0f, new int[] { 255, 0, 0 }, terrainLayout, curvePoints,
                null, null, 30.0f);
        tank2 = new Tank("B", 100, 20.0f, 150.0f, 250.0f, new int[] { 0, 255, 0 }, terrainLayout, curvePoints,
                null, null, 30.0f);
        tank3 = new Tank("C", 100, 20.0f, 200.0f, 300.0f, new int[] { 0, 0, 255 }, terrainLayout, curvePoints,
                null, null, 30.0f);

        tanks = new ArrayList<Tank>();
        tanks.add(tank);
        tanks.add(tank1);
        tanks.add(tank2);
        tanks.add(tank3);
    }

    @Test
    public void testTankConstructor() {
        PApplet appMock = Mockito.mock(PApplet.class);
        SoundManager smMock = mock(SoundManager.class);
    
        ArrayList<PVector> curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(100, 200));
        curvePoints.add(new PVector(150, 250));
        Terrain terrain = new Terrain();
        terrain.setCurvePoint(curvePoints);

        // Create a projectile at a specific position and velocity
        PVector position = new PVector(120, 180);
        PVector velocity = new PVector(1, 2);
        Projectile projectile = new Projectile(30,
                position, velocity,
                new String[2][2],
                terrain,
                tanks,
                30.0f);

        tank.mytanks = tanks;
        projectile.explode();
        tank.setProjectile(projectile);
        tank.isHit(position);
        TankCallBack tcb = new TankCallBack() {

            @Override
            public int endTurn(String hello, int health) {
                // TODO Auto-generated method stub
                return 0;
            }
            
        };
        tank.turnCallback = tcb;
        tank.drawTank(appMock, smMock);
        tank.setParasuate(1);
        

        // Ensure Tank attributes are initialized correctly
        assertEquals(playerLabel, tank.getPlayerLabel());
        assertEquals(maxHealth, tank.getHealth());
        assertEquals(x, tank.getPosition().x);
        assertEquals(y, tank.getPosition().y);
        assertArrayEquals(color, tank.getColor());
        assertArrayEquals(terrainLayout, tank.terrainLayout);
        assertFalse(tank.getLargetProjectile()); // Default value should be false
        assertNotNull(tank.getFuel()); // Ensure fuel is initialized
    }

    @Test
    public void testTankConstructoThree() {
        PApplet appMock = Mockito.mock(PApplet.class);
        SoundManager smMock = mock(SoundManager.class);
    
        ArrayList<PVector> curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(100, 200));
        curvePoints.add(new PVector(150, 250));
        Terrain terrain = new Terrain();
        terrain.setCurvePoint(curvePoints);

        // Create a projectile at a specific position and velocity
        PVector position = new PVector(0, 0);
        PVector velocity = new PVector(1, 2);
        Projectile projectile = new Projectile(30,
                position, velocity,
                new String[2][2],
                terrain,
                tanks,
                30.0f);

        tank.mytanks = tanks;
        projectile.explode();
        tank.setProjectile(projectile);
        tank.isHit(position);
        TankCallBack tcb = new TankCallBack() {

            @Override
            public int endTurn(String hello, int health) {
                // TODO Auto-generated method stub
                return 0;
            }
            
        };
        tank.turnCallback = tcb;
        
        tank.drawTank(appMock, smMock);
        smMock.playErrorSound();
        

     
        assertNotNull(tank.getFuel()); // Ensure fuel is initialized
    }

    @Test
    public void testSetLargerProjectile() {
        // Instantiate Tank using mock data

        // Set larger projectile and verify
        tank.setLargetProjectile(true);
        assertTrue(tank.getLargetProjectile());
    }

    @Test
    public void testDamage() {

        // tank.takeDamage(250);
        assertEquals(0, tank.takeDamage(250));

    }

    @Test
    public void testUpdatePosition() {
        tank.updatePosition(true, false, false, false, false, tanks);
        tank.updatePosition(false, true, false, false, false, tanks);
        tank.updatePosition(false, false, true, false, false, tanks);
        tank.updatePosition(false, false, false, true, false, tanks);
        tank.updatePosition(false, false, false, false, true, tanks);
        tank.setLargetProjectile(true);
        tank.updatePosition(false, false, false, false, true, tanks);

        assertNotNull(tank);
    }

    @Test
    public void testScore() {
        tank.setScore(100);
        tank.getScore();
        assertTrue(tank.isAlive());

    }

    @Test
    public void testInterpolsteTankPositin() {
        assertNotNull(tank.interpolateTankPosition(30.0f));
    }

    @Test
    public void testDrawShield()
    {
        PApplet appMock = Mockito.mock(PApplet.class);
        tank.activateShield();
        tank.drawShield(appMock);
    }

    @Test
    public void testGetHit() {
        tank.setHit(true);
        assertTrue(tank.getHit());
    }

    @Test
    public void testFindPlayerLabel() {
        Tank resultTank = tank.findTankByLabel("B", tanks);
        // Assert that the returned tank object matches the expected tank with the
        // specified label
        assertNotNull(resultTank); // Assert that a tank with the specified label is found

        assertEquals("B", resultTank.getPlayerLabel()); // Assert that the tank's label matches the expected label

        assertEquals(150.0f, resultTank.getPosition().x, 0.01); // Assert that the tank's x-coordinate matches the
                                                                // expected value
        assertEquals(250.0f, resultTank.getPosition().y, 0.01); // Assert that the tank's y-coordinate matches the
                                                                // expected value
        assertArrayEquals(new int[] { 0, 255, 0 }, resultTank.getColor());
    }

    @Test
    public void testIsHit() {

        // Define a projectile position to test with
        PVector projectilePosition = new PVector(100, 200); // Adjust coordinates as needed

        // Call the isHit method and capture the result
        boolean result = tank.isHit(projectilePosition);
        tank.handleHit();

        assertTrue(result); 
    }

    @Test
    public void testFuel() {
        tank.setFuel(250);
        assertNotNull(tank.getFuel(), "Fuel is getting Null");
    }

    @Test
    public void testShield() {
        tank.activateShield();
        assertTrue(tank.hasActiveShield());
    }

    @Test
    public void testConsumeFuel() {
        tank.setFuel(250);
        tank.consumeFuel(30);
        assertEquals(220, tank.getFuel());
    }

    @Test
    public void testFuelZero() {
        tank.setFuel(-10);
        tank.consumeFuel(30);
        assertEquals(0, tank.getFuel());
    }

    @Test
    public void testShieldHit() {
        tank.activateShield();
        tank.handleHit();
    }

    @Test
    public void testPowerLevelUp() {
        tank.setPowerLevelUp(40);
        assertNotNull(tank.getPowerLevel(), "Power Level is Null");
    }

    @Test
    public void testPowerLevelDown() {
        tank.setPowerLevelDown(50);
        assertNotNull(tank.getPowerLevel(), "Power Level is Null");
    }

}
