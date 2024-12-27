package HollowKnight.controller.menu;

import HollowKnight.Game;
import HollowKnight.controller.Controller;
import HollowKnight.gui.GUI;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.menu.Menu;
import HollowKnight.model.menu.Particle;
import HollowKnight.state.particle.*;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;
import java.util.Random;

public class ParticleMenuController extends Controller<Menu> {
    private double windAngle = 0; // Current wind direction in radians
    private double windSpeed = 2; // Current wind speed
    private final int screenWidth = 220;
    private final int screenHeight = 110;
    private final Random random = new Random();

    // Colors for the gradient
    TextColor.RGB currentStartColor = randomColor();
    TextColor.RGB currentEndColor = randomColor();
    TextColor.RGB nextStartColor = randomColor();
    TextColor.RGB nextEndColor = randomColor();
    private int transitionStartTick = -1; // Tick when the transition begins

    public ParticleMenuController(Menu menu) {
        super(menu);
    }

    public double getWindAngle() {
        return windAngle;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public Position wrapPosition(int x, int y) {
        if (x < 0) x = screenWidth - 1;
        else if (x >= screenWidth) x = 1;
        if (y < 0) y = screenHeight - 1;
        else if (y >= screenHeight) y = 1;
        return new Position(x, y);
    }

    @Override
    public void move(Game game, GUI.ACTION action, long time) throws IOException {
        int modeDuration = 50; // Number of ticks before switching mode
        int mode = (int) ((time / modeDuration) % 5); // Cycle through 4 modes

        for (Particle particle : getModel().getParticles()) {
            // Update particle state based on the current mode
            ParticleState state;
            switch (mode) {
                case 0 -> state = new RandomState();      // Random mode
                case 1 -> state = new WindyState();       // Windy mode
                case 2 -> state = new CalmState();        // Calm mode
                case 3 -> state = new DispersingState();  // Dispersing mode
                case 4 -> state = new ZicoState();
                default -> throw new IllegalStateException("Unexpected mode: " + mode);
            }
            particle.setState(state);

            // Delegate movement logic to the particle's state
            Position newPosition = particle.getState().move(particle, time, this);
            particle.setPosition(newPosition);


        }
        updateGradients(time);

    }

    void updateGradients(long time) {
        // Trigger gradient change periodically
        // Change gradient every 500 ticks
        int gradientChangeInterval = 500;
        if (time % gradientChangeInterval == 0) {
            transitionStartTick = (int) time; // Start the gradient transition
            nextStartColor = randomColor();
            nextEndColor = randomColor();
        }

        // Smoothly interpolate the gradient during the transition period
        // Number of frames for smooth transition
        int gradientTransitionFrames = 100;
        if (transitionStartTick >= 0 && time < transitionStartTick + gradientTransitionFrames) {
            float transitionFactor = (float) (time - transitionStartTick) / gradientTransitionFrames;
            currentStartColor = interpolateColor(currentStartColor, nextStartColor, transitionFactor);
            currentEndColor = interpolateColor(currentEndColor, nextEndColor, transitionFactor);
        } else if (transitionStartTick >= 0 && time >= transitionStartTick + gradientTransitionFrames) {
            // Finalize the transition
            currentStartColor = nextStartColor;
            currentEndColor = nextEndColor;
            transitionStartTick = -1; // Reset transition
        }
    }

    // Interpolates between two colors based on a factor (0.0 to 1.0)
    TextColor.RGB interpolateColor(TextColor.RGB start, TextColor.RGB end, float factor) {
        int r = (int) (start.getRed() + factor * (end.getRed() - start.getRed()));
        int g = (int) (start.getGreen() + factor * (end.getGreen() - start.getGreen()));
        int b = (int) (start.getBlue() + factor * (end.getBlue() - start.getBlue()));
        return new TextColor.RGB(r, g, b);
    }

    // Generates a random RGB color
    TextColor.RGB randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new TextColor.RGB(r, g, b);
    }

}
