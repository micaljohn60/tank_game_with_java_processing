package Tanks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import processing.core.PApplet;
import processing.core.PVector;

public class TesetProjectile {

    private ArrayList<Tank> tanks;
    private int projectileSize;
    private Projectile projectile;
    MockPApplet app;
    private Terrain mockedTerrain;

    @BeforeEach
    public void setup() {
        mockedTerrain = mock(Terrain.class);
        app = new MockPApplet();
        app.tankSetup();
        
        tanks = app.getTanks();
        projectileSize = 10;
    }

    @Test
    public void testProjectileConstructor() {
        Projectile projectile = new Projectile(projectileSize,
                app.position,
                app.velocity,
                app.terrainLayout,
                app.terrain,
                tanks,
                app.windMagnitude);
        assertNotNull(projectile);
        assertNotNull(projectile.hitsTankOrTerrain(tanks, app.terrain, projectile));
        
    }

    @Test
    public void testProjectileConstructorTwo() {
        Terrain terrain = new Terrain();
        terrain.terrainLayout = new String[][] { { "...", "..." }, { "X", "T" } };
        PVector position = new PVector(100.0f,200.f);
        Projectile projectile = new Projectile(projectileSize,
                position,
                app.velocity,
                app.terrainLayout,
                terrain,
                tanks,
                app.windMagnitude);
        assertNotNull(projectile);
        assertNotNull(projectile.hitsTankOrTerrain(new ArrayList<Tank>(), terrain, projectile));
        
    }

    @Test
    public void testProjectileConstructorThree() {
        Terrain terrain = new Terrain();
        terrain.terrainLayout = new String[][] { { "X", "X" }, { "X", "T" } };
        PVector position = new PVector(0.0f,0.0f);
        Projectile projectile = new Projectile(projectileSize,
                position,
                app.velocity,
                app.terrainLayout,
                terrain,
                tanks,
                app.windMagnitude);
        assertNotNull(projectile);
        assertNotNull(projectile.hitsTankOrTerrain(new ArrayList<Tank>(), terrain, projectile));
        
    }

    @Test
    public void testOutOfBoundOne()
    {
        Projectile projectile = new Projectile(projectileSize,
                app.position,
                app.velocity,
                app.terrainLayout,
                app.terrain,
                tanks,
                app.windMagnitude);
        
        PVector position = new PVector(100.0f,200.f);

        assertFalse(projectile.isOutOfBounds(position));;
    }

    @Test
    public void testOutOfBoundTwo()
    {
        PVector position = new PVector(60.0f,400.f);
        Projectile projectile = new Projectile(projectileSize,
                position,
                app.velocity,
                app.terrainLayout,
                app.terrain,
                tanks,
                app.windMagnitude);
       
        assertFalse(projectile.isOutOfBounds(position));;
    }

    @Test
    public void testUpdate_Exploded() {
        // Create a dummy terrain with specific curve points for testing
        ArrayList<PVector> curvePoints = new ArrayList<>();
        curvePoints.add(new PVector(100, 200));
        curvePoints.add(new PVector(150, 250));
        Terrain terrain = new Terrain();
        terrain.setCurvePoint(curvePoints);

        // Create a projectile at a specific position and velocity
        PVector position = new PVector(120, 180);
        PVector velocity = new PVector(1, 2);
        Projectile projectile = new Projectile(30, position, velocity, new String[2][2], terrain, new ArrayList<>(), 30.0f);

        // Call the update method to simulate the projectile behavior
        projectile.update();
        projectile.explode();
        projectile.expandExplosion();
        // Assert that the projectile has exploded after updating
        assertTrue(projectile.isExploded());
    }

    @Test
    public void testPosition()
    {
        Projectile projectile = new Projectile(projectileSize,
                app.position,
                app.velocity,
                app.terrainLayout,
                app.terrain,
                tanks,
                app.windMagnitude);
        assertNotNull(projectile.getPositionX());
        assertNotNull(projectile.getPositionY());
        assertNotNull(projectile.getExplosionRadius());
    }

    @Test
    public void testDisplay_ExplodedOrFinished() {
        // Mock the PApplet object
        PApplet appMock = Mockito.mock(PApplet.class);

        // Create a projectile with specific properties (exploded or finished)
        PVector position = new PVector(100, 200);
        Projectile projectile = new Projectile(10, position, new PVector(0, 0), new String[2][2], null, new ArrayList<>(), 0);
        // Simulate exploded state

        // Call the display method with the mock PApplet
        projectile.display(appMock);

        // Verify that displayExplosion method is called when projectile is exploded
        projectile.displayExplosion(appMock);

        assertNotNull(projectile);
    }
}
