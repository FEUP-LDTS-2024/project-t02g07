package HollowKnight.controller.menu;

import HollowKnight.Game;
import HollowKnight.gui.GUI;
import HollowKnight.gui.RescalableGUI;
import HollowKnight.model.menu.Menu;
import HollowKnight.model.menu.Option;
import HollowKnight.state.GameState;
import HollowKnight.state.MainMenuState;
import HollowKnight.state.SettingsMenuState;
import HollowKnight.state.State;
import HollowKnight.view.sprites.GameSpriteLoader;
import HollowKnight.view.sprites.SpriteLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class OptionControllerTest {
    private Game game;
    private Menu menu;
    private OptionController optionController;
    private GameSpriteLoader gameSpriteLoader;

    @BeforeEach
    public void setup(){
        this.game = Mockito.mock(Game.class);
        this.menu = Mockito.mock(Menu.class);
        doNothing().when(game).setState(isA(State.class));
        SpriteLoader spriteLoader = Mockito.mock(SpriteLoader.class);
        when(game.getSpriteLoader()).thenReturn(spriteLoader);

        this.optionController = new OptionController(menu);
    }

    @Test
    public void moveStartGame() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.START_GAME);
        when(menu.getCurrentOption()).thenReturn(e);

        optionController.move(game, GUI.ACTION.NULL, 0);
        verify(game, Mockito.times(0))
                .setState(Mockito.any(State.class));

        optionController.move(game, GUI.ACTION.SELECT, 0);
        verify(game, Mockito.times(1))
                .setState(Mockito.any(GameState.class));
    }

    @Test
    public void moveSettings() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.SETTINGS);
        when(menu.getCurrentOption()).thenReturn(e);

        optionController.move(game, GUI.ACTION.NULL, 0);
        verify(game, Mockito.times(0))
                .setState(Mockito.any(State.class));

        optionController.move(game, GUI.ACTION.SELECT, 0);
        verify(game, Mockito.times(1))
                .setState(Mockito.any(SettingsMenuState.class));
    }

    @Test
    public void moveExit() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.EXIT);
        when(menu.getCurrentOption()).thenReturn(e);

        optionController.move(game, GUI.ACTION.NULL, 0);
        verify(game, Mockito.times(0))
                .setState(null);

        optionController.move(game, GUI.ACTION.SELECT, 0);
        verify(game, Mockito.times(1))
                .setState(null);
    }

    @Test
    public void moveMainMenu() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.TO_MAIN_MENU);
        when(menu.getCurrentOption()).thenReturn(e);

        optionController.move(game, GUI.ACTION.NULL, 0);
        verify(game, Mockito.times(0))
                .setState(Mockito.any(State.class));

        optionController.move(game, GUI.ACTION.SELECT, 0);
        verify(game, Mockito.times(1))
                .setState(Mockito.any(MainMenuState.class));


    }

    @Test
    public void moveResolutionRight() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.RESOLUTION);
        when(menu.getCurrentOption()).thenReturn(e);

        optionController.move(game, GUI.ACTION.NULL, 0);
        verify(game, Mockito.times(0))
                .getResolution();

        for (int idx = 0; idx < RescalableGUI.ResolutionScale.values().length - 1; idx++){
            when(game.getResolution()).thenReturn(RescalableGUI.ResolutionScale.values()[idx]);
            optionController.move(game, GUI.ACTION.RIGHT, 0);

            verify(game, Mockito.times(idx+1))
                    .getResolution();
            verify(game, Mockito.times(1))
                    .setResolution(RescalableGUI.ResolutionScale.values()[idx+1]);
        }
    }

    @Test
    public void moveResolutionLeft() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.RESOLUTION);
        when(menu.getCurrentOption()).thenReturn(e);

        optionController.move(game, GUI.ACTION.NULL, 0);
        verify(game, Mockito.times(0))
                .getResolution();

        for (int idx = 1; idx < RescalableGUI.ResolutionScale.values().length; idx++){
            when(game.getResolution()).thenReturn(RescalableGUI.ResolutionScale.values()[idx]);
            optionController.move(game, GUI.ACTION.LEFT, 0);

            verify(game, Mockito.times(idx))
                    .getResolution();
            verify(game, Mockito.times(1))
                    .setResolution(RescalableGUI.ResolutionScale.values()[idx-1]);
        }
    }

    @Test
    public void moveResolutionSpecialCaseRight() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.RESOLUTION);
        when(menu.getCurrentOption()).thenReturn(e);
        when(game.getResolution()).thenReturn(RescalableGUI.ResolutionScale.values()[RescalableGUI.ResolutionScale.values().length-1]);

        optionController.move(game, GUI.ACTION.RIGHT, 0);
        verify(game, Mockito.times(1))
                .getResolution();
        verify(game, Mockito.times(0))
                .setResolution(Mockito.any());

    }

    @Test
    public void moveResolutionSpecialCaseLeft1() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.RESOLUTION);
        when(menu.getCurrentOption()).thenReturn(e);
        when(game.getResolution()).thenReturn(null);

        optionController.move(game, GUI.ACTION.LEFT, 0);
        verify(game, Mockito.times(1))
                .getResolution();
        verify(game, Mockito.times(0))
                .setResolution(Mockito.any());
    }

    @Test
    public void moveResolutionSpecialCaseLeft2() throws IOException, URISyntaxException, FontFormatException {
        Option e = new Option(0,0, Option.Type.RESOLUTION);
        when(menu.getCurrentOption()).thenReturn(e);
        when(game.getResolution()).thenReturn(RescalableGUI.ResolutionScale.values()[0]);

        optionController.move(game, GUI.ACTION.LEFT, 0);
        verify(game, Mockito.times(1))
                .getResolution();
        verify(game, Mockito.times(1))
                .setResolution(null);
    }

}