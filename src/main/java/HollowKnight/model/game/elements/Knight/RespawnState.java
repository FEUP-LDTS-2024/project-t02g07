package HollowKnight.model.game.elements.Knight;


import HollowKnight.model.Position;
import HollowKnight.model.Vector;
import HollowKnight.model.game.elements.Collectables.Collectables;
import HollowKnight.model.game.elements.Collectables.EnergyOrb;
import HollowKnight.model.game.elements.Collectables.HealthOrb;
import HollowKnight.model.game.elements.Collectables.SpeedOrb;
import HollowKnight.model.game.scene.Scene;
import HollowKnight.model.game.scene.SceneLoader;

import java.io.IOException;

public class RespawnState extends KnightState {

    private long deathTimer;

    public RespawnState(Knight knight, long deathTimer) {
        super(knight);
        this.deathTimer = deathTimer;
        knight.getScene().setRespawnParticles(getKnight().createRespawnParticles(450));
    }

    @Override
    public Vector jump() {
        return updateVelocity(getKnight().getVelocity());
    }

    @Override
    public Vector dash() {
        return updateVelocity(getKnight().getVelocity());
    }

    @Override
    public Vector updateVelocity(Vector newVelocity) {
        deathTimer--;
        tickParticles();
        return new Vector(0, 0);
    }

    @Override
    public KnightState getNextState() throws IOException {
        Scene scene = getKnight().getScene();

        if (deathTimer <= 0) {
            SceneLoader sceneLoader = new SceneLoader(scene.getSceneID());
            sceneLoader.setOrbs(scene);
            getKnight().setOrbs(0);
            getKnight().setHP(50);
            getKnight().setPosition(scene.getStartPosition());
            getKnight().setGotHit(false);           //if player dies to damage then he resets the boolean to receive damage
            return new FallingState(getKnight());
        }
        return this;
    }
}