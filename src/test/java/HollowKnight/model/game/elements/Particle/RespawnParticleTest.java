package HollowKnight.model.game.elements.Particle;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RespawnParticleTest {
    private RespawnParticle respawnParticle;
    private Scene mockedScene;
    @BeforeEach
    void setUp() {
        this.respawnParticle =
                new RespawnParticle(1,1, new Position(0, 3.0), new TextColor.RGB(0,0,0));
        this.mockedScene = mock(Scene.class);
        respawnParticle.setScene(mockedScene);
    }

    @Test
    void testCollision() {
        Vector vel = respawnParticle.applyCollisions(new Vector(1, 1));
        assertEquals(vel, new Vector(1, 1));
    }


    @Test
    void moveParticleDownCollision() {
        when(mockedScene.collidesDown(any(),any())).thenReturn(true);
        when(mockedScene.collidesLeft(any(),any())).thenReturn(false);
        when(mockedScene.collidesRight(any(),any())).thenReturn(false);
        when(mockedScene.collidesUp(any(),any())).thenReturn(false);

        Position newPosition = respawnParticle.moveParticle(mockedScene,10);
        assertEquals(newPosition.y(), respawnParticle.getPosition().y());
    }

    @Test
    void moveParticleLeftCollision() {
        respawnParticle.setPosition(new Position(2,0));
        when(mockedScene.collidesDown(any(),any())).thenReturn(false);
        when(mockedScene.collidesLeft(any(),any())).thenReturn(true);
        when(mockedScene.collidesRight(any(),any())).thenReturn(false);
        when(mockedScene.collidesUp(any(),any())).thenReturn(false);

        Position newPosition = respawnParticle.moveParticle(mockedScene,10);
        assertEquals(newPosition.x(), respawnParticle.getPosition().x());
    }

    @Test
    void moveParticleUpCollision() {
        when(mockedScene.collidesDown(any(),any())).thenReturn(false);
        when(mockedScene.collidesLeft(any(),any())).thenReturn(false);
        when(mockedScene.collidesRight(any(),any())).thenReturn(false);
        when(mockedScene.collidesUp(any(),any())).thenReturn(true);

        Position newPosition = respawnParticle.moveParticle(mockedScene,10);
        assertEquals(newPosition.y(), respawnParticle.getPosition().y());
    }


    @Test
    void moveParticleRightCollision() {
        respawnParticle.setPosition(new Position(2,0));

        when(mockedScene.collidesDown(any(),any())).thenReturn(false);
        when(mockedScene.collidesLeft(any(),any())).thenReturn(false);
        when(mockedScene.collidesRight(any(),any())).thenReturn(true);
        when(mockedScene.collidesUp(any(),any())).thenReturn(false);

        Position newPosition = respawnParticle.moveParticle(mockedScene,10);
        assertEquals(newPosition.y(), respawnParticle.getPosition().y());
    }

    @Test
    void testScene() {
        assertEquals(respawnParticle.getScene(), respawnParticle.getScene());
    }

    @Test
    void testSetColor() {
        respawnParticle.setColor(new TextColor.RGB(25, 25, 25));
        assertEquals(respawnParticle.getColor(), new TextColor.RGB(25, 25, 25));
    }
}