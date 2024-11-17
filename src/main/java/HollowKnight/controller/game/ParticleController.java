package HollowKnight.controller.game;

import HollowKnight.Game;
import HollowKnight.controller.Controller;
import HollowKnight.gui.GUI;
import HollowKnight.model.Position;
import HollowKnight.model.game.elements.Particle.Particle;
import HollowKnight.model.game.scene.Scene;

import java.io.IOException;
import java.util.Random;

public class ParticleController extends Controller<Scene>{

    public ParticleController(Scene model) {
        super(model);
    }

    @Override
    public void move(Game game, GUI.ACTION action, long time) throws IOException {
        for(Particle particle: getModel().getParticles()){
            particle.setPosition(ParticleMove(particle));
        }
    }
    public Position ParticleMove(Particle particle){
        Position currentPos = particle.getPosition();
        if (currentPos == null) {
            throw new IllegalStateException("Particle position is null");
        }
        int new_x = (int)particle.getPosition().x() + 2;
        int new_y = (int)particle.getPosition().y() + 2;
        if (new_x < 0)
            new_x = getModel().getWidth() - 1;
        else if (new_x >= getModel().getWidth()) {
            new_x = 1;
        }
        if (new_y >= getModel().getHeight())
            new_y = 0;
        return new Position(new_x,new_y);
    }
}
