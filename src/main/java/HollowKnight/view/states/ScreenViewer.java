package HollowKnight.view.states;

import HollowKnight.gui.GUI;

import java.io.IOException;

public abstract class ScreenViewer<T> {
    private final T model;

    public ScreenViewer(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public abstract void draw(GUI gui, long time) throws IOException;
}
