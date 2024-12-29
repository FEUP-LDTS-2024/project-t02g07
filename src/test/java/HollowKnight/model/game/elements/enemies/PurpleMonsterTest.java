package HollowKnight.model.game.elements.enemies;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PurpleMonsterTest {
    private PurpleMonster purpleMonster;
    private Scene mockScene;

    @BeforeEach
    void setUp() {
        mockScene = Mockito.mock(Scene.class);
        purpleMonster = new PurpleMonster(30, 40, 30, mockScene, 40, new Position(8, 9), 'p');
    }

    @Test
    void testGetChar() {
        assertEquals('p', purpleMonster.getChar(), "Character representation should match 'p'");
    }

    @Test
    void testUpdatePositionNoCollisions() {
        Vector velocity = new Vector(3, 2);
        purpleMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Position newPosition = purpleMonster.updatePosition();

        assertEquals(33, newPosition.x());
        assertEquals(42, newPosition.y());
    }

    @Test
    void testApplyCollisionsLeftCollision() {
        Vector velocity = new Vector(-2, 0);
        purpleMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Vector newVelocity = purpleMonster.applyCollisions(velocity);

        assertEquals(-1, newVelocity.x(), "Velocity should adjust after collision on the left");
        assertEquals(0, newVelocity.y(), "Vertical velocity should remain unchanged");
    }

    @Test
    void testApplyCollisionsRightCollision() {
        Vector velocity = new Vector(2, 0);
        purpleMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);

        Vector newVelocity = purpleMonster.applyCollisions(velocity);

        assertEquals(0, newVelocity.x(), "Velocity should adjust after collision on the right");
        assertEquals(0, newVelocity.y(), "Vertical velocity should remain unchanged");
    }

    @Test
    void testApplyCollisionsNoCollisions() {
        Vector velocity = new Vector(3, 2);
        purpleMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Vector newVelocity = purpleMonster.applyCollisions(velocity);

        assertEquals(3, newVelocity.x(), "Velocity should remain unchanged with no collisions");
        assertEquals(2, newVelocity.y(), "Velocity should remain unchanged with no collisions");
    }

    @Test
    void testMoveMonster() {
        Vector velocity = new Vector(2, 1);
        purpleMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Position newPosition = purpleMonster.moveMonster();

        assertEquals(32, newPosition.x(), "Monster should move correctly on the x-axis");
        assertEquals(41, newPosition.y(), "Monster should move correctly on the y-axis");
    }
}
