package HollowKnight.view.elements.trees;

import HollowKnight.gui.GUI;
import HollowKnight.model.game.elements.Tree.MediumTree;
import HollowKnight.view.elements.ElementViewer;
import HollowKnight.view.sprites.Sprite;

import java.io.IOException;

public class MediumTreeViewer implements ElementViewer<MediumTree> {
    private final Sprite mediumTreeSprite;
    public MediumTreeViewer() throws IOException {
        this.mediumTreeSprite = new Sprite("sprites/Ambient/MediumTree.png");
    }

    @Override
    public void draw(MediumTree model, GUI gui) throws IOException {
        mediumTreeSprite.draw(gui, model.getPosition().x(), model.getPosition().y());
    }
}
