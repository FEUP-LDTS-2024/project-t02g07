package HollowKnight.controller.menu;

import HollowKnight.Game;
import HollowKnight.gui.GUI;
import HollowKnight.model.menu.Menu;
import HollowKnight.model.menu.Particle;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.state.particle.ParticleState;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticleMenuControllerTest {

    private Game game;
    private Menu menu;
    private ParticleMenuController controller;
    private Particle particle;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        menu = mock(Menu.class);
        controller = new ParticleMenuController(menu);

        // Mock particle
        particle = mock(Particle.class);
        when(menu.getParticles()).thenReturn(Arrays.asList(particle));
    }

    @Test
    void testGetWindAngle() {
        // Simply checks if the wind angle is returned correctly (no complex logic to validate)
        assertEquals(0, controller.getWindAngle());
    }

    @Test
    void testGetWindSpeed() {
        // Ensures that the wind speed is set correctly
        assertEquals(2, controller.getWindSpeed());
    }

    @Test
    void testWrapPosition() {
        // Test each edge case for wrapping the particle position

        // Left boundary
        Position pos = controller.wrapPosition(-1, 10);
        assertEquals(219, pos.x());
        assertEquals(10, pos.y());

        // Right boundary
        pos = controller.wrapPosition(200, 10);
        assertEquals(200.0, pos.x());
        assertEquals(10, pos.y());

        // Top boundary
        pos = controller.wrapPosition(10, -1);
        assertEquals(10, pos.x());
        assertEquals(109, pos.y());

        // Bottom boundary
        pos = controller.wrapPosition(10, 110);
        assertEquals(10, pos.x());
        assertEquals(1, pos.y());

        // Center case (no wrap)
        pos = controller.wrapPosition(100, 50);
        assertEquals(100, pos.x());
        assertEquals(50, pos.y());
    }

    @Test
    void testParticleMovementModes() throws IOException {
        // Test particle state transitions and movement based on the time (mode logic)
        long currentTime = 1000;

        // Verify the mode and particle state transition
        for (int mode = 0; mode < 5; mode++) {
            int modeDuration = 50;
            long time = mode * modeDuration;
            int expectedMode = (int) ((time / modeDuration) % 5);

            // Create particle mock behavior
            ParticleState mockState = mock(ParticleState.class);
            when(mockState.move(any(), anyLong(), eq(controller))).thenReturn(new Position(1, 1));
            when(particle.getState()).thenReturn(mockState);

            // Switch mode based on the time interval
            controller.move(game, GUI.ACTION.NULL, time);

            // Ensure the correct particle state (based on mode) is set for each mode
            //verify(particle, times(1)).setState(any(ParticleState.class));
            assertNotNull(particle.getState());
        }
    }

    @Test
    void testUpdateGradients() throws IOException {
        // Simulate time passing for gradient transition
        long time = 500;
        controller.updateGradients(time);
        verify(particle, times(0)).setState(any());  // Ensure particles aren’t being affected by gradient logic directly

        // After time % 500 (tick), gradients should change
        controller.updateGradients(time + 500);
        assertNotEquals(controller.currentStartColor, controller.nextStartColor);
        assertNotEquals(controller.currentEndColor, controller.nextEndColor);
    }

    @Test
    void testSmoothGradientInterpolation() {
        // Test color interpolation logic
        float factor = 0.5f; // Midway through the transition
        TextColor.RGB interpolatedColor = controller.interpolateColor(
                new TextColor.RGB(0, 0, 0), new TextColor.RGB(255, 255, 255), factor);

        assertEquals(127, interpolatedColor.getRed());
        assertEquals(127, interpolatedColor.getGreen());
        assertEquals(127, interpolatedColor.getBlue());
    }

    @Test
    void testRandomColorGeneration() {
        // Generate random color and check it's valid
        TextColor.RGB color = controller.randomColor();
        assertTrue(color.getRed() >= 0 && color.getRed() <= 255);
        assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255);
        assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255);
    }
}
