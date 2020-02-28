package view.grapher.graphics;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;

import java.awt.*;
import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class Parametric extends Graphic {
    private double startT;
    private double endT;
    private double[] xMap;
    private Expression<Double> xFunc;
    private Variable<Double> xVar;

    public Parametric() {
        xMap = new double[MAP_SIZE];
        startT = 0;
        endT = 6.2832;
    }
    public Parametric(int map_size){
        setMAP_SIZE(map_size);
        startT = 0;
        endT = 6.2832;
    }
    public void updateX(Expression<Double> xFunc, Variable<Double> xVar) {
        this.xFunc = xFunc;
        this.xVar = xVar;
    }
    @Override
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        this.offsetY = offsetY;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.scaleX = scaleX;
        for (int i = 0; i < MAP_SIZE; ++i) {
            double t = startT + (double) i * (endT - startT) / MAP_SIZE;
            var.setValue(t);
            xVar.setValue(t);
            map[i] = func.calculate();
            xMap[i] = xFunc.calculate();
        }

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        for (int i = 0; i < MAP_SIZE - 1; ++i) {
            if (Double.isNaN(map[i] + map[i + 1] + xMap[i] + xMap[i + 1])
                    || ((map[i] - map[i + 1]) * (map[i] - map[i + 1]) * scaleY * scaleY + (xMap[i] - xMap[i + 1]) * (xMap[i] - xMap[i + 1]) * scaleX * scaleX)
                    > MAX_DELTA * MAX_DELTA)
                continue;
            int y1 = (int) ((offsetY - map[i]) * scaleY);
            int y2 = (int) ((offsetY - map[i + 1]) * scaleY);
            if (y1 < 0 && y2 < 0 || y1 > HEIGHT && y2 > HEIGHT)
                continue;
            int x1 = (int) ((-offsetX + xMap[i]) * scaleX);
            int x2 = (int) ((-offsetX + xMap[i + 1]) * scaleX);
            if (x1 < 0 || x2 < 0 || x1 > GRAPH_WIDTH && x2 > GRAPH_WIDTH)
                continue;
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public void updateBoards(double startT, double endT) {
        this.startT = startT;
        this.endT = endT;
        needResize = true;
    }

    public double getStartT() {
        return startT;
    }

    public double getEndT() {
        return endT;
    }

    @Override
    public void setMAP_SIZE(int map_size) {
        super.setMAP_SIZE(map_size);
        xMap = new double[map_size];
    }
}
