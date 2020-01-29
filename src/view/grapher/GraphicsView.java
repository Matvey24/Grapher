package view.grapher;

import controller.Language;
import controller.ModelUpdater;
import view.elements.ElementsList;
import view.grapher.graphics.Graphic;

import java.awt.*;
import java.util.ArrayList;

public class GraphicsView {
    private final ArrayList<Graphic> graphics;
    private final CoordinateSystem coordinateSystem;

    private final ModelUpdater updater;

    public GraphicsView(ElementsList list, ModelUpdater updater) {
        this.updater = updater;
        graphics = new ArrayList<>();
        coordinateSystem = new CoordinateSystem();
        updater.setList(list);
        updater.setGraphics(graphics);
        updater.setResize(this::resize);
    }
    private void resize() {
        updater.setState(Language.UPDATING);
        double offsetX = updater.getOffsetX();
        double offsetY = updater.getOffsetY();
        double scaleX = updater.getScaleX();
        double scaleY = updater.getScaleY();
        coordinateSystem.resize(offsetX, offsetY, scaleX, scaleY);
        for (Graphic g : graphics)
            g.resize(offsetX, offsetY, scaleX, scaleY);
        updater.setState(Language.FINE);
    }
    public void paint(Graphics g) {
        coordinateSystem.draw(g);
        try {
            for (Graphic s : graphics)
                s.paint(g);
        } catch (NullPointerException e) {
            updater.error(e.toString());
        }
    }
}
