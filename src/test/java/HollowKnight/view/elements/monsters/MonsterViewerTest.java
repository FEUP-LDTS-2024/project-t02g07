package HollowKnight.view.elements.monsters;

import HollowKnight.gui.GUI;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.game.elements.enemies.Enemies;
import HollowKnight.view.sprites.Sprite;
import HollowKnight.view.sprites.SpriteLoader;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonsterViewerTest {

    private MonsterViewer monsterViewer;
    private SpriteLoader spriteLoader;
    Sprite minhoteSprite;
    Sprite purpleMonsterSprite;
    Sprite swordMonsterSprite1;
    Sprite swordMonsterSprite2;
    private GUI gui;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        gui = mock(GUI.class);
        // Mock sprite loading
        minhoteSprite = mock(Sprite.class);
        purpleMonsterSprite = mock(Sprite.class);
        swordMonsterSprite1 = mock(Sprite.class);
        swordMonsterSprite2 = mock(Sprite.class);

        when(spriteLoader.get("sprites/Enemies/MinhoteMonster.png")).thenReturn(minhoteSprite);
        when(spriteLoader.get("sprites/Enemies/PurpleMonster.png")).thenReturn(purpleMonsterSprite);
        when(spriteLoader.get("sprites/Enemies/swordMonster-Attack0.png")).thenReturn(swordMonsterSprite1);
        when(spriteLoader.get("sprites/Enemies/swordMonster-Attack1.png")).thenReturn(swordMonsterSprite2);

        monsterViewer = new MonsterViewer(spriteLoader);
    }

    @Test
    void testMonsterViewerInitialization() throws IOException {
        // Verify sprite loading
        verify(spriteLoader).get("sprites/Enemies/MinhoteMonster.png");
        verify(spriteLoader).get("sprites/Enemies/PurpleMonster.png");
        verify(spriteLoader).get("sprites/Enemies/swordMonster-Attack0.png");
        verify(spriteLoader).get("sprites/Enemies/swordMonster-Attack1.png");
    }

    @Test
    void testDrawSwordMonster() throws IOException {
        Enemies swordMonster = mock(Enemies.class);
        when(swordMonster.getChar()).thenReturn('E');
        when(swordMonster.getPosition()).thenReturn(new Position(10, 20));

        monsterViewer.draw(swordMonster, gui, 30, 0, 0);

        // Verify sprite drawing logic for sword monster animation
        verify(swordMonsterSprite1, times(1)).draw(gui,
                (int)swordMonster.getPosition().x() - 4, (int)swordMonster.getPosition().y());
    }

    @Test
    void testDrawGhostMonster() throws IOException {
        Enemies ghostMonster = mock(Enemies.class);
        when(ghostMonster.getChar()).thenReturn('m');
        when(ghostMonster.getPosition()).thenReturn(new Position(50, 50));

        monsterViewer.draw(ghostMonster, gui, 0, 0, 0);

        // Verify specific drawing for ghost monster
        verify(gui).drawHitBox(eq(50), eq(50), eq(4), eq(4), any(TextColor.RGB.class));
        verify(gui, never()).drawPixel(eq(50), eq(50), any(TextColor.RGB.class));
    }

    @Test
    void testDrawPurpleMonster() throws IOException {
        Enemies purpleMonster = mock(Enemies.class);
        when(purpleMonster.getChar()).thenReturn('l');
        when(purpleMonster.getPosition()).thenReturn(new Position(30, 40));

        monsterViewer.draw(purpleMonster, gui, 0, 0, 0);

        // Verify purple monster drawing logic
        //ArgumentCaptor<Sprite> spriteCaptor = ArgumentCaptor.forClass(Sprite.class);
        verify(purpleMonsterSprite, times(1)).draw(gui,
                (int)purpleMonster.getPosition().x() - 4, (int)purpleMonster.getPosition().y() - 1);
    }

    @Test
    void testDrawInvalidMonsterTypeThrowsException() {
        Enemies invalidMonster = mock(Enemies.class);
        when(invalidMonster.getChar()).thenReturn('x');

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> monsterViewer.draw(invalidMonster, gui, 0, 0, 0));

        assertEquals("No sprite for character: x", thrown.getMessage());
    }

    @Test
    void testGetSpriteForAnimation() throws IOException {
        Sprite sprite1 = mock(Sprite.class);
        Sprite sprite2 = mock(Sprite.class);
        monsterViewer = new MonsterViewer(spriteLoader);
        List<Sprite> sprites = List.of(sprite1, sprite2);

        // Verify correct sprite is selected based on time
        Sprite resultSprite1 = monsterViewer.getSpriteForAnimation(sprites, 0);
        Sprite resultSprite2 = monsterViewer.getSpriteForAnimation(sprites, 15);
        Sprite resultSprite3 = monsterViewer.getSpriteForAnimation(sprites, 30);

        assertSame(sprite1, resultSprite1); // Frame index = 0
        assertSame(sprite2, resultSprite2); // Frame index = 1
        assertSame(sprite1, resultSprite3); // Frame index loops back to 0
    }
}
