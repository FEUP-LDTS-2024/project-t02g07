package HollowKnight.model.game.elements.enemies;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;

import static java.lang.Math.max;

import java.util.Random;

public class GhostMonster extends Enemies {

    private double amplitude;      // Controls the height of the wave
    private double frequency;      // Controls how fast the wave oscillates
    private double horizontalSpeed; // Fixed horizontal speed
    private double elapsedTime;          // Time tracker for the wave movement
    private final int screenWidth;       // Width of the game screen
    private final int screenHeight;      // Height of the game screen

    private final char symbol;

    public GhostMonster(int x, int y, int HP, Scene scene, int damage, Position size, char symbol) {
        super(x, y, HP, scene, damage, size);
        this.symbol = symbol;
        Random random = new Random();

        // Randomize amplitude (height of sine wave) and frequency
        this.amplitude = 1.5 + random.nextDouble(); // 1.5 to 2.5
        this.frequency = 0.05 + (random.nextDouble() * 0.05); // 0.05 to 0.1 (wave speed)
        this.horizontalSpeed = 1.25 + (0.75 * random.nextDouble()); // Speed: 1.25 to 2
        this.elapsedTime = 0.0;

        // Set screen boundaries (replace with your scene dimensions)
        this.screenWidth = scene.getWidth();
        this.screenHeight = scene.getHeight();

        // Set initial velocity
        setVelocity(new Vector(horizontalSpeed, 0));
    }

    @Override
    public char getChar() {
        return symbol;
    }

    public void setAmplitude(int amplitude) {this.amplitude = amplitude;}
    public void setFrequency(double freq) {this.frequency = freq;}
    public void setHorizontalSpeed(int hspeed) {this.horizontalSpeed = hspeed;}



    @Override
    public Position updatePosition() {
        // Increment time for sine wave calculations
        elapsedTime += 1;

        // Horizontal movement
        double newX = getPosition().x() + horizontalSpeed;

        // Vertical movement: sine wave function
        double newY = getPosition().y() + amplitude * Math.sin(frequency * elapsedTime);

        // Screen wrapping: If the enemy moves off the screen, wrap around
        if (newX > screenWidth) newX = 0; // Wrap from right to left
        if (newX < 0) newX = screenWidth; // Wrap from left to right
        if (newY > screenHeight) newY = 0; // Wrap from bottom to top
        if (newY < 0) newY = screenHeight; // Wrap from top to bottom

        return new Position(newX, newY);
    }

    @Override
    public Position moveMonster() {
        Position newPosition = updatePosition();
        setPosition(newPosition);
        return newPosition;
    }

    @Override
    protected Vector applyCollisions(Vector velocity) {
        // Disable collision logic for wave-like movement
        return velocity;
    }
}
