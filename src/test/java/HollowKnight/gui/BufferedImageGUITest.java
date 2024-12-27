package HollowKnight.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.googlecode.lanterna.TextColor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BufferedImageGUITest {

    private BufferedImageGUI gui;
    private BufferedImage buffer;

    @BeforeEach
    void setUp() {
        // Setup a BufferedImage with size 100x100 for testing
        buffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        gui = new BufferedImageGUI(buffer);
    }

    @Test
    void testGetWidth() {
        // Check that the width of the buffer matches the expected width
        assertEquals(100, gui.getWidth());
    }

    @Test
    void testGetHeight() {
        // Check that the height of the buffer matches the expected height
        assertEquals(100, gui.getHeight());
    }

    @Test
    void testDrawPixel() {
        TextColor.RGB color = new TextColor.RGB(255, 0, 0); // Red color
        gui.drawPixel(10, 10, color);

        // Ensure the pixel at (10, 10) is red
        Color pixelColor = new Color(buffer.getRGB(10, 10));
        assertEquals(255, pixelColor.getRed());
        assertEquals(0, pixelColor.getGreen());
        assertEquals(0, pixelColor.getBlue());
    }

    @Test
    void testDrawRectangle() {
        TextColor.RGB color = new TextColor.RGB(0, 255, 0); // Green color
        gui.drawRectangle(10, 10, 20, 20, color);

        // Check a few pixels inside the rectangle
        Color rectangleColor1 = new Color(buffer.getRGB(15, 15)); // Inside the rectangle
        Color rectangleColor2 = new Color(buffer.getRGB(25, 25)); // Inside the rectangle
        Color rectangleColor3 = new Color(buffer.getRGB(5, 5)); // Outside the rectangle

        assertEquals(0, rectangleColor3.getRed());  // Not affected (default black)
        assertEquals(0, rectangleColor3.getGreen());
        assertEquals(0, rectangleColor3.getBlue());

        assertEquals(0, rectangleColor1.getRed());
        assertEquals(255, rectangleColor1.getGreen());
        assertEquals(0, rectangleColor1.getBlue());

        assertEquals(0, rectangleColor2.getRed());
        assertEquals(255, rectangleColor2.getGreen());
        assertEquals(0, rectangleColor2.getBlue());
    }

    @Test
    void testCls() {
        TextColor.RGB color = new TextColor.RGB(0, 0, 0); // Black color
        gui.cls();

        // Check the entire buffer is set to black (or transparent depending on the design)
        Color colorAt0 = new Color(buffer.getRGB(0, 0));
        Color colorAt50 = new Color(buffer.getRGB(50, 50));

        assertEquals(0, colorAt0.getRed());
        assertEquals(0, colorAt0.getGreen());
        assertEquals(0, colorAt0.getBlue());

        assertEquals(0, colorAt50.getRed());
        assertEquals(0, colorAt50.getGreen());
        assertEquals(0, colorAt50.getBlue());
    }

    @Test
    void testDrawText() {
        TextColor.RGB color = new TextColor.RGB(0, 255, 0); // Green text color
        String text = "Hello, world!";
        gui.drawText(10, 10, color, text);

        // We cannot easily assert the exact pixels, but we can test the behavior
        // Assuming we have access to the Graphics object used to draw text, a simple way
        // is to mock the drawing mechanism if testing actual graphics calls
        // To assert the drawing operation
        assertDoesNotThrow(() -> gui.drawText(10, 10, color, text));
    }

    // Test to ensure that the flush and close methods don't throw exceptions
    @Test
    void testFlushAndClose() {
        // Assert that no exceptions are thrown when calling flush and close
        assertDoesNotThrow(() -> gui.flush());
        assertDoesNotThrow(() -> gui.close());
    }

    @Test
    void testUnsupportedMethods() {
        // Assert UnsupportedOperationExceptions for unsupported methods
        assertThrows(UnsupportedOperationException.class, () -> gui.getACTION());
        assertThrows(UnsupportedOperationException.class, () -> gui.getGUI());
        assertThrows(UnsupportedOperationException.class, () -> gui.getFPS());
        assertThrows(UnsupportedOperationException.class, () -> gui.setFPS(60));
        assertThrows(UnsupportedOperationException.class, () -> gui.drawHitBox(10, 10, 50, 50, new TextColor.RGB(255, 255, 255)));
    }
}
