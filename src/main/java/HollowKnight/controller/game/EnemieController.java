package HollowKnight.controller.game;

import HollowKnight.Game;
import HollowKnight.controller.Controller;
import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.enemies.Enemies;
import HollowKnight.model.game.elements.enemies.GhostMonster;
import HollowKnight.model.game.elements.enemies.PurpleMonster;
import HollowKnight.model.game.elements.enemies.SwordMonster;
import HollowKnight.model.game.scene.Scene;

import java.io.IOException;

public class EnemieController extends Controller<Scene> {
    private long lastMovement;
    public EnemieController(Scene scene) {
        super(scene);
        this.lastMovement = 0;
    }

    @Override
    public void move(Game game, GUI.ACTION action, long time) throws IOException {
        if (time - lastMovement > 2) {
            GhostMonster[][] ghostMonsters = getModel().getMinhoteMonsters();
            for (GhostMonster[] row : ghostMonsters) {
                for (GhostMonster enemy : row) {
                    if (enemy != null) {
                        moveMonster(enemy);
                    }
                }
            }
            PurpleMonster[][] purpleMonsters = getModel().getPurpleMonsters();
            for (PurpleMonster[] row : purpleMonsters) {
                for (PurpleMonster enemy : row) {
                    if (enemy != null) {
                        moveMonster(enemy);
                    }
                }
            }
            SwordMonster[][] swordMonsters = getModel().getSwordMonsters();
            for (SwordMonster[] row : swordMonsters) {
                for (SwordMonster enemy : row) {
                    if (enemy != null) {
                        moveMonster(enemy);
                    }
                }
            }
            this.lastMovement = time;
        }
    }

    private void moveMonster(Enemies enemies) {
        enemies.setPosition(enemies.moveMonster());
    }
}
