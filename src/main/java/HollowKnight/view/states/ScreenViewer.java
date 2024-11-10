package HollowKnight.view.states;

import HollowKnight.gui.GUI;
import HollowKnight.model.Model;

public abstract class ScreenViewer<T> {
    final protected GUI gui;
    final protected T model;

    public ScreenViewer(GUI gui, T model) {
        this.gui = gui;
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public abstract void draw();
}