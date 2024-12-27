package HollowKnight.view.states;

import static org.mockito.Mockito.*;

import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.Knight.Knight;
import HollowKnight.model.game.scene.Scene;
import HollowKnight.view.text.TextViewer;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatsViewerTest {
    @Mock
    private GUI gui; // Mocked GUI object

    @Mock
    private Scene scene; // Mocked Scene object

    @Mock
    private Knight player; // Mocked Player object

    @Mock
    private TextViewer textViewer; // Mocked TextViewer object

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void testDrawPlayerStats_shouldCallTextViewerWithCorrectValues() throws IOException {
        // Arrange
        long time = 12345L; // Example timestamp
        TextColor.RGB expectedColor = new TextColor.RGB(0, 225, 75);

        // Mock the behavior of the player and GUI
        when(scene.getPlayer()).thenReturn(player); // When scene.getPlayer() is called, return mock player
        when(player.getHP()).thenReturn(100); // Player HP is mocked as 100
        when(gui.getFPS()).thenReturn(60); // FPS is mocked as 60
        when(player.getOrbs()).thenReturn(5); // Player has 5 orbs

        // Act
        PlayerStatsViewer.drawPlayerStats(gui, time, scene, textViewer); // Invoke the method to test

        // Assert: Verify that TextViewer's draw method was called with correct values
        verify(textViewer).draw(eq("hp 100"), eq(4.0d), eq(8.0d), any(TextColor.RGB.class), eq(gui)); // Verifies drawing HP
        verify(textViewer).draw(eq("fps 60"), eq(4.0d), eq(16.0d), any(TextColor.RGB.class), eq(gui)); // Verifies drawing FPS
        verify(textViewer).draw(eq("Orbs 5"), eq(160.0d), eq(8.0d), any(TextColor.RGB.class), eq(gui));
    }
    @Test
    void testDrawPlayerStats_withZeroOrbs_shouldCallTextViewerWithZeroOrbs() throws IOException {
        // Arrange
        long time = 12345L; // Example timestamp

        // Mock the behavior of the player and GUI
        when(scene.getPlayer()).thenReturn(player); // When scene.getPlayer() is called, return mock player
        when(player.getHP()).thenReturn(100); // Player HP is mocked as 100
        when(gui.getFPS()).thenReturn(60); // FPS is mocked as 60
        when(player.getOrbs()).thenReturn(0); // Player has 0 orbs

        // Act
        PlayerStatsViewer.drawPlayerStats(gui, time, scene, textViewer); // Invoke the method to test

        // Assert: Verify that TextViewer's draw method was called with correct values
        verify(textViewer).draw(eq("hp 100"), eq(4.0d), eq(8.0d), any(TextColor.RGB.class), eq(gui)); // Verifies drawing HP
        verify(textViewer).draw(eq("fps 60"), eq(4.0d), eq(16.0d), any(TextColor.RGB.class), eq(gui)); // Verifies drawing FPS
        verify(textViewer).draw(eq("Orbs 0"), eq(160.0d), eq(8.0d), any(TextColor.RGB.class), eq(gui)); // Verifies drawing Orbs with 0
    }
}