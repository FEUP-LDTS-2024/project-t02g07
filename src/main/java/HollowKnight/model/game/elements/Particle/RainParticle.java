package HollowKnight.model.game.elements.Particle;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import com.googlecode.lanterna.TextColor;

import static java.lang.Math.max;

public class RainParticle extends Particle {

    public RainParticle(int x, int y, Position velocity, TextColor.RGB color) {
        super(x, y, velocity, color);
    }

    @Override
    protected Vector applyCollisions(Vector velocity)
    {
        return velocity;
    }

    @Override
    public Position moveParticle(Scene scene, long time) {
        Position currentPos = getPosition();
        if (currentPos == null) {
            throw new IllegalStateException("Particle position is null");
        }

        // Handle wind simulation
        int windEffect = (int) (Math.random() * 3) - 1; // Wind effect range: -1 to 1
        int new_x = (int) getPosition().x() + 2 + windEffect;  // Adds wind effect to horizontal movement
        int new_y = (int) getPosition().y() + 2;  // Vertical movement remains constant

        // Handle boundary conditions for x and y
        if (new_x < 0) {
            new_x = scene.getWidth() - 1;
        } else if (new_x >= scene.getWidth()) {
            new_x = 1;
        }

        if (new_y >= scene.getHeight()) {
            new_y = 0;  // Particle reaches bottom and is "destroyed" (restarts from top)
        }
        return new Position(new_x, new_y);
    }
}