package HollowKnight.view.elements.monsters;

import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.enemies.Enemies;
import HollowKnight.view.elements.ElementViewer;
import HollowKnight.view.sprites.Sprite;
import HollowKnight.view.sprites.SpriteLoader;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;
import java.util.*;

public class MonsterViewer implements ElementViewer<Enemies> {
    private final Map<Character, List<Sprite>> monstersMap;

    public MonsterViewer(SpriteLoader spriteLoader) throws IOException {
        monstersMap = new HashMap<>();

        // Load sprites for different monster types
        monstersMap.put('m', Collections.singletonList(spriteLoader.get("sprites/Enemies/MinhoteMonster.png")));
        monstersMap.put('l', Collections.singletonList(spriteLoader.get("sprites/Enemies/PurpleMonster.png")));

        List<Sprite> swordMonsterAttackSprites = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            swordMonsterAttackSprites.add(spriteLoader.get("sprites/Enemies/swordMonster-Attack" + i + ".png"));
        }
        monstersMap.put('E', swordMonsterAttackSprites);
    }

    @Override
    public void draw(Enemies model, GUI gui, long time, int offsetX, int offsetY) throws IOException {
        char monsterType = model.getChar(); // Assume getType returns a key representing the monster (e.g., 'h', 'e', 'E').
        List<Sprite> sprites = monstersMap.get(monsterType);
        if (sprites == null) {
            throw new IllegalArgumentException("No sprite for character: " + model.getChar());
        }

        if (monsterType == 'E') { // SwordMonster logic
            Sprite sprite = getSpriteForAnimation(sprites, time);
            sprite.draw(gui, (int) model.getPosition().x() - 4, (int) model.getPosition().y());
        } else if (monsterType == 'm') { // GhostMonster logic
            sprites.get(0).draw(gui, (int) model.getPosition().x() - 4, (int) model.getPosition().y() - 6);
            drawGhostSpecificElements(gui, model);
        } else if (monsterType == 'l'){ // Default behavior for PurpleMonster and others
            sprites.get(0).draw(gui, (int) model.getPosition().x() - 4, (int) model.getPosition().y() - 1);
        }
    }

    Sprite getSpriteForAnimation(List<Sprite> sprites, long tick) {
        int animationFPS = 6; // Animation updates at 6 FPS
        int animationFrameTime = 30 / animationFPS; // Frames per tick at game FPS = 30
        int frameIndex = (int) ((tick / animationFrameTime) % sprites.size());
        return sprites.get(frameIndex);
    }

    private void drawGhostSpecificElements(GUI gui, Enemies model) {
        gui.drawHitBox((int) model.getPosition().x(), (int) model.getPosition().y(), 4, 4,
                new TextColor.RGB(25, 25, 100));
        //gui.drawPixel((int) model.getPosition().x(), (int) model.getPosition().y(), new TextColor.RGB(200, 105, 150));
    }
}
