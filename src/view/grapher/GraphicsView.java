package view.grapher;

import model.Language;
import controller.ModelUpdater;
import view.grapher.graphics.Graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

public class GraphicsView extends JPanel {
    private final ArrayList<Graphic> graphics;
    private final CoordinateSystem coordinateSystem;
    private final ModelUpdater updater;
    private boolean painting;
    public boolean draw_coords;

    public GraphicsView(ModelUpdater updater) {
        this.updater = updater;
        graphics = new ArrayList<>();
        coordinateSystem = new CoordinateSystem();
        updater.setGraphics(graphics);
        draw_coords = true;

        registerKeyboardAction((e) -> {
                    try {
                        updater.setState(Language.UPDATING);
                        Graphics g = new MyGraphics();
                        paint(g);
                        updater.setState(Language.FINE);
                    }catch (RuntimeException ex){
                        updater.setState(ex.getMessage());
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK),
                WHEN_IN_FOCUSED_WINDOW
        );
        updater.setResize(this::resize);
    }

    private void resize() {
        if (painting)
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

    public void myrepaint() {
        if (!painting) {
            painting = true;
            repaint();
        }
    }

    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (draw_coords)
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

class MyGraphics extends Graphics {
    private Font font;

    private final int mouseX;
    private final int mouseY;
    private final Robot r;

    public MyGraphics() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        mouseX = p.x;
        mouseY = p.y;
        try {
            r = new Robot();
        } catch (Exception e) {
            throw new RuntimeException("Error creating robot!");
        }
    }

    @Override
    public Graphics create() {
        return this;
    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color c) {

    }

    @Override
    public void setPaintMode() {

    }

    @Override
    public void setXORMode(Color c1) {
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return null;
    }

    @Override
    public Rectangle getClipBounds() {
        return null;
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {

    }

    @Override
    public void setClip(int x, int y, int width, int height) {

    }

    @Override
    public Shape getClip() {
        return null;
    }

    @Override
    public void setClip(Shape clip) {

    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        try {
            r.mouseMove(mouseX + x1, mouseY + y1);
            Thread.sleep(1);
            r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(1);
            r.mouseMove(mouseX + x2, mouseY + y2);
            Thread.sleep(1);
            r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
        } catch (Exception e) {
            throw new RuntimeException("Error drawing: " + e.getMessage());
        }
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {

    }

    @Override
    public void clearRect(int x, int y, int width, int height) {

    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void drawOval(int x, int y, int width, int height) {

    }

    @Override
    public void fillOval(int x, int y, int width, int height) {

    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void drawString(String str, int x, int y) {

    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {

    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public void dispose() {
        r.mouseMove(mouseX, mouseY);
    }
}
