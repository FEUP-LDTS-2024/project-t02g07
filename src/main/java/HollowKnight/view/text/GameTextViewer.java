package HollowKnight.view.text;

import HollowKnight.gui.GUI;
import com.googlecode.lanterna.TextColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

record CharPosition(int row, int col) {}

public class GameTextViewer implements TextViewer {
    private static final int charWidth = 3;
    private static final int charHeight = 5;
    private static final int spacing = 1;

    private final BufferedImage fontImage;
    private final Map<Character, CharPosition> charMap;

    public GameTextViewer() throws IOException {
        URL fontImageResource = getClass().getClassLoader().getResource("fonts/font.png");
        if (fontImageResource == null) {
            throw new FileNotFoundException("Font image file not found in resources!");
        }
        this.fontImage = ImageIO.read(fontImageResource);

        URL fontMapResource = getClass().getClassLoader().getResource("fonts/font-map.txt");
        if (fontMapResource == null) {
            throw new FileNotFoundException("Font map file not found in resources!");
        }

        this.charMap = parseCharMap(fontMapResource);
    }

    private Map<Character, CharPosition> parseCharMap(URL resource) throws IOException {
        Map<Character, CharPosition> charMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
            int y = 0;
            for (String line; (line = bufferedReader.readLine()) != null; y++) {
                for (int x = 0; x < line.length(); x++) {
                    charMap.put(line.charAt(x), new CharPosition(x, y));
                }
            }
        }
        return charMap;
    }


    @Override
    public void draw(char character, double x, double y, TextColor foregroundColor, GUI gui) {
        if (charMap.containsKey(character)) {
            CharPosition charPosition = charMap.get(character);
            drawKnownChar(charPosition, x, y, foregroundColor, gui);
        } else {
            drawUnknownChar(x, y, (TextColor.RGB) foregroundColor, gui);
        }
    }

    private void drawKnownChar(CharPosition position, double x, double y, TextColor foregroundColor, GUI gui) {
        final int COLOR_WHITE = 0xFFFFFFFF;
        int imgX = position.row() * (charWidth + 1);
        int imgY = position.col() * (charHeight + 1);
        for (int dy = 0; dy < charHeight; dy++) {
            for (int dx = 0; dx < charWidth; dx++) {
                if (fontImage.getRGB(imgX + dx, imgY + dy) != COLOR_WHITE)
                    gui.drawPixel((int) (x + dx), (int) (y + dy), (TextColor.RGB) foregroundColor);
            }
        }
    }

    private void drawUnknownChar(double x, double y, TextColor.RGB foregroundColor, GUI gui) {
        gui.drawRectangle((int) x, (int) y, charWidth, charHeight, foregroundColor);
    }
    @Override
    public void draw(String string, double x, double y, TextColor foregroundColor, GUI gui) {
        for (int i = 0; i < string.length(); i++) {
            int xOffset = i * (charWidth + spacing);
            draw(string.charAt(i), x + xOffset, y, foregroundColor, gui);
        }
    }

    public static int getCharHeight() {
        return charHeight;
    }

}
