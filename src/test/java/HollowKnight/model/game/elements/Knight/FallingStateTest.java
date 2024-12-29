package HollowKnight.model.game.elements.Knight;

import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FallingStateTest {
    private Knight knight;
    private Knight knight_;

    private Scene scene;
    private FallingState fallingState;
    private FallingState fallingState2;


    @BeforeEach
    void setUp() {
        knight = mock(Knight.class);
        knight_ = new Knight(0, 0, 50, 1, 1);
        scene = mock(Scene.class);
        knight_.setScene(scene);
        fallingState2 = new FallingState(knight_);

        knight_.setState(fallingState2);
        when(knight.getScene()).thenReturn(scene);
        when(knight.getAcceleration()).thenReturn(0.75);
        when(knight.getVelocity()).thenReturn(new Vector(0, 1));
        when(knight.getMaxVelocity()).thenReturn(new Vector(2, 3));
        when(scene.getGravity()).thenReturn(0.25);

        fallingState = new FallingState(knight);
    }
    @Test
    void dashRight() {
        knight_.setFacingRight(true);
        Vector result = fallingState2.dash();

        assertEquals(6.0, result.x());
        assertEquals(0.0, result.y());
        assertTrue(knight_.isFacingRight());
    }

    @Test
    void dashLeft() {
        knight_.setFacingRight(false);
        Vector result = fallingState2.dash();

        assertEquals(-6.0, result.x());
        assertEquals(0.0, result.y());
        assertFalse(knight_.isFacingRight());
    }
    @Test
    void testJumpWithValidVelocityAndJumpCounter() {
        when(knight.getJumpCounter()).thenReturn(0);
        when(knight.getJumpBoost()).thenReturn(4.0);

        Vector result = fallingState.jump();

        assertEquals(-2.5625, result.y(), 0.1); // Velocity adjusted by jump boost
        verify(knight).setJumpCounter(1);
        verify(scene).setDoubleJumpParticles(anyList());
    }

    @Test
    void testJumpExceedsJumpCounter() {
        when(knight.getJumpCounter()).thenReturn(2);

        Vector result = fallingState.jump();

        assertEquals(1.4375, result.y(), 0.1); // No change in velocity
        verify(knight, never()).setJumpCounter(anyInt());
        verify(scene, never()).setDoubleJumpParticles(anyList());
    }

    @Test
    void testJumpInvalidVelocity() {
        when(knight.getVelocity()).thenReturn(new Vector(0, 2)); // Velocity exceeds jump range

        Vector result = fallingState.jump();

        assertEquals(2.4375, result.y(), 0.1); // No change in velocity
        verify(knight, never()).setJumpCounter(anyInt());
        verify(scene, never()).setDoubleJumpParticles(anyList());
    }

    @Test
    void testUpdateVelocityLowVelocity() {
        when(knight.getVelocity()).thenReturn(new Vector(0, 0.5));

        Vector result = fallingState.updateVelocity(new Vector(1, 0.5));

        assertEquals(0.75, result.x(), 0.1); // Velocity adjusted by acceleration
        assertEquals(0.625, result.y(), 0.1); // Gravity applied
    }

    @Test
    void testUpdateVelocityHighVelocity() {
        when(knight.getVelocity()).thenReturn(new Vector(0, 2));

        Vector result = fallingState.updateVelocity(new Vector(1, 2));

        assertEquals(0.75, result.x(), 0.1); // Velocity adjusted by acceleration
        assertEquals(2.4375, result.y(), 0.1); // Gravity scaled for high velocity
    }

    @Test
    void testGetNextStateOnGround() {
        when(knight.isOnGround()).thenReturn(true);
        when(knight.getVelocity()).thenReturn(new Vector(3, 0));

        KnightState nextState = fallingState.getNextState();

        assertTrue(nextState instanceof RunningState); // Should transition to RunningState
    }

    @Test
    void testGetNextStateMidAir() {
        when(knight.isOnGround()).thenReturn(false);
        when(knight.getJumpCounter()).thenReturn(0);

        Vector result = fallingState.jump();
        KnightState nextState = fallingState.getNextState();
        assertEquals(1.4375, result.y());
        assertTrue(nextState instanceof FallingState); // Should transition to JumpState
    }

    @Test
    void testGetNextStateNoTransition() {
        when(knight.isOnGround()).thenReturn(false);
        when(knight.getJumpCounter()).thenReturn(0);

        KnightState nextState = fallingState.getNextState();

        assertSame(fallingState, nextState); // Should remain in FallingState
    }

    @Test
    void testRespawnParticlesTriggered() {
        fallingState.tickParticles(); // Decrement the timer to 0
        for (int i = 0; i < 99; i++) {
            fallingState.tickParticles();
        }

        assertEquals(0, fallingState.getParticlesTimer());
        KnightState nextState = fallingState.getNextState();

        verify(scene).setRespawnParticles(anyList()); // Verify respawn particles triggered
        assertNotNull(nextState); // Ensure next state is valid
    }
}
