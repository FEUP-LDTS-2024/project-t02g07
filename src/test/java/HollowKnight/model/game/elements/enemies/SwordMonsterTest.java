package HollowKnight.model.game.elements.enemies;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SwordMonsterTest {
    private SwordMonster swordMonster;
    private Scene mockScene;

    @BeforeEach
    void setUp() {
        mockScene = Mockito.mock(Scene.class);
        swordMonster = new SwordMonster(20, 30, 50, mockScene, 25, new Position(8, 8), 's');
    }

    @Test
    void testGetChar() {
        assertEquals('s', swordMonster.getChar(), "Character representation should match 's'");
    }

    @Test
    void testUpdatePositionNoCollisions() {
        Vector velocity = new Vector(2, 0);
        swordMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Position newPosition = swordMonster.updatePosition();

        assertEquals(22, newPosition.x());
        assertEquals(30, newPosition.y());
    }

    @Test
    void testApplyCollisionsLeftCollision() {
        Vector velocity = new Vector(-1, 0);
        swordMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Vector newVelocity = swordMonster.applyCollisions(velocity);

        assertEquals(0, newVelocity.x(), "Velocity should adjust to 0 after collision on the left");
        assertEquals(0, newVelocity.y(), "Vertical velocity should remain unchanged");
    }

    @Test
    void testApplyCollisionsRightCollision() {
        Vector velocity = new Vector(1, 0);
        swordMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);

        Vector newVelocity = swordMonster.applyCollisions(velocity);

        assertEquals(0, newVelocity.x(), "Velocity should adjust to 0 after collision on the right");
        assertEquals(0, newVelocity.y(), "Vertical velocity should remain unchanged");
    }

    @Test
    void testApplyCollisionsNoCollisions() {
        Vector velocity = new Vector(3, 2);
        swordMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Vector newVelocity = swordMonster.applyCollisions(velocity);

        assertEquals(3, newVelocity.x(), "Velocity should remain unchanged when no collisions");
        assertEquals(2, newVelocity.y(), "Vertical velocity should remain unchanged when no collisions");
    }

    @Test
    void testMoveMonster() {
        Vector velocity = new Vector(2, 1);
        swordMonster.setVelocity(velocity);

        Mockito.when(mockScene.collidesLeft(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(mockScene.collidesRight(Mockito.any(), Mockito.any())).thenReturn(false);

        Position newPosition = swordMonster.moveMonster();

        assertEquals(22, newPosition.x(), "Monster should move correctly on the x-axis");
        assertEquals(31, newPosition.y(), "Monster should move correctly on the y-axis");
    }
}
