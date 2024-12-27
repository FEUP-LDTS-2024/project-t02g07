package HollowKnight.view.states;

import HollowKnight.gui.BufferedImageGUI;
import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.scene.Scene;
import HollowKnight.view.elements.*;
import HollowKnight.view.elements.collectables.OrbViewer;
import HollowKnight.view.elements.knight.KnightViewer;
import HollowKnight.view.elements.particle.ParticleViewer;
import HollowKnight.view.elements.monsters.MonsterViewer;
import HollowKnight.view.elements.rocks.RockViewer;
import HollowKnight.view.elements.spike.SpikeViewer;
import HollowKnight.view.elements.tile.TileViewer;
import HollowKnight.view.elements.tree.TreeViewer;
import HollowKnight.view.sprites.ViewerProvider;
import HollowKnight.view.text.GameTextViewer;
import HollowKnight.view.text.TextViewer;
import com.googlecode.lanterna.TextColor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameViewer extends ScreenViewer<Scene> {

    final TextViewer textViewer;

    private static final int CAMERA_WIDTH = 230;
    private static final int CAMERA_HEIGHT = 130;

    final ParticleViewer particleViewer;
    final KnightViewer knightViewer;
    final TileViewer tileViewer;
    private final SpikeViewer spikeViewer;
    private final TreeViewer treeViewer;
    private final OrbViewer orbViewer;
    private final RockViewer rockViewer;
    final MonsterViewer monsterViewer;
    BufferedImage staticLayer;
    boolean staticLayerUpdated;

    private static final Logger LOGGER = Logger.getLogger(GameViewer.class.getName());

    public GameViewer(Scene model, ViewerProvider viewerProvider) throws IOException {

        super(model);

        this.textViewer = new GameTextViewer();

        this.particleViewer = viewerProvider.getParticleViewer();

        this.knightViewer = viewerProvider.getPlayerViewer();

        this.tileViewer = viewerProvider.getTileViewer();

        this.spikeViewer = viewerProvider.getSpikeViewer();

        this.treeViewer = viewerProvider.getTreeViewer();

        this.orbViewer = viewerProvider.getOrbViewer();

        this.rockViewer = viewerProvider.getRockViewer();

        this.monsterViewer = viewerProvider.getMonsterViewer();

        this.staticLayer = null;
        this.staticLayerUpdated = true;
    }

    @Override
    public void draw(GUI gui, long time) throws IOException {
        gui.cls();
        int[] cameraBounds = calculateCameraBounds();
        dynamicGradientBackground(gui, time);

        if (staticLayerUpdated || staticLayer == null) {
            updateStaticLayer(cameraBounds);
        }
        drawStaticLayer(gui);

        drawElements(gui, getModel().getParticles(), this.particleViewer, time, cameraBounds);
        drawElements(gui, getModel().getDoubleJumpParticles(), this.particleViewer, time, cameraBounds);
        drawElements(gui, getModel().getJumpParticles(), this.particleViewer, time, cameraBounds);
        drawElements(gui, getModel().getRespawnParticles(), this.particleViewer, time, cameraBounds);
        drawElements(gui, getModel().getDashParticles(), this.particleViewer, time, cameraBounds);
        drawElements(gui, getModel().getMonsters(), this.monsterViewer, time, cameraBounds);


        drawElement(gui, this.knightViewer, getModel().getPlayer(), time, cameraBounds);

        PlayerStatsViewer.drawPlayerStats(gui, time, getModel(), this.textViewer);

        gui.flush();
    }

    int[] calculateCameraBounds() {
        int playerX = (int) getModel().getPlayer().getPosition().x();
        int playerY = (int) getModel().getPlayer().getPosition().y();

        // Center the camera on the player
        int left = playerX - CAMERA_WIDTH / 2;
        int top = playerY - CAMERA_HEIGHT / 2;

        // Prevent the camera from moving out of map bounds
        left = Math.max(0, Math.min(left, 340 - CAMERA_WIDTH));
        top = Math.max(0, Math.min(top, 130 - CAMERA_HEIGHT));

        return new int[]{left, top, left + CAMERA_WIDTH, top + CAMERA_HEIGHT};
    }



    void updateStaticLayer(int[] cameraBounds) throws IOException {
        // Resize the static layer if needed
        if (staticLayer == null || staticLayer.getWidth() != CAMERA_WIDTH || staticLayer.getHeight() != CAMERA_HEIGHT) {
            staticLayer = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        }

        // Calculate world offset for rendering static layer
        int offsetX = cameraBounds[0]; // Camera's top-left corner (world coordinates)
        int offsetY = cameraBounds[1];

        // Clear the static layer
        java.awt.Graphics2D g = staticLayer.createGraphics();
        g.setColor(new java.awt.Color(0, 0, 0, 0)); // Transparent background
        g.fillRect(0, 0, staticLayer.getWidth(), staticLayer.getHeight());

        // Create a temporary GUI for rendering the static layer tiles
        GUI tempGUI = new BufferedImageGUI(staticLayer);

        // Draw only visible static elements (within the camera bounds)
        drawElements(tempGUI, getModel().getSpikes(), this.spikeViewer, 0, new int[]{offsetX, offsetY, offsetX + CAMERA_WIDTH, offsetY + CAMERA_HEIGHT});
        drawElements(tempGUI, getModel().getTiles(), this.tileViewer, 0, new int[]{offsetX, offsetY, offsetX + CAMERA_WIDTH, offsetY + CAMERA_HEIGHT});
        drawElements(tempGUI, getModel().getTrees(), this.treeViewer, 0, new int[]{offsetX, offsetY, offsetX + CAMERA_WIDTH, offsetY + CAMERA_HEIGHT});
        drawElements(tempGUI, getModel().getOrbs(), this.orbViewer, 0, new int[]{offsetX, offsetY, offsetX + CAMERA_WIDTH, offsetY + CAMERA_HEIGHT});
        drawElements(tempGUI, getModel().getRocks(), this.rockViewer, 0, new int[]{offsetX, offsetY, offsetX + CAMERA_WIDTH, offsetY + CAMERA_HEIGHT});


        g.dispose(); // Release graphics resources
        staticLayerUpdated = false;
    }

    void drawStaticLayer(GUI gui) {
        // Calculate the camera offset (start of visible region)
        int offsetX = calculateCameraBounds()[0];
        int offsetY = calculateCameraBounds()[1];

        // Render static layer directly onto GUI
        for (int x = 0; x < CAMERA_WIDTH; x++) {
            for (int y = 0; y < CAMERA_HEIGHT; y++) {
                // Map screen coordinates back to world coordinates
                int worldX = x + offsetX;
                int worldY = y + offsetY;

                // Skip out-of-bounds rendering
                if (worldX < 0 || worldY < 0 || worldX >= 1000 || worldY >= 1000) continue;

                // Retrieve pixel data from staticLayer
                int argb = staticLayer.getRGB(x, y);
                int alpha = (argb >> 24) & 0xff;

                if (alpha != 0) {
                    int red = (argb >> 16) & 0xff;
                    int green = (argb >> 8) & 0xff;
                    int blue = argb & 0xff;

                    // Render pixel to the GUI
                    gui.drawPixel(x, y, new TextColor.RGB(red, green, blue));
                }
            }
        }
    }

    boolean isElementInCamera(Element element, int[] cameraBounds) {
        int x = (int) element.getPosition().x();
        int y = (int) element.getPosition().y();
        return x >= cameraBounds[0] && x < cameraBounds[2] && y >= cameraBounds[1] && y < cameraBounds[3];
    }

    <T extends Element> void drawElements(GUI gui, List<T> elements, ElementViewer<T> viewer, long time, int[] cameraBounds) throws IOException {
        elements.stream()
                .filter(element -> isElementInCamera(element, cameraBounds))
                .forEach(element -> {
                    try {
                        drawElement(gui, viewer, element, time, cameraBounds);
                    } catch (IOException e) {
                        // Log the exception at a WARNING level
                        LOGGER.log(Level.WARNING, "Failed to draw element: {0}", e.getMessage());
                        LOGGER.log(Level.FINE, "Stack Trace: ", e);
                    }
                });
    }

    <T extends Element> void drawElement(GUI gui, ElementViewer<T> viewer, T element, long time, int[] cameraBounds) throws IOException {
        int adjustedX = (int) (element.getPosition().x() - cameraBounds[0]);
        int adjustedY = (int) (element.getPosition().y() - cameraBounds[1]);
        viewer.draw(element, gui, time, adjustedX, adjustedY);
    }

    private <T extends Element> void drawElements(GUI gui, T[][] elements, ElementViewer<T> viewer, long frameCount, int[] cameraBounds) throws IOException {
        for (T[] elementLine : elements) {
            for (T element : elementLine) {
                if (element != null)
                    drawElement(gui, viewer, element, frameCount, cameraBounds);
            }
        }
    }

    void dynamicGradientBackground(GUI gui, long time) {
        int width = 240; // getModel().getWidth();
        int height = 120; // getModel().getHeight();

        double changeRate = 0.04;
        // Calculate dynamic colors based on time, with a grayscale tone
        int baseRed1 = (int) (64 + 63 * Math.sin(time * changeRate)); // Shifted to darker gray
        int baseGreen1 = (int) (64 + 63 * Math.sin(time * changeRate + Math.PI / 3));
        int baseBlue1 = (int) (64 + 63 * Math.sin(time * changeRate + 2 * Math.PI / 3));

        int baseRed2 = (int) (64 + 63 * Math.sin(time * changeRate + Math.PI));
        int baseGreen2 = (int) (64 + 63 * Math.sin(time * changeRate + Math.PI + Math.PI / 3));
        int baseBlue2 = (int) (64 + 63 * Math.sin(time * changeRate + Math.PI + 2 * Math.PI / 3));

        TextColor.RGB color1 = new TextColor.RGB(baseRed1, baseGreen1, baseBlue1);
        TextColor.RGB color2 = new TextColor.RGB(baseRed2, baseGreen2, baseBlue2);

        boolean flashActive = (time % 800 < 20); // Flash active for 20 ticks every 800 ticks
        boolean afterEffectActive = (time % 800 >= 20 && time % 800 < 60); // Aftereffect active for 40 ticks
        double afterEffectFactor = afterEffectActive ? (1.0 - (time % 800 - 20) / 40.0) : 0.0;

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                if (flashActive) {
                    // During the flash, draw a bright white color
                    gui.drawPixel(w, h, new TextColor.RGB(255, 255, 255));
                } else if (afterEffectActive) {
                    // Aftereffect: add a subtle light glow on top of the gradient
                    double interpolationX = (double) w / (width - 1);
                    double interpolationY = (double) h / (height - 1);

                    int red = (int) ((1 - interpolationX) * color1.getRed() + interpolationX * color2.getRed());
                    int green = (int) ((1 - interpolationY) * color1.getGreen() + interpolationY * color2.getGreen());
                    int blue = (int) ((1 - interpolationX) * color1.getBlue() + interpolationX * color2.getBlue());

                    // Blend gradient with a light glow effect
                    red = (int) (red + afterEffectFactor * (255 - red));
                    green = (int) (green + afterEffectFactor * (255 - green));
                    blue = (int) (blue + afterEffectFactor * (255 - blue));

                    gui.drawPixel(w, h, new TextColor.RGB(red, green, blue));
                } else {
                    // Normal gradient background with darker tone
                    double interpolationX = (double) w / (width - 1);
                    double interpolationY = (double) h / (height - 1);

                    int red = (int) ((1 - interpolationX) * color1.getRed() + interpolationX * color2.getRed());
                    int green = (int) ((1 - interpolationY) * color1.getGreen() + interpolationY * color2.getGreen());
                    int blue = (int) ((1 - interpolationX) * color1.getBlue() + interpolationX * color2.getBlue());

                    // Apply an additional gray-scale reduction
                    red = (int) (red * 0.6); // Reduce to 60% of original
                    green = (int) (green * 0.6);
                    blue = (int) (blue * 0.6);

                    gui.drawPixel(w, h, new TextColor.RGB(red, green, blue));
                }
            }
        }
    }
}