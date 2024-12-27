package HollowKnight.controller.menu;

import HollowKnight.Game;
import HollowKnight.gui.GUI;
import HollowKnight.model.menu.SettingsMenu;
import HollowKnight.state.MainMenuState;
import HollowKnight.view.sprites.SpriteLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

class SettingsMenuControllerTest {
    private Game game;
    private SettingsMenu settingsMenu;
    private OptionController optionController;
    private SettingsMenuController settingsMenuController;
    private ParticleMenuController particleMenuController;

    @BeforeEach
    public void setup() {
        this.game = Mockito.mock(Game.class);
        this.settingsMenu = Mockito.mock(SettingsMenu.class);
        this.optionController = Mockito.mock(OptionController.class);
        this.particleMenuController = Mockito.mock(ParticleMenuController.class);
        this.settingsMenuController = new SettingsMenuController(settingsMenu, particleMenuController,  optionController);
        SpriteLoader spriteLoader = Mockito.mock(SpriteLoader.class);
        Mockito.when(game.getSpriteLoader()).thenReturn(spriteLoader);
    }

    @Test
    public void actionDownOption() throws IOException, URISyntaxException, FontFormatException {
        for (int counter = 1; counter < 20; counter++){
            settingsMenuController.move(game, GUI.ACTION.DOWN, 0);
            Mockito.verify(settingsMenu, Mockito.times(counter)).nextOption();
        }
    }

    @Test
    public void actionUpOption() throws IOException, URISyntaxException, FontFormatException {
        for (int counter = 1; counter < 20; counter++){
            settingsMenuController.move(game, GUI.ACTION.UP, 0);
            Mockito.verify(settingsMenu, Mockito.times(counter)).previousOption();
        }
    }

    @Test
    public void actionQuit() throws IOException, URISyntaxException, FontFormatException {
        settingsMenuController.move(game, GUI.ACTION.QUIT, 0);
        Mockito.verify(game, Mockito.times(1)).setState(Mockito.any(MainMenuState.class));
    }

    @Test
    public void actionOthers() throws IOException, URISyntaxException, FontFormatException {
        settingsMenuController.move(game, GUI.ACTION.NULL, 0);
        Mockito.verify(optionController, Mockito.times(1))
                .move(eq(game), eq(GUI.ACTION.NULL), Mockito.anyLong());
    }
}