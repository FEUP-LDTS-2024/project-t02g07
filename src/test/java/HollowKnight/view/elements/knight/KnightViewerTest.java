package HollowKnight.view.elements.knight;

import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.Knight.FallingState;
import HollowKnight.model.game.elements.Knight.Knight;
import HollowKnight.model.game.scene.Scene;
import HollowKnight.view.sprites.GameSpriteLoader;
import HollowKnight.view.sprites.Sprite;
import HollowKnight.view.sprites.SpriteLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KnightViewerTest {

    private Scene scene;
    private GUI gui;
    private GameSpriteLoader gameSpriteLoader;

    @BeforeEach
    public void setup() {
        this.scene = mock(Scene.class);
        this.gui = mock(GUI.class);
        this.gameSpriteLoader = mock(GameSpriteLoader.class);
    }

    @Test
    public void draw() throws IOException {
        Sprite sprite1 = mock(Sprite.class);
        Knight player = new Knight(20,30,50,3f, 50);

        KnightViewer knightViewer = new KnightViewer(gameSpriteLoader);
        long frameCount = 1000;

        when(gameSpriteLoader.get("sprites/Knight/movement/falling/pixil-frame-0-reversed.png")).thenReturn(sprite1);

        player.setFacingRight(false);
        player.setState(new FallingState(player));
        knightViewer.draw(player, gui, frameCount,0,0);

        verify(sprite1, atLeast(0))
                .draw(gui, (int)player.getPosition().x(), (int)player.getPosition().y());

        player.setFacingRight(true);
        player.setState(new FallingState(player));
        knightViewer.draw(player, gui, frameCount,0,0);

        verify(sprite1, atLeast(0))
                .draw(gui, (int)player.getPosition().x(), (int)player.getPosition().y());
    }
}