package HollowKnight.model.game.elements.Knight;

import HollowKnight.model.Position;
import HollowKnight.model.Vector;
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
    }

    @Test
    void updateVelocity() {
        Vector result = idleState.updateVelocity(knight.getVelocity());

        assertEquals(0.0, result.x());
        assertEquals(0.0, result.y());
    }

    @Test
    void getNextStateWalking() {
        knight.setVelocity(new Vector(1, 0));

        KnightState nextState = idleState.getNextState();

        assertInstanceOf(WalkingState.class, nextState);
    }

    @Test
    void getNextStateStay() {
        KnightState nextState = idleState.getNextState();

        assertSame(idleState, nextState);
    }
}