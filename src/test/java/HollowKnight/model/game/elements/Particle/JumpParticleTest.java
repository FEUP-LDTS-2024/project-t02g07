package HollowKnight.model.game.elements.Particle;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import com.googlecode.lanterna.TextColor;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.lifecycle.BeforeTry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JumpParticleTest {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 90;

    private Scene scene;
    private int time;
    private JumpParticle jumpParticle;

    @BeforeEach
    public void setup() {
        this.scene = mock(Scene.class);
        this.time=0;
        this.jumpParticle = new JumpParticle(1,1, new Position(1,1)
                ,new TextColor.RGB(0,0,0));
    }

    @Test
    void moveParticle() {
        Position position = jumpParticle.moveParticle(scene,time);
        Assertions.assertNotEquals(position.x(), jumpParticle.getPosition().x());
        Assertions.assertNotEquals(position.y(), jumpParticle.getPosition().y());
    }
    @Test
    void testCollision() {
        Vector vel = jumpParticle.applyCollisions(new Vector(1, 1));
        assertEquals(vel, new Vector(1, 1));
    }

    @Property
    public void move(
            @ForAll @IntRange(max = WIDTH) int x,
            @ForAll @IntRange(max = HEIGHT) int y
    ) {
        JumpParticle jumpParticle1 = new JumpParticle(x, y, new Position(0,0), new TextColor.RGB(0,0,0));

        Position newPosition = jumpParticle1.moveParticle(scene,25);

        assertTrue((newPosition.x() != jumpParticle1.getPosition().x()) || newPosition.y() != jumpParticle1.getPosition().y());
    }
}