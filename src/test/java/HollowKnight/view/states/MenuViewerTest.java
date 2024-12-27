package HollowKnight.view.states;

import HollowKnight.gui.GUI;
import HollowKnight.gui.RescalableGUI;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.menu.*;
import HollowKnight.state.particle.CalmState;
import HollowKnight.view.menu.LogoViewer;
import HollowKnight.view.menu.OptionViewer;
import HollowKnight.view.menu.ParticleViewer;
import HollowKnight.view.sprites.ViewerProvider;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuViewerTest {

    @Mock
    private ViewerProvider viewerProvider;

    @Mock
    private OptionViewer optionViewer;

    @Mock
    private LogoViewer logoViewer;

    @Mock
    private ParticleViewer particleViewer;

    @Mock
    private RescalableGUI gui;

    private MainMenu mainMenu;
    private SettingsMenu settingsMenu;
    private MenuViewer<MainMenu> mainMenuViewer;
    private MenuViewer<SettingsMenu> settingsMenuViewer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Mock viewer provider
        when(viewerProvider.getEntryViewer()).thenReturn(optionViewer);
        when(viewerProvider.getLogoViewer()).thenReturn(logoViewer);

        // Mock menus
        mainMenu = mock(MainMenu.class);
        settingsMenu = mock(SettingsMenu.class);

        when(mainMenu.getParticles()).thenReturn(List.of(new Particle(new Position(0, 0),
                new CalmState(), new TextColor.RGB(25, 25, 25))));
        when(settingsMenu.getParticles()).thenReturn(List.of(new Particle(new Position(0, 0),
                new CalmState(), new TextColor.RGB(25, 25, 25))));
        when(mainMenu.getOptions()).thenReturn(List.of(new Option(10, 10, Option.Type.START_GAME)));
        when(settingsMenu.getOptions()).thenReturn(List.of(new Option(20, 20, Option.Type.EXIT)));

        // Initialize viewers
        mainMenuViewer = new MenuViewer<>(mainMenu, viewerProvider);
        settingsMenuViewer = new MenuViewer<>(settingsMenu, viewerProvider);
    }

    @Test
    void testDrawMainMenu() throws IOException {
        // Configure mock behavior
        when(mainMenu.isSelected(anyInt())).thenReturn(false);
        when(mainMenu.getInGame()).thenReturn(false);

        mainMenuViewer.draw(gui, 50L);

        // Verify background drawing
        verify(gui, atLeastOnce()).cls();
        verify(gui, atLeastOnce()).flush();

        // Verify logo drawing
        verify(logoViewer).draw(gui, 90, 30);

        // Verify options are drawn
        verify(optionViewer).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    void testDrawSettingsMenu() throws IOException {
        // Configure mock behavior
        when(settingsMenu.isSelected(anyInt())).thenReturn(true);
        when(settingsMenu.getInGame()).thenReturn(false);

        settingsMenuViewer.draw(gui, 100L);

        // Verify background drawing
        verify(gui, atLeastOnce()).cls();
        verify(gui, atLeastOnce()).flush();

        // Verify logo drawing
        verify(logoViewer).draw(gui, 90, 30);

        // Verify options with blinking effect
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    @Test
    void testDrawParticles() throws IOException {
        Particle particle = new Particle(new Position(0, 0),
                new CalmState(), new TextColor.RGB(25, 25, 25));
        List<Particle> particles = List.of(particle);

        mainMenuViewer.drawParticles(gui, particles, particleViewer, 50L);

        verify(particleViewer, times(1)).draw(eq(particle), eq(gui), eq(50L), eq(0), eq(0));
    }

    @Test
    void testDrawRetroDynamicBackgroundGray() throws IOException {
        mainMenuViewer.drawRetroDynamicBackground(gui, 50L, true);

        // Verify gray gradient pixels
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    void testDrawRetroDynamicBackgroundColorful() throws IOException {
        settingsMenuViewer.drawRetroDynamicBackground(gui, 50L, false);

        // Verify colorful gradient pixels
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }
}
