package HollowKnight.model.game.elements.Knight;

import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IdleStateTest {
    private Knight knight;
    private IdleState idleState;
    private Scene mockedScene;


    @BeforeEach
    void setUp() {
        this.mockedScene = Mockito.mock(Scene.class);
        knight = new Knight(0, 0, 0,0,0);
        idleState = new IdleState(knight);
        knight.setState(idleState);
        knight.setVelocity(new Vector(0, 0));
        knight.setScene(mockedScene);
    }

    @Test
    void updateVelocity() {
        Vector result = idleState.updateVelocity(knight.getVelocity());

        assertEquals(0.0, result.x());
        assertEquals(0.0, result.y());
    }

    @Test
    void jump() {
        Vector result = idleState.jump();

        assertEquals(0.0, result.x());
        assertEquals(-4.0, Math.floor(result.y()));
    }

    @Test
    void dash() {
        Vector result = idleState.dash();

        assertEquals(6.0, result.x());
        assertEquals(0.0, result.y());
        assertTrue(knight.isFacingRight());
    }

    @Test
    void getNextStateWalking() {
        when(mockedScene.collidesDown(Mockito.any(), Mockito.any())).thenReturn(true);
        knight.setVelocity(new Vector(1, 0));

        KnightState nextState = idleState.getNextState();

        assertInstanceOf(WalkingState.class, nextState);
    }

    @Test
    void getNextStateStay() {
        when(mockedScene.collidesDown(Mockito.any(), Mockito.any())).thenReturn(true);

        KnightState nextState = idleState.getNextState();

        assertSame(idleState, nextState);
    }

    @Test
    void getNextStateDashing() {
        when(knight.isOnGround()).thenReturn(true);
        knight.setVelocity(new Vector(10, knight.getVelocity().y()));

        KnightState nextState = idleState.getNextState();

        assertInstanceOf(DashState.class, nextState);
    }

    @Test
    void getNextStateJumping() {
        when(knight.isOnGround()).thenReturn(false);
        knight.setVelocity(new Vector(knight.getVelocity().x(), -10));

        KnightState nextState = idleState.getNextState();

        assertInstanceOf(JumpState.class, nextState);
    }
}