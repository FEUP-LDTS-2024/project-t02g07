package HollowKnight.model.game.elements.Particle;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.scene.Scene;
import com.googlecode.lanterna.TextColor;

import static java.lang.Math.max;

public abstract class Particle extends Element {

    private Scene scene;

    private TextColor.RGB color;
    private Position velocity; // Includes x and y velocities
    private double opacity;    // Controls particle transparency
    private final double fadeRate = 0.00675; // Rate at which particles fade

    // Constructor and other methods...

    public Particle(int x, int y, Position velocity, TextColor.RGB color){
        super(x, y);
        this.color = color;
        this.opacity = 1.0;
        this.velocity = velocity;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public double getFadeRate() {
        return fadeRate;
    }

    public Position getVelocity() {
        return velocity;
    }

    public void setVelocity(Position velocity) {
        this.velocity = velocity;
    }

    public TextColor.RGB getColor() {
        return color;
    }

    public void setColor(TextColor.RGB color) {
        this.color = color;
    }

    protected abstract Vector applyCollisions(Vector velocity);

    public abstract Position moveParticle(Scene scene, long time);

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
