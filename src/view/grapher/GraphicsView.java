package view.grapher;

import controller.ModelUpdater;
import view.elements.ElementsList;
import view.grapher.graphics.Graphic;

import java.awt.*;
import java.util.ArrayList;

public class GraphicsView {
    private ArrayList<Graphic> graphics;
    private CoordinateSystem coordinateSystem;

    private ModelUpdater updater;

    public GraphicsView(ElementsList list, ModelUpdater updater) {
        this.updater = updater;
        graphics = new ArrayList<>();
        coordinateSystem = new CoordinateSystem();
        updater.setList(list);
        updater.setGraphics(graphics);
        updater.setResize(this::resize);
    }

    private void resize() {
        double offsetX = updater.getOffsetX();
        double offsetY = updater.getOffsetY();
        double scaleX = updater.getScaleX();
        double scaleY = updater.getScaleY();
        coordinateSystem.resize(offsetX, offsetY, scaleX, scaleY);
        for (Graphic g : graphics)
            g.resize(offsetX, offsetY, scaleX, scaleY);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.translate(ElementsList.WIDTH, 0);
        coordinateSystem.draw(g);
        try {
            for (Graphic s : graphics)
                s.paint(g);
        } catch (NullPointerException e) {
            updater.error(e.toString());
        }
    }
}
