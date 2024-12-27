package HollowKnight.controller.game;

import HollowKnight.Game;
import HollowKnight.gui.GUI;
import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.game.elements.Particle.Particle;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class ParticleControllerTest {
    private Game game;
    private ParticleController particleController;
    private Scene scene;
    @BeforeEach
    void setUp() {
         this.game = Mockito.mock(Game.class);
         this.scene = Mockito.mock(Scene.class);
         this.particleController = new ParticleController(scene);
    }

    @Test
    void move() throws IOException {
        // Mock Particle objects
        Particle rainParticle = Mockito.mock(Particle.class);
        Particle jumpParticle = Mockito.mock(Particle.class);
        Particle doubleJumpParticle = Mockito.mock(Particle.class);
        Particle respawnParticle = Mockito.mock(Particle.class);
        Particle dashParticle = Mockito.mock(Particle.class);
        // Use real Position instances for clarity
        Position position1 = new Position(5, 5);
        Position position2 = new Position(6, 6);
        when(rainParticle.getPosition()).thenReturn(position1);
        when(jumpParticle.getPosition()).thenReturn(position2);

        // Mock the moveParticle method for all particles
        when(rainParticle.moveParticle(scene, 0)).thenReturn(new Position(5, 6));
        when(jumpParticle.moveParticle(scene, 0)).thenReturn(new Position(6, 7));
        when(doubleJumpParticle.moveParticle(scene, 0)).thenReturn(new Position(7, 8));
        when(respawnParticle.moveParticle(scene, 0)).thenReturn(new Position(8, 9));
        when(dashParticle.moveParticle(scene, 0)).thenReturn(new Position(8, 9));



        // Mock the particle lists in Scene
        List<Particle> rainParticles = Arrays.asList(rainParticle, rainParticle);
        List<Particle> jumpParticles = Arrays.asList(jumpParticle);
        List<Particle> doubleJumpParticles = Arrays.asList(doubleJumpParticle);
        List<Particle> respawnParticles = Arrays.asList(respawnParticle);
        List<Particle> dashParticles = Arrays.asList(dashParticle);

        when(scene.getParticles()).thenReturn(rainParticles);
        when(scene.getJumpParticles()).thenReturn(jumpParticles);
        when(scene.getDoubleJumpParticles()).thenReturn(doubleJumpParticles);
        when(scene.getRespawnParticles()).thenReturn(respawnParticles);
        when(scene.getDashParticles()).thenReturn(dashParticles);


        // Invoke the move method
        particleController.move(game, GUI.ACTION.NULL, 0);

        // Verify that setScene and setPosition are called for all particles
        verify(rainParticle, times(2)).setScene(scene);
        verify(rainParticle, times(2)).setPosition(new Position(5, 6));

        verify(jumpParticle, times(1)).setScene(scene);
        verify(jumpParticle, times(1)).setPosition(new Position(6, 7));

        verify(doubleJumpParticle, times(1)).setScene(scene);
        verify(doubleJumpParticle, times(1)).setPosition(new Position(7, 8));

        verify(respawnParticle, times(1)).setScene(scene);
        verify(respawnParticle, times(1)).setPosition(new Position(8, 9));

        verify(dashParticle, times(1)).setScene(scene);
        verify(dashParticle, times(1)).setPosition(new Position(8, 9));
    }

}