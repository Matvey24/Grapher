package view.grapher;

import model.Language;
import controller.ModelUpdater;
import view.elements.ElementsList;
import view.grapher.graphics.Graphic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphicsView extends JPanel {
    private final ArrayList<Graphic> graphics;
    private final CoordinateSystem coordinateSystem;
    private final ModelUpdater updater;
    private boolean painting;
    public boolean draw_coords;
    public GraphicsView(ElementsList list, ModelUpdater updater) {
        this.updater = updater;
        graphics = new ArrayList<>();
        coordinateSystem = new CoordinateSystem();
        updater.setList(list);
        updater.setGraphics(graphics);
        updater.setResize(this::resize);
        draw_coords = true;
    }

    private void resize() {
        if(painting)
            return;
        updater.setState(Language.UPDATING);
        double scaleX = updater.getScaleX();
        double scaleY = updater.getScaleY();
        double offsetX = updater.getOffsetX();
        double offsetY = updater.getOffsetY();
        coordinateSystem.resize(offsetX, offsetY, scaleX, scaleY);
        for (Graphic g : graphics)
            g.resize(offsetX, offsetY, scaleX, scaleY);
        updater.setState(Language.FINE);
    }
    public void myrepaint(){
        if(!painting) {
            painting = true;
            repaint();
        }
    }
    public CoordinateSystem getCoordinateSystem(){
        return coordinateSystem;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(draw_coords)
            coordinateSystem.draw(g);
        try {
            for (Graphic s : graphics)
                s.paint(g);
        } catch (NullPointerException e) {
            updater.error(e.toString());
        }
        painting = false;
    }
}
