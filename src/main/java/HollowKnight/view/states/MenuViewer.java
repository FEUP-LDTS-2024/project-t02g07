package HollowKnight.view.states;

import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.menu.Menu;
import HollowKnight.model.menu.Option;
import HollowKnight.view.elements.ElementViewer;
import HollowKnight.view.menu.OptionViewer;
import HollowKnight.view.menu.ParticleViewer;
import com.googlecode.lanterna.TextColor;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuViewer extends ScreenViewer<Menu> {

    private final ParticleViewer particleViewer;
    private static final TextColor.RGB unselectedColor = new TextColor.RGB(234,234,234);
    private static final TextColor.RGB selectedColor = new TextColor.RGB(255,234,69);
    private final OptionViewer optionViewer;
    public MenuViewer(Menu model) throws IOException {
        super(model);
        this.optionViewer = new OptionViewer();
        this.particleViewer = new ParticleViewer();
    }

    @Override
    public void draw(GUI gui, long time) throws IOException {
        gui.cls();
        drawBackGround(gui);
        this.drawOptions(gui, getModel().getOptions(), optionViewer, time);
        drawElements(gui, getModel().getParticles(), this.particleViewer, time);
        gui.flush();
    }

    private void drawOptions(GUI gui, List<Option> options, OptionViewer viewer, long time) throws IOException {
        int animationDuration = 40; // Number of ticks for the animation
        int maxOffsetX = 40; // Maximum horizontal movement (how far right to start the animation)

        // Calculate the start time for drawing to begin (when the first option reaches its initial position)
        int firstOptionStartTime = 0;

        // Calculate the time when each option should start sliding (based on its index)
        for (int idx = 0; idx < options.size(); idx++) {
            Option option = options.get(idx);

            // Determine the start time for the first option to begin drawing
            int optionStartTime = idx * animationDuration;  // Each option starts after the previous one finishes
            firstOptionStartTime = optionStartTime;

            // Wait until the first option reaches its initial position
            if (time < firstOptionStartTime) {
                continue; // Skip drawing until the first option reaches its start time
            }

            // Calculate the new x position for the animation (moving from right to left)
            int startPositionX = (int)option.getPosition().x(); // Initial x position
            int currentPositionX = startPositionX;

            if (time >= optionStartTime && time < optionStartTime + animationDuration) {
                // Calculate the movement for the animation: gradually decrease x position
                int movementOffset = (int) (maxOffsetX * (1 - (time - optionStartTime) / (float) animationDuration)); // Moves from right to left
                currentPositionX += movementOffset;
            }

            // Update the option's position with the new x value
            Option updatedOption = new Option(currentPositionX, (int)option.getPosition().y(), option.getText());
            System.out.println("Option pos : " + currentPositionX + ", " + (int)option.getPosition().y() + " " + option.getText());

            // Draw the option with the appropriate color (selected or unselected)
            viewer.draw(updatedOption, gui, getModel().isSelected(idx) ? selectedColor : unselectedColor);
        }
    }

    // Gradient calculation for the selected option (from green to red)
    private TextColor.RGB calculateGradient(int idx, int totalOptions) {
        int r = (int) (255 * (idx / (float) totalOptions));
        int g = (int) (255 * (1 - (idx / (float) totalOptions)));
        return new TextColor.RGB(r, g, 0);
    }

    private void drawBackGround(GUI gui) throws IOException {
        int screenWidth = 160;
        int screenHeight = 90;

        // Background gradient color (Black to White)
        for (int w = 0; w < screenWidth; w++) {
            int colorValue = (int) (255 * w / (screenWidth - 1.0)); // Same for R, G, and B
            TextColor.RGB color = new TextColor.RGB(colorValue, colorValue, colorValue);

            for (int h = 0; h < screenHeight; h++) {
                gui.drawPixel(w, h, color);
            }
        }

        // Border color
        TextColor.RGB borderColor = new TextColor.RGB(25, 25, 25);
        for (int w = 0; w < screenWidth; w++) {
            gui.drawPixel(w, 0, borderColor);
            gui.drawPixel(w, screenHeight - 1, borderColor);
        }
        for (int h = 1; h < screenHeight - 1; h++) {
            gui.drawPixel(0, h, borderColor);
            gui.drawPixel(screenWidth - 1, h, borderColor);
        }
    }

    private  <T extends Element> void drawElements(GUI gui, List<T> elements, ElementViewer<T> viewer, long time) throws IOException {
        for (T element : elements)
            drawElement(gui, element, viewer, time);
    }

    private  <T extends Element> void drawElement(GUI gui, T element, ElementViewer<T> viewer, long time) throws IOException {
        viewer.draw(element, gui, time);
    }
}