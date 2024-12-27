package HollowKnight.view.states;

import HollowKnight.gui.GUI;
import HollowKnight.gui.RescalableGUI;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.game.elements.Collectables.Collectables;
import HollowKnight.model.game.elements.Collectables.SpeedOrb;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.elements.Knight.Knight;
import HollowKnight.model.game.elements.Particle.DashParticle;
import HollowKnight.model.game.elements.Particle.RainParticle;
import HollowKnight.model.game.elements.Spike;
import HollowKnight.model.game.elements.enemies.Enemies;
import HollowKnight.model.game.elements.enemies.PurpleMonster;
import HollowKnight.model.game.elements.tile.Tile;
import HollowKnight.model.game.scene.Scene;
import HollowKnight.view.elements.collectables.OrbViewer;
import HollowKnight.view.elements.monsters.MonsterViewer;
import HollowKnight.view.elements.particle.ParticleViewer;
import HollowKnight.view.elements.knight.KnightViewer;
import HollowKnight.view.elements.rocks.RockViewer;
import HollowKnight.view.elements.spike.SpikeViewer;
import HollowKnight.view.elements.tile.TileViewer;
import HollowKnight.view.elements.tree.TreeViewer;
import HollowKnight.view.sprites.ViewerProvider;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameViewerTest {

    private Scene scene;
    private ViewerProvider viewerProvider;
    private ParticleViewer particleViewer;
    private KnightViewer playerViewer;
    private SpikeViewer spikeViewer;
    private OrbViewer orbViewer;
    private TileViewer tileViewer;
    private RescalableGUI gui;
    private TreeViewer treeViewer;
    private RockViewer rockViewer;
    private MonsterViewer monsterViewer;

    private GameViewer gameViewer;

    @BeforeEach
    public void setup() throws IOException {
        this.scene = new Scene(10, 10, 0);

        scene.setPlayer(new Knight(0, 0, 50,0,0));

        scene.getPlayer().setScene(scene);

        scene.setOrbs(new Collectables[][] { { null, new SpeedOrb(8, 0,1.1,'s') }, { null, null }});

        scene.setTiles(new Tile[][] {
                { null, null },
                { new Tile(0, 8, 'T'), null } // Ensure non-null tiles
        });

        scene.setSpikes(new Spike[][] {
                { null, null },
                { null, new Spike(8, 8, '^') } // Ensure correct positioning
        });

        scene.setParticles(List.of(
                new RainParticle(0, 8, new Position(2, 2), new TextColor.RGB(25, 25, 25))
        ));

        scene.setDashParticles(List.of(
                new DashParticle(0, 8, new Position(2, 2), new TextColor.RGB(25, 25, 25))
        ));


        List<Enemies> monsters = new ArrayList<>();
        monsters.add(new PurpleMonster(0,0,50,scene,0,new Position(0,0),'p'));

        scene.setMonsters(monsters);

        this.viewerProvider = mock(ViewerProvider.class);
        this.particleViewer = mock(ParticleViewer.class);
        this.playerViewer = mock(KnightViewer.class);
        this.spikeViewer = mock(SpikeViewer.class);
        this.orbViewer = mock(OrbViewer.class);
        this.tileViewer = mock(TileViewer.class);
        this.treeViewer = mock(TreeViewer.class);
        this.rockViewer = mock(RockViewer.class);
        this.monsterViewer = mock(MonsterViewer.class);
        this.gui = mock(RescalableGUI.class);

        when(viewerProvider.getParticleViewer()).thenReturn(particleViewer);
        when(viewerProvider.getPlayerViewer()).thenReturn(playerViewer);
        when(viewerProvider.getSpikeViewer()).thenReturn(spikeViewer);
        when(viewerProvider.getOrbViewer()).thenReturn(orbViewer);
        when(viewerProvider.getTileViewer()).thenReturn(tileViewer);
        when(viewerProvider.getMonsterViewer()).thenReturn(monsterViewer);
        when(viewerProvider.getTreeViewer()).thenReturn(treeViewer);
        when(viewerProvider.getRockViewer()).thenReturn(rockViewer);
        when(viewerProvider.getMonsterViewer()).thenReturn(monsterViewer);

        // Initialize the game viewer
        gameViewer = new GameViewer(scene, viewerProvider);
    }

    @Test
    void testCalculateCameraBounds() {
        int[] bounds = gameViewer.calculateCameraBounds();
        assertEquals(0, bounds[0]);
        assertEquals(0, bounds[1]);
    }
    /*
    @Test
    void testUpdateStaticLayer() throws IOException {
        int[] cameraBounds = {0, 0, 5, 5};
        gameViewer.updateStaticLayer(cameraBounds);

        verify(viewerProvider.getTileViewer(), times(1))
                .draw(any(), any(), eq(0L), anyInt(), anyInt());
        verify(viewerProvider.getSpikeViewer(), times(1))
                .draw(any(), any(), eq(0L), anyInt(), anyInt());
    }

    @Test
    void testDrawStaticLayer() {
        // Mock a GUI
        gameViewer.staticLayer = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        int[] cameraBounds = gameViewer.calculateCameraBounds();

        gameViewer.drawStaticLayer(gui);

        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any());
    }

    @Test
    void testDraw() throws IOException {
        gameViewer.draw(gui, 1L);

        verify(gui, times(1)).cls();
        verify(gui, times(1)).flush();
        verify(viewerProvider.getPlayerViewer(), times(1)).draw(any(Knight.class), any(), eq(1L), anyInt(), anyInt());
    }
    */

    @Test
    public void draw() throws IOException {
        long frameCount = 100;
        int screenWidth = 16, screenHeight = 16;

        // Mock GUI dimensions
        when(gui.getWidth()).thenReturn(screenWidth);
        when(gui.getHeight()).thenReturn(screenHeight);

        // Call the draw method
        gameViewer.draw(gui, frameCount);

        // Verify initial clearing
        verify(gui, times(1)).cls();

        // Verify the draw calls for various layers and elements
        verify(playerViewer, times(1)).draw(scene.getPlayer(), gui, frameCount, 0, 0);
        verify(particleViewer, times(1)).draw(scene.getParticles().get(0), gui, frameCount, 0, 0);
        verify(particleViewer, times(1)).draw(scene.getDashParticles().get(0), gui, frameCount, 0, 0);

        // Verify static layers
        Tile expectedTile = scene.getTiles()[1][0];
        Spike expectedSpike = scene.getSpikes()[1][1];

        // Tiles and spikes should be checked for proper calls
        if (expectedTile != null) {
            verify(tileViewer, times(1)).draw(expectedTile, gui, frameCount, 0, 0);
        }

        if (expectedSpike != null) {
            verify(spikeViewer, times(1)).draw(expectedSpike, gui, frameCount, 0, 0);
        }

        // Ensure GUI flush was called
        verify(gui, times(1)).flush();
    }

    @Test
    void testIsElementInCamera() {
        Element elementInside = new SpeedOrb(5, 5, 1.2, 's');
        Element elementOutside = new SpeedOrb(15, 15, 1.2, 's');

        int[] cameraBounds = {0, 0, 10, 10};

        assertTrue(gameViewer.isElementInCamera(elementInside, cameraBounds));
        assertFalse(gameViewer.isElementInCamera(elementOutside, cameraBounds));
    }

    @Test
    void testDynamicGradientBackground() throws IOException {
        GUI mockGUI = mock(GUI.class);

        // Call the dynamic background rendering method
        gameViewer.dynamicGradientBackground(mockGUI, 1000L);  // Simulating the time-based effect

        // Ensure GUI draws pixels correctly by verifying the method call
        verify(mockGUI, atLeastOnce()).drawPixel(anyInt(), anyInt(), any());
    }

    @Test
    void testDynamicGradientBackgroundFlashActive() {
        long time = 10; // Flash active: time % 800 < 20

        gameViewer.dynamicGradientBackground(gui, time);

        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(new TextColor.RGB(255, 255, 255)));
    }

    @Test
    void testDynamicGradientBackgroundAfterEffectActive() {
        long time = 30; // Aftereffect active: 20 <= time % 800 < 60

        gameViewer.dynamicGradientBackground(gui, time);

        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());

        boolean isAfterEffectColor = colorCaptor.getAllValues().stream()
                .anyMatch(color -> color.getRed() > 0 && color.getGreen() > 0 && color.getBlue() > 0);
        assertTrue(isAfterEffectColor, "Aftereffect active colors should be subtly brighter.");
    }

    @Test
    void testGameViewerInitWithMockDependencies() {
        // Ensure that after initialization with mocked dependencies, the components are available
        assertNotNull(gameViewer.textViewer);
        assertNotNull(gameViewer.particleViewer);
        assertNotNull(gameViewer.knightViewer);
        assertNotNull(gameViewer.tileViewer);
        assertNotNull(gameViewer.monsterViewer);
    }

}