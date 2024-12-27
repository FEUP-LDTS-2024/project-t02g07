package HollowKnight.controller.game;

import HollowKnight.Game;
import HollowKnight.gui.GUI;
import HollowKnight.model.credits.Credits;
import HollowKnight.model.game.elements.Knight.Knight;
import HollowKnight.model.game.scene.Scene;
import HollowKnight.model.game.scene.SceneLoader;
import HollowKnight.model.menu.MainMenu;
import HollowKnight.state.CreditsState;
import HollowKnight.state.GameState;
import HollowKnight.state.MainMenuState;
import HollowKnight.view.sprites.GameSpriteLoader;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SceneControllerTest {
    private Game game;
    private SceneController sceneController;
    private Scene scene;
    private PlayerController playerController;
    private ParticleController particleController;
    private EnemieController enemieController;
    private GameSpriteLoader gameSpriteLoader;
    @BeforeEach
    void setUp() {
        this.game = Mockito.mock(Game.class);
        this.scene = Mockito.mock(Scene.class);
        Knight knight = Mockito.mock(Knight.class);
        Mockito.when(scene.getPlayer()).thenReturn(knight);
        this.playerController = Mockito.mock(PlayerController.class);
        this.particleController = Mockito.mock(ParticleController.class);
        this.enemieController = Mockito.mock(EnemieController.class);
        this.sceneController = new SceneController(scene, playerController, particleController, enemieController);
        this.gameSpriteLoader = new GameSpriteLoader();
        when(game.getSpriteLoader()).thenReturn(gameSpriteLoader);
    }

    @Test
    void move() throws IOException {
        sceneController.move(game, GUI.ACTION.NULL, 0);
        Mockito.verify(playerController, Mockito.times(1))
                .move(game, GUI.ACTION.NULL, 0);
        Mockito.verify(game, Mockito.times(0))
                .setState(any());
        Mockito.verify(particleController, Mockito.times(1))
                .move(game, GUI.ACTION.NULL, 0);
    }

    @Test
    void moveWithQuit() throws IOException {
        sceneController.move(game, GUI.ACTION.QUIT, 0);
        Mockito.verify(playerController, Mockito.times(0))
                .move(game, GUI.ACTION.NULL, 0);
        Mockito.verify(game, Mockito.times(1))
                .setState(any(MainMenuState.class));
        Mockito.verify(particleController, Mockito.times(0))
                .move(game, GUI.ACTION.NULL, 0);
    }

    @Property
    void allArenasAreClosed(@ForAll @IntRange(min = 3, max = 50) int width, @ForAll @IntRange(min = 3, max = 50) int height,
                                @ForAll List<GUI.@From("moveActions") ACTION> actions) throws IOException
    {
        SceneLoader sceneLoader = new SceneLoader(0);
        Scene newScene = sceneLoader.createScene(new Knight(1,1,50,5f, 6)); //TODO add knight
        PlayerController controller = new PlayerController(newScene);

        for (GUI.ACTION action : actions) {
            controller.move(null, action, 100);
            assert (controller.getModel().getPlayer().getPosition().x() > 0);
            assert (controller.getModel().getPlayer().getPosition().y() > 0);
            /*assert (controller.getModel().getPlayer().getPosition().x() < width+1);
            assert (controller.getModel().getPlayer().getPosition().y() < height+1);*/
        }
    }

    @Test
    void moveWithEndPositionAndEnoughOrbs() throws IOException {
        // Arrange
        Knight knight = Mockito.mock(Knight.class);
        when(scene.getPlayer()).thenReturn(knight);

        // Mock knight's orbs and scene's state
        when(knight.getOrbs()).thenReturn(6); // mock 3 * (sceneID + 1), e.g., sceneID = 1 -> 3*2 = 6
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(1); // mock the sceneID value
        when(game.getNumberOfLevels()).thenReturn(3); // Assume there are 3 levels

        // Set up the mock behavior for the transition to a new scene
        SceneLoader sceneLoader = Mockito.mock(SceneLoader.class);
        Scene newScene = Mockito.mock(Scene.class);
        when(sceneLoader.createScene(any(Knight.class))).thenReturn(newScene);

        // Act
        sceneController.move(game, GUI.ACTION.NULL, 0);

        // Assert
        // Check if the correct scene transition happens (move to next scene)
        Mockito.verify(game, Mockito.times(1)).setState(Mockito.any(GameState.class));

        // Verify the scene loading logic and ensure no Credits state is set since not on last level
        //Mockito.verify(sceneLoader, Mockito.times(1)).createScene(any(Knight.class));
        Mockito.verify(game, Mockito.times(0)).setState(Mockito.any(CreditsState.class));
    }

    @Test
    void moveWithEndPositionAndCreditsState() throws IOException {
        // Arrange
        Knight knight = Mockito.mock(Knight.class);
        when(scene.getPlayer()).thenReturn(knight);

        // Mock knight's orbs and scene's state
        when(knight.getOrbs()).thenReturn(9); // mock 3 * (sceneID + 1), e.g., sceneID = 2 -> 3*3 = 9
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(2); // mock last scene (2)
        when(game.getNumberOfLevels()).thenReturn(3); // Assume there are 3 levels

        // Mock credits state setup
        Credits credits = new Credits(knight);
        when(game.getSpriteLoader()).thenReturn(gameSpriteLoader);

        // Act
        sceneController.move(game, GUI.ACTION.NULL, 0);

        // Assert
        // Ensure the Credits state is set as we are on the last level
        Mockito.verify(game, Mockito.times(1)).setState(Mockito.any(CreditsState.class));

        // Ensure no scene loading for the next level
        Mockito.verify(game, Mockito.times(0)).setState(Mockito.any(GameState.class));
    }


    @Provide
    Arbitrary<GUI.ACTION> moveActions() {
        return Arbitraries.of( GUI.ACTION.RIGHT, GUI.ACTION.LEFT);
    }
}