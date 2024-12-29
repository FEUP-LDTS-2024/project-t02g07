package HollowKnight.model.game.elements.Knight;

import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DamagedStateTest {
    private Knight knight;
    private DamagedState damageState;
    private Scene mockedScene;

    @BeforeEach
    void setup() {
        mockedScene = mock(Scene.class);
        knight = new Knight(0, 0, 50,0,0);
        knight.setScene(mockedScene);
        damageState = new DamagedState(knight, 10);
        knight.setState(damageState);
        knight.setFacingRight(true);
        knight.setVelocity(new Vector(1.8, 2.0));
        when(mockedScene.getGravity()).thenReturn(0.25);
    }

    @Test
    void jump() {
        Vector result = damageState.jump();

        assertEquals(1.35, result.x());
        assertEquals(-1.0, Math.floor(result.y()));
    }

    @Test
    void dash() {
        Vector result = damageState.dash();

        assertEquals(6.0, result.x());
        assertEquals(2.0, result.y());
        assertTrue(knight.isFacingRight());
    }

    @Test
    void updateVelocity() {
        Vector result = damageState.updateVelocity(knight.getVelocity());

        assertEquals(1.35, result.x());
        assertEquals(2.25, result.y());

    }

    @Test
    void getNextStateIdle() {
        when(knight.isOnGround()).thenReturn(true);
        knight.setVelocity(new Vector(0.0, 0.0));

        KnightState nextState = damageState.getNextState();

        assertInstanceOf(DamagedState.class, nextState);
    }

    @Test
    void getNextStateWalking() {
        when(knight.isOnGround()).thenReturn(true);
        knight.setVelocity(new Vector(1.0, 0.0));

        KnightState nextState = damageState.getNextState();

        assertInstanceOf(DamagedState.class, nextState);
    }

    @Test
    void getNextStateRunning() {
        when(knight.isOnGround()).thenReturn(true);
        knight.setVelocity(new Vector(2.0, 0.0));

        KnightState nextState = damageState.getNextState();

        assertInstanceOf(DamagedState.class, nextState);
    }

    @Test
    void getNextStateStay() {
        KnightState nextState = damageState.getNextState();

        assertSame(damageState, nextState);
    }

    @Test
    void testTicks() {
        damageState.setTicks(10);
        assertEquals(damageState.getTicks(), 10);
    }
}