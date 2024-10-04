package Tanks;

import processing.core.PApplet;

import org.checkerframework.checker.units.qual.min;

import ddf.minim.*;
import java.net.URL;

/**
 * Responsiable for managing the audio files
 */
public class SoundManager{

    private Minim minim;
    private AudioSample fireSound;
    private AudioSample tankMovingSound;
    private AudioSample explosionSound;
    private AudioSample errorSound;
    private AudioSample winnderVFX;

    /**
     * Default Constructor
     */
    public SoundManager(){}

    /**
     * Construct a new SoundManager with initial parameter
     * @param parent Instance of the PApplet
     */
    public SoundManager(PApplet parent){

        minim = new Minim(parent);
        this.fireSound = minim.loadSample("src/main/resources/vfxs/mortor_vfx_1.mp3");
        this.tankMovingSound = minim.loadSample("src/main/resources/vfxs/tank_moving_sound.mp3");
        this.explosionSound = minim.loadSample("src/main/resources/vfxs/explosion_vfx_1.mp3");
        this.errorSound = minim.loadSample("src/main/resources/vfxs/error_vfx.mp3");
        this.winnderVFX = minim.loadSample("src/main/resources/vfxs/winner_vfx.mp3");
    }

    /**
     * trigger the explosion VFX
     */
    public void playExplosionSound() {
        this.explosionSound.trigger();
    }

    /**
     * trigger the easter VFX
     */
    public void playWinner(){
        this.winnderVFX.trigger();
    }

    /**
     * Stop the easter VFX
     */
    public void stopWinner()
    {
        this.winnderVFX.close();
    }

    /**
     * trigger the erround sound
     */
    public void playErrorSound() {
        this.errorSound.trigger();
    }

    /**
     * trigger the tank moving sound
     */
    public void playTankMovingSOund() {
        this.tankMovingSound.trigger();
    }

    /**
     * trigger the tank fire
     */
    public void playFireSound()
    {
        this.fireSound.trigger();
    }

    /**
     * stop the tank mvoing VFX
     */
    public void stopTankMoving()
    {
        tankMovingSound.stop();
        
    }

    /**
     * Dispose the VFX to save memory
     */
    public void dispose() {
        // Dispose of Minim resources;
        explosionSound.close();
        tankMovingSound.close();
        minim.stop();
    }

    
}
