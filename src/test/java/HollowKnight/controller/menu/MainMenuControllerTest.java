package HollowKnight.controller.menu;

import HollowKnight.Game;
import HollowKnight.gui.GUI;
import HollowKnight.model.menu.MainMenu;
import HollowKnight.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

class MainMenuControllerTest {
    private Game game;
    private MainMenu mainMenu;
    private ParticleMenuController particleMenuController;
    private MainMenuController mainMenuController;
    private OptionController optionController;

    @BeforeEach
    void setUp() {
        this.game = Mockito.mock(Game.class);
        this.mainMenu = Mockito.mock(MainMenu.class);
        this.optionController = mock(OptionController.class);
        this.particleMenuController = Mockito.mock(ParticleMenuController.class);
        this.mainMenuController = new MainMenuController(mainMenu, particleMenuController, optionController);
    }

    @Test
    void moveUp() throws IOException, URISyntaxException, FontFormatException {
        // Call the move method with the UP action
        mainMenuController.move(game, GUI.ACTION.UP, 0);

        // Verify that previousOption() was called
        verify(mainMenu, times(1)).previousOption();
        verifyNoInteractions(game);
        //verifyNoInteractions(particleMenuController);
    }

    @Test
    void moveDown() throws IOException, URISyntaxException, FontFormatException {
        mainMenuController.move(game, GUI.ACTION.DOWN, 0);

        verify(mainMenu, times(1)).nextOption();
        verifyNoInteractions(game);
        //verifyNoInteractions(particleMenuController);
    }

    @Test
    void selectStart() throws IOException, URISyntaxException, FontFormatException {
        when(mainMenu.isSelectedStart()).thenReturn(true);
        when(mainMenu.isSelectedExit()).thenReturn(false);

        mainMenuController.move(game, GUI.ACTION.SELECT, 100);

        Mockito.verify(optionController, Mockito.times(1))
                .move(eq(game), eq(GUI.ACTION.SELECT), Mockito.anyLong());
        //verifyNoInteractions(particleMenuController);
    }

    @Test
    void selectExit() throws IOException, URISyntaxException, FontFormatException {
        // Mock the behavior for menu selection
        when(mainMenu.isSelectedExit()).thenReturn(true);
        when(mainMenu.isSelectedStart()).thenReturn(false);

        // Call the move method with the SELECT action and a valid time
        mainMenuController.move(game, GUI.ACTION.SELECT, 100);


        // Verify that game.setState(null) was called
        Mockito.verify(optionController, Mockito.times(1))
                .move(eq(game), eq(GUI.ACTION.SELECT), Mockito.anyLong());
        //verifyNoInteractions(particleMenuController);
    }

    @Test
    void delegateToParticleController() throws IOException, URISyntaxException, FontFormatException {
        // Call the move method and check that ParticleMenuController's move is invoked
        mainMenuController.move(game, GUI.ACTION.NULL, 0);

        verify(particleMenuController, times(1)).move(game, GUI.ACTION.NULL, 0);
        verifyNoInteractions(mainMenu);
        verifyNoInteractions(game);
    }

    @Test
    public void actionQuit() throws IOException, URISyntaxException, FontFormatException {
        mainMenuController.move(game, GUI.ACTION.QUIT, 0);
        Mockito.verify(game, Mockito.times(1)).setState(null);
    }
}
