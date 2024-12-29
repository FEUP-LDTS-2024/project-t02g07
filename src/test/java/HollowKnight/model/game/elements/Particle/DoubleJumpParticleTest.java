package HollowKnight.model.game.elements.Particle;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import com.googlecode.lanterna.TextColor;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import static org.junit.jupiter.api.Assertions.*;

class DoubleJumpParticleTest {
    private Scene scene;
    private int time;
    private DoubleJumpParticle doubleJumpParticle;
    @BeforeEach
    void setUp() {
        this.scene = Mockito.mock(Scene.class);
        this.time=0;
        this.doubleJumpParticle = new DoubleJumpParticle(1,1, new Position(1,1)
                ,new TextColor.RGB(0,0,0));
    }

    @Test
    void moveParticle() {
        Position position = doubleJumpParticle.moveParticle(scene,time);
        Assertions.assertNotEquals(position.x(), doubleJumpParticle.getPosition().x());
        Assertions.assertNotEquals(position.y(), doubleJumpParticle.getPosition().y());

    }
    @Test
    void testCollision() {
        Vector vel = doubleJumpParticle.applyCollisions(new Vector(1, 1));
        assertEquals(vel, new Vector(1, 1));
    }
    @Property
    public void move(
            @ForAll @IntRange(max = WIDTH) int x,
            @ForAll @IntRange(max = HEIGHT) int y
    ) {
        DoubleJumpParticle doubleJumpParticle1 = new DoubleJumpParticle(x, y, new Position(0,0), new TextColor.RGB(0,0,0));

        Position newPosition = doubleJumpParticle1.moveParticle(scene,25);

        assertTrue((newPosition.x() != doubleJumpParticle1.getPosition().x()) || newPosition.y() != doubleJumpParticle1.getPosition().y());
    }
}