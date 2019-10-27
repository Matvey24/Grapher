package view.grapher;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;

import java.awt.*;

import static java.awt.image.ImageObserver.ERROR;
import static view.MainPanel.GRAPH_WIDTH;

public class Graphic {
    private static final int MAP_SIZE = 500;
    private static final int MAX_DELTA_Y = 100;
    private double[] map;
    private Expression<Double> func;
    private Variable<Double> var;
    private Color color;
    private double offsetX;
    private double offsetY;
    private double scaleY;
    private double scaleX;
    private boolean needResize;

    public Graphic() {
        map = new double[MAP_SIZE];
        color = Color.BLACK;
    }

    public void update(Expression<Double> func, Variable<Double> var) {
        this.var = var;
        this.func = func;
        needResize = true;
    }

    void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        this.offsetY = offsetY;
        this.scaleY = scaleY;
        if(needResize || offsetX != this.offsetX || this.scaleX != scaleX) {
            needResize = false;
            this.offsetX = offsetX;
            this.scaleX = scaleX;
            for (int i = 0; i < MAP_SIZE; ++i) {
                var.setValue(offsetX + (double) i * GRAPH_WIDTH / MAP_SIZE / scaleX);
                map[i] = func.calculate();
            }
        }
    }
    public void setColor(Color color) {
        this.color = color;
    }
    void paint(Graphics g) {
        g.setColor(color);
        for (int i = 0; i < MAP_SIZE - 1; ++i) {
            if(Double.isNaN(map[i]) || Double.isNaN(map[i + 1]) || Math.abs(map[i] - map[i + 1]) > MAX_DELTA_Y)
                continue;
            g.drawLine(i * GRAPH_WIDTH / MAP_SIZE, (int) ((offsetY - map[i]) * scaleY),
                    (i + 1) * GRAPH_WIDTH / MAP_SIZE, (int) ((offsetY - map[i + 1]) * scaleY));
        }
    }

}
