package HollowKnight.view.states;

import HollowKnight.gui.BufferedImageGUI;
import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.elements.enemies.Enemies;
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

    final ParticleViewer particleViewer;
    final KnightViewer knightViewer;
    final TileViewer tileViewer;
    private final SpikeViewer spikeViewer;
    private final TreeViewer treeViewer;
    private final OrbViewer orbViewer;
    private final RockViewer rockViewer;
    final MonsterViewer monsterViewer;

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

    }

    @Override
    public void draw(GUI gui, long time) throws IOException {
        gui.cls();

        dynamicGradientBackground(gui, time);

        drawElements(gui, getModel().getParticles(), this.particleViewer, time);
        drawElements(gui, getModel().getDoubleJumpParticles(), this.particleViewer, time);
        drawElements(gui, getModel().getJumpParticles(), this.particleViewer, time);
        drawElements(gui, getModel().getRespawnParticles(), this.particleViewer, time);
        drawElements(gui, getModel().getDashParticles(), this.particleViewer, time);

        drawElements(gui, getModel().getSpikes(), this.spikeViewer, 0);
        drawElements(gui, getModel().getTiles(), this.tileViewer, 0);
        drawElements(gui, getModel().getTrees(), this.treeViewer, 0);
        drawElements(gui, getModel().getOrbs(), this.orbViewer, 0);
        drawElements(gui, getModel().getRocks(), this.rockViewer, 0);

        drawElement(gui, this.knightViewer, getModel().getPlayer(), time);
        drawElements(gui, getModel().getMonsters(), this.monsterViewer, time);

        PlayerStatsViewer.drawPlayerStats(gui, time, getModel(), this.textViewer);

        gui.flush();
    }

    <T extends Element> void drawElements(GUI gui, List<T> elements, ElementViewer<T> viewer, long time) throws IOException {
        elements
                .forEach(element -> {
                    try {
                        drawElement(gui, viewer, element, time);
                    } catch (IOException e) {
                        // Log the exception at a WARNING level
                        LOGGER.log(Level.WARNING, "Failed to draw element: {0}", e.getMessage());
                        LOGGER.log(Level.FINE, "Stack Trace: ", e);
                    }
                });
    }

    <T extends Element> void drawElement(GUI gui, ElementViewer<T> viewer, T element, long time) throws IOException {
        int adjustedX = (int) element.getPosition().x();
        int adjustedY = (int) element.getPosition().y();
        viewer.draw(element, gui, time, adjustedX, adjustedY);
    }

    private <T extends Element> void drawElements(GUI gui, T[][] elements, ElementViewer<T> viewer, long frameCount) throws IOException {
        for (T[] elementLine : elements) {
            for (T element : elementLine) {
                if (element != null)
                    drawElement(gui, viewer, element, frameCount);
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