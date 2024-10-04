package Tanks;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import processing.core.PApplet;
import processing.core.PVector;

public class HealthBarTest {

    @Test
    public void testHealtBar() {
        // Mock the PApplet object
        PApplet appMock = Mockito.mock(PApplet.class);
        HealthBar healthBar = new HealthBar(0, 0, 0, 0, new int[] {0,0,0}, new int[] {0,0,0}, 0);
        healthBar.display(appMock);
        assertNotNull(healthBar);
    }
}
