package HollowKnight.model.game.elements.Knight;


import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JumpStateTest {

    private Knight knight;
    private JumpState jumpState;
    private Scene mockedScene;

    @BeforeEach
    void setup() {
        mockedScene = mock(Scene.class);
        knight = new Knight(0, 0,10,0,0);
        this.knight.setScene(mockedScene);
        jumpState = new JumpState(knight);
        knight.setState(jumpState);
        knight.setVelocity(new Vector(1, -0.25));
        when(mockedScene.getGravity()).thenReturn(0.25);
        when(knight.isOnGround()).thenReturn(false);
    }

    @Test
    void jump() {
        when(mockedScene.collidesUp(any(), any())).thenReturn(false);
        Vector result = jumpState.jump();

        assertEquals(0.75, result.x());
        assertEquals(-2.0, Math.floor(result.y()));

        when(mockedScene.collidesUp(any(), any())).thenReturn(true);
        result = jumpState.jump();

        assertEquals(0.75, result.x());
        assertEquals(-1.0, Math.ceil(result.y()));
    }

    @Test
    void dash() {
        knight.setFacingRight(true);
        Vector result = jumpState.dash();

        assertEquals(6.0, result.x());
        assertEquals(-0.25, result.y());
        assertTrue(knight.isFacingRight());
    }

    @Test
    void dashLeft() {
        knight.setFacingRight(false);
        Vector result = jumpState.dash();

        assertEquals(-6.0, result.x());
        assertEquals(-0.25, result.y());
        assertFalse(knight.isFacingRight());
    }

    @Test
    void updateVelocity() {
        Vector result = jumpState.updateVelocity(knight.getVelocity());

        assertEquals(0.75, result.x());
        assertEquals(-0.125, result.y());

    }

    @Test
    void jump_() {
        knight.setVelocity(new Vector(1, -2));

        when(mockedScene.collidesUp(any(), any())).thenReturn(false);
        Vector result = jumpState.jump();

        assertEquals(0.75, result.x());
        assertEquals(-3.0, Math.floor(result.y()));

        when(mockedScene.collidesUp(any(), any())).thenReturn(true);
        result = jumpState.jump();

        assertEquals(0.75, result.x());
        assertEquals(0, Math.ceil(result.y()));
    }

    @Test
    void dash_() {
        knight.setVelocity(new Vector(1, -2));

        knight.setFacingRight(true);
        Vector result = jumpState.dash();

        assertEquals(6.0, result.x());
        assertEquals(-2.0, result.y());
        assertTrue(knight.isFacingRight());
    }

    @Test
    void dashLeft_() {
        knight.setVelocity(new Vector(1, -2));

        knight.setFacingRight(false);
        Vector result = jumpState.dash();

        assertEquals(-6.0, result.x());
        assertEquals(-2.0, result.y());
        assertFalse(knight.isFacingRight());
    }

    @Test
    void updateVelocity_() {
        knight.setVelocity(new Vector(1, -2));

        Vector result = jumpState.updateVelocity(knight.getVelocity());

        assertEquals(0.75, result.x());
        assertEquals(-1.75, result.y());
    }


    @Test
    void getNextStateDashing() {
        knight.setVelocity(new Vector(10, knight.getVelocity().y()));

        KnightState nextState = jumpState.getNextState();

        assertInstanceOf(DashState.class, nextState);
    }

    @Test
    void getNextStateFalling() {
        knight.setVelocity(new Vector(knight.getVelocity().x(), 10));

        KnightState nextState = jumpState.getNextState();

        assertInstanceOf(FallingState.class, nextState);
    }

    @Test
    void getNextStateFallingOnZeroVelocity() {
        knight.setVelocity(new Vector(knight.getVelocity().x(), 0));

        KnightState nextState = jumpState.getNextState();

        assertInstanceOf(FallingState.class, nextState);
    }

    @Test
    void getNextStateStay() {
        KnightState nextState = jumpState.getNextState();

        assertSame(jumpState, nextState);
    }
}