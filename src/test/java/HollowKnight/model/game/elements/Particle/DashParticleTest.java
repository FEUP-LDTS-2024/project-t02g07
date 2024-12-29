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

class DashParticleTest {
    private Scene scene;
    private int time;
    private DashParticle dashParticle;
    @BeforeEach
    void setUp() {
        this.scene = Mockito.mock(Scene.class);
        this.time=0;
        this.dashParticle = new DashParticle(1,1, new Position(1,1)
                ,new TextColor.RGB(0,0,0));
    }

    @Test
    void moveParticle() {
        Position position = dashParticle.moveParticle(scene,time);
        Assertions.assertNotEquals(position.x(), dashParticle.getPosition().x());
        Assertions.assertNotEquals(position.y(), dashParticle.getPosition().y());
    }

    @Test
    void testCollision() {
        Vector vel = dashParticle.applyCollisions(new Vector(1, 1));
        assertEquals(vel, new Vector(1, 1));
    }

    @Property
    public void move(
            @ForAll @IntRange(max = WIDTH) int x,
            @ForAll @IntRange(max = HEIGHT) int y
    ) {
        DashParticle dashParticle1 = new DashParticle(x, y, new Position(0,0), new TextColor.RGB(0,0,0));

        Position newPosition = dashParticle1.moveParticle(scene,25);

        assertTrue((newPosition.x() != dashParticle1.getPosition().x()) || newPosition.y() != dashParticle1.getPosition().y());
    }

}