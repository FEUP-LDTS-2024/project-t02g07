package HollowKnight.view.elements.knight.knightStates;

import HollowKnight.model.game.elements.Knight.IdleState;
import HollowKnight.view.elements.PairList;
import HollowKnight.view.sprites.Sprite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IdleAnimation extends StateAnimation{
    public IdleAnimation(Class state, int frames) {
        super(state, frames);
    }

    @Override
    protected void loadAnimation(String path) throws IOException {
        List<Sprite> idleSpriteRight = new ArrayList<>();
        List<Sprite> idleSpriteLeft = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            idleSpriteRight.add(new Sprite("sprites/Knight/Idle/pixil-frame-" + i + ".png"));
            idleSpriteLeft.add(new Sprite("sprites/Knight/Idle/pixil-frame-" + i + "-reversed.png"));
        }
        this.setState(IdleState.class);
        this.setFrames(8);
        this.setAnimation(new PairList<>(idleSpriteRight, idleSpriteLeft));
    }
}