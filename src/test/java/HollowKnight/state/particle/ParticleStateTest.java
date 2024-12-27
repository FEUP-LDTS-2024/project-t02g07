package HollowKnight.state.particle;
import HollowKnight.controller.menu.ParticleMenuController;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.menu.Particle;
import HollowKnight.state.particle.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ParticleStateTest {
    @Mock
    private ParticleMenuController controller;

    @Mock
    private Particle particle;

    private RandomState randomState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        randomState = new RandomState();
    }

    // TESTING CalmState
    @Test
    void testCalmStateMove_shouldMoveParticleDownAndRandomly() {
        // Arrange
        when(particle.getPosition()).thenReturn(new Position(5, 5));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(new Position(5, 6));

        // Act
        ParticleState calmState = new CalmState();
        Position newPosition = calmState.move(particle, 1000L, controller);

        // Assert: Y position should increase by 1, X should remain unchanged (mocked behavior)
        assertEquals(5, newPosition.x());
        assertEquals(6, newPosition.y());
    }

    // TESTING DispersingState
    @Test
    void testDispersingStateMove_shouldMoveParticleWithWindEffect() {
        // Arrange: Mocking a wind effect with speed and angle
        when(particle.getPosition()).thenReturn(new Position(5, 5));
        when(controller.getWindAngle()).thenReturn(Math.PI / 4); // 45 degrees
        when(controller.getWindSpeed()).thenReturn(2.0);
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(new Position(6, 7));

        // Act
        ParticleState dispersingState = new DispersingState();
        Position newPosition = dispersingState.move(particle, 1000L, controller);

        // Assert: X and Y position should be affected by the wind speed and angle (mocked behavior)
        assertEquals(6, newPosition.x());
        assertEquals(7, newPosition.y());
    }

    // TESTING WindyState
    @Test
    void testWindyStateMove_shouldMoveParticleAccordingToWind() {
        // Arrange: Mocking a wind effect
        when(particle.getPosition()).thenReturn(new Position(5, 5));
        when(controller.getWindAngle()).thenReturn(Math.PI / 4); // 45 degrees
        when(controller.getWindSpeed()).thenReturn(2.0);
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(new Position(6, 7));

        // Act
        ParticleState windyState = new WindyState();
        Position newPosition = windyState.move(particle, 1000L, controller);

        // Assert: Verify that wind effects on the particle position are handled correctly
        assertEquals(6, newPosition.x());
        assertEquals(7, newPosition.y());
    }

    // TESTING ZicoState
    @Test
    void testZicoStateMove_shouldMoveParticleDownBy10Units() {
        // Arrange
        when(particle.getPosition()).thenReturn(new Position(5, 5));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(new Position(5, 15));

        // Act
        ParticleState zicoState = new ZicoState();
        Position newPosition = zicoState.move(particle, 1000L, controller);

        // Assert: The Y position should be moved by 10 units down
        assertEquals(5, newPosition.x()); // X position remains the same
        assertEquals(15, newPosition.y()); // Y position is 10 units lower
    }

    @Test
    void testMove_shouldMoveParticleWithRandomOffset() {
        // Arrange: Mocking particle's position and the behavior of the random object
        when(particle.getPosition()).thenReturn(new Position(5, 5));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(new Position(6, 6));

        // Simulating random.nextInt(3) behavior (random values between 0, 1, 2)
//        when(random.nextInt(3)).thenReturn(1);  // Will result in a movement of +1 for both X and Y

        // Act
        Position newPosition = randomState.move(particle, 1000L, controller);

        // Assert: Verify that X and Y are incremented by a random value (-1, 0, or +1 for each)
        assertEquals(6, newPosition.x()); // Expected new X position based on random nextInt(3) + -1
        assertEquals(6, newPosition.y()); // Expected new Y position based on random nextInt(3) + 1
    }
}
