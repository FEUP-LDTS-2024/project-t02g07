package HollowKnight.view.text;

import HollowKnight.gui.GUI;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTextViewerTest {

    @Mock
    private GUI gui;

    private BufferedImage mockFontImage;
    private GameTextViewer textViewer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Mock font image
        mockFontImage = mock(BufferedImage.class);
        when(mockFontImage.getWidth()).thenReturn(100);
        when(mockFontImage.getHeight()).thenReturn(100);

        // Mock resources
        URL mockFontImageResource = getClass().getClassLoader().getResource("fonts/font.png");
        URL mockFontMapResource = getClass().getClassLoader().getResource("fonts/font-map.txt");

        assertNotNull(mockFontImageResource, "Font image resource must exist");
        assertNotNull(mockFontMapResource, "Font map resource must exist");

        textViewer = new GameTextViewer() {
            private BufferedImage loadFontImage(URL fontImageResource) {
                return mockFontImage;
            }

            private Map<Character, CharPosition> parseCharMap(URL fontMapResource) {
                return Map.of('A', new CharPosition(0, 0), 'B', new CharPosition(1, 0));
            }
        };
    }

    @Test
    void drawKnownCharacter() throws IOException {
        CharPosition position = new CharPosition(0, 0);
        when(mockFontImage.getRGB(anyInt(), anyInt())).thenReturn(0x000000); // Mock black pixel

        textViewer.draw('A', 10, 20, new TextColor.RGB(255, 0, 0), gui);

        verify(gui, atLeastOnce()).drawPixel(eq(10), eq(20), any(TextColor.RGB.class));
    }

    @Test
    void drawUnknownCharacter() {
        textViewer.draw('ð', 10, 20, new TextColor.RGB(255, 0, 0), gui);
        verify(gui).drawRectangle(eq(10), eq(20), eq(3), eq(5), any(TextColor.RGB.class));
    }

    @Test
    void drawStringWithMixedCharacters() throws IOException {
        textViewer.draw("AB?", 10, 20, new TextColor.RGB(0, 255, 0), gui);

        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));

        assertTrue(xCaptor.getAllValues().stream().anyMatch(x -> x >= 10)); // Check positions
        assertTrue(yCaptor.getAllValues().stream().allMatch(y -> y >= 20));
    }

    @Test
    void testGetCharHeight() {
        assertEquals(5, GameTextViewer.getCharHeight());
    }
}
