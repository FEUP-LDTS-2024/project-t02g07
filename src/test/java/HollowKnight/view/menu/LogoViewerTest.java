package HollowKnight.view.menu;

import HollowKnight.gui.GUI;
import HollowKnight.view.sprites.Sprite;
import HollowKnight.view.sprites.SpriteLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class LogoViewerTest {
    private SpriteLoader spriteLoader;
    private Sprite sprite;
    private GUI gui;

    @BeforeEach
    public void setup() throws IOException {
        this.spriteLoader = mock(SpriteLoader.class);
        this.sprite = mock(Sprite.class);
        this.gui = mock(GUI.class);

        when(spriteLoader.get("icon/gameIcon.png")).thenReturn(sprite);
    }

    @Test
    public void draw() throws IOException {
        LogoViewer logoViewer = new LogoViewer(spriteLoader);
        int x = 20, y = 30;

        logoViewer.draw(gui, x, y);

        verify(sprite, times(1)).draw(gui, x, y);
    }
}