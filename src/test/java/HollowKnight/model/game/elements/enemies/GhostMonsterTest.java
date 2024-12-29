package HollowKnight.model.game.elements.enemies;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class GhostMonsterTest {
    private GhostMonster ghostMonster;
    private Scene scene;

    @BeforeEach
    void setUp() {
        this.scene = Mockito.mock(Scene.class);
        ghostMonster = new GhostMonster(10, 20, 10, scene, 15, new Position(2, 2), 'x');
    }

    @Test
    void testGetChar() {
        assertEquals('x', ghostMonster.getChar(), "Character representation should match 'x'");
    }

    @Test
    void testUpdatePositionSineWaveMotion() {
        ghostMonster.setHorizontalSpeed(5);
        ghostMonster.setAmplitude(3);
        ghostMonster.setFrequency(0.1);

        Position initialPosition = ghostMonster.getPosition();
        Position updatedPosition = ghostMonster.updatePosition();

        assertFalse(updatedPosition.x() > initialPosition.x(), "GhostMonster should move right due to horizontalSpeed");
        assertNotEquals(initialPosition.y(), updatedPosition.y(), "GhostMonster's y position should change due to sine wave motion");
    }

    @Test
    void testUpdatePositionScreenWrapping() {
        ghostMonster.setHorizontalSpeed(5);
        ghostMonster.setAmplitude(3);
        ghostMonster.setFrequency(0.1);


        // Place ghost near the right edge to trigger wrapping
        ghostMonster.setPosition(new Position(99, 25));
        Position updatedPosition = ghostMonster.updatePosition();

        assertTrue(updatedPosition.x() < 100, "GhostMonster should wrap from right edge to left");

        // Place ghost near the bottom edge to trigger wrapping
        ghostMonster.setPosition(new Position(50, 49));
        updatedPosition = ghostMonster.updatePosition();

        assertTrue(updatedPosition.y() < 50, "GhostMonster should wrap from bottom edge to top");
    }

    @Test
    void testMoveMonsterUpdatesPosition() {
        ghostMonster.setHorizontalSpeed(3);
        ghostMonster.setAmplitude(2);
        ghostMonster.setFrequency(0.2);

        Position initialPosition = ghostMonster.getPosition();
        Position movedPosition = ghostMonster.moveMonster();

        assertNotEquals(initialPosition, movedPosition, "moveMonster should update the GhostMonster's position");
        assertEquals(movedPosition, ghostMonster.getPosition(), "moveMonster should set the new position correctly");
    }

    @Test
    void testApplyCollisions() {
        Vector initialVelocity = new Vector(2, 3);
        Vector resultingVelocity = ghostMonster.applyCollisions(initialVelocity);

        assertEquals(initialVelocity, resultingVelocity, "applyCollisions should return the original velocity for GhostMonster");
    }
}
