package Tanks;

import processing.core.PApplet;
import processing.core.PVector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import processing.event.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ddf.minim.Minim;


public class AppTest extends PApplet {

    private Minim minim;

    @BeforeEach
    public void setup()
    {
        MockPApplet mockPApplet = new MockPApplet();
        minim = new Minim(mockPApplet);
        mockPApplet.tankSetup();
    }
    

        @Test
        public void AppTest()
        {
        PApplet appMock = mock(PApplet.class);
        //SoundManager sm = mock(SoundManager.class);

        MockPApplet mockPApplet = new MockPApplet();
        appMock.frameRate(30);
        
        //sm.setMinim(minim);

        App app = new App();
       
        Terrain terrain = new Terrain();
        app.setTerrain(terrain);
        terrain.drawTree(appMock, 0, 0);
        terrain.foregroundColor = "255,255,255";
        ArrayList<PVector> curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(100, 200));
        curvePoints.add(new PVector(150, 250));
        terrain.renderGraphic(appMock);
        mockPApplet.tankSetup();

        app.setTanks(mockPApplet.getTanks());
  
        app.removeTankWithZeroHealth("A");
        mockPApplet.loadJSONObject("config.json");
        // app.playerColorsObject = mockPApplet.getPlayerJsonObject();
        app.setPlayerColorObject(mockPApplet.getPlayerJsonObject());
        //app.levels = mockPApplet.getLevels();
        app.setLevels(mockPApplet.getLevels());
        //app.currentTurnIndex = 0;
        app.setCurrentTurnIndex(0);
        app.setCurrentActiveTank(mockPApplet.getTank());
        //app.currentActiveTank = mockPApplet.getTank();
        app.tankIsHit("E", 10,mockPApplet.getTank());
        app.setTankSpawnPoints(mockPApplet.getTankSpawnPoints());
        //app.tankSpawnPoints = mockPApplet.getTankSpawnPoints();
        app.spawnTanks();
        app.endTurn("Terrain", 10);
        app.setCurrentActiveTank(null);
        PApplet.runSketch(new String[] {"Tanks.app"}, app);
        app.delay(10000);

        assertNotNull(app.parsePlayerColors());

    }


    @Test
    public void testKeyPressed() {

        App appUnderTest = new App();
        MockPApplet mockApp = new MockPApplet();
        SoundManager smMock = mock(SoundManager.class);

        appUnderTest.setSoundManager(smMock);
        appUnderTest.setCurrentActiveTank(mockApp.getTank());
        appUnderTest.keyCode = LEFT;
        
        KeyEvent event = new KeyEvent(smMock, 0, 0, 0, (char) 37, 37);
        appUnderTest.keyPressed(event);
              
        assertNotNull(appUnderTest.CELLSIZE);
        verify(smMock).playTankMovingSOund(); // Verify expected sound play

        // Simulate key press RIGHT
        appUnderTest.keyCode = RIGHT;
        KeyEvent rightEvent = new KeyEvent(smMock, 0, 0, 0, (char) 39, 39);
        appUnderTest.keyPressed(rightEvent);
        appUnderTest.setMoveLeft(true);
        assertTrue(appUnderTest.isMoveLeft());

        appUnderTest.setMoveRight(true);
        assertTrue(appUnderTest.isMoveRight());
        verify(smMock, times(2)).playTankMovingSOund(); // Verify sound play again

        // Simulate key press SPACE
        appUnderTest.keyCode = ' ';
        KeyEvent fire = new KeyEvent(smMock, 0, 0, 0, (char) 32, 32);
        appUnderTest.keyPressed(fire);
        appUnderTest.setFire(true);
        assertTrue(appUnderTest.isFire());
        verify(smMock).playFireSound(); // Verify fire sound play

        KeyEvent powerUP = new KeyEvent(smMock, 0, 0, 0, (char) 87, 87);
        appUnderTest.keyPressed(powerUP);

        KeyEvent powerDown = new KeyEvent(smMock, 0, 0, 0, (char) 83, 83);
        appUnderTest.keyPressed(powerDown);

        KeyEvent largerProjectile = new KeyEvent(smMock, 0, 0, 0, (char) 88, 88);
        appUnderTest.keyPressed(largerProjectile);

        KeyEvent shield = new KeyEvent(smMock, 0, 0, 0, (char) 72, 72);
        appUnderTest.keyPressed(shield);

        KeyEvent repairKit = new KeyEvent(smMock, 0, 0, 0, (char) 82, 82);
        appUnderTest.keyPressed(repairKit);

        KeyEvent buyParasuate = new KeyEvent(smMock, 0, 0, 0, (char) 80, 80);
        appUnderTest.keyPressed(buyParasuate);


        KeyEvent buyFuel = new KeyEvent(smMock, 0, 0, 0, (char) 70, 70);
        appUnderTest.keyPressed(buyFuel);

        // KeyEvent justTest = new KeyEvent(smMock, 0, 0, 0, (char) 78, 78);
        // appUnderTest.keyPressed(justTest);

        KeyEvent moveUP = new KeyEvent(smMock, 0, 0, 0, (char) 38, 38);
        appUnderTest.keyPressed(moveUP);
        appUnderTest.setMoveUp(true);
        assertTrue(appUnderTest.isMoveUp());

        KeyEvent moveDown = new KeyEvent(smMock, 0, 0, 0, (char) 40, 40);
        appUnderTest.keyPressed(moveDown);
        appUnderTest.setMoveDown(true);
        assertTrue(appUnderTest.isMoveDown());

        appUnderTest.keyCode = LEFT;
        appUnderTest.keyReleased();
        appUnderTest.setMoveLeft(false);
        assertFalse(appUnderTest.isMoveLeft());

        // Simulate key press RIGHT
        appUnderTest.keyCode = RIGHT;
        appUnderTest.keyReleased();
        appUnderTest.setMoveRight(false);
        assertFalse(appUnderTest.isMoveRight());

        appUnderTest.keyCode = UP;
        appUnderTest.keyReleased();
        appUnderTest.setMoveUp(false);
        assertFalse(appUnderTest.isMoveUp());

        appUnderTest.keyCode = DOWN;
        appUnderTest.keyReleased();
        appUnderTest.setMoveDown(false);
        assertFalse(appUnderTest.isMoveDown());

        appUnderTest.keyCode = ' ';
        appUnderTest.keyReleased();
        appUnderTest.setFire(false);
        assertFalse(appUnderTest.isFire());

    }

}
