package view.grapher.graphics;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import java.awt.*;

public abstract class Graphic {
    public int MAP_SIZE = 500;
    public String name;
    final int MAX_DELTA = 1000;
    double[] map;

    protected Expression<Double> func;
    Variable<Double> var;
    Color color;
    double offsetX;
    double offsetY;
    double scaleY;
    double scaleX;
    boolean needResize;

    Graphic() {
        map = new double[MAP_SIZE];
        color = Color.BLACK;
    }

    public void update(Expression<Double> func, Variable<Double> var) {
        this.var = var;
        this.func = func;
        needResize = true;
    }

    public abstract void resize(double offsetX, double offsetY, double scaleX, double scaleY);
    public void setColor(Color color) {
        this.color = color;
    }
    public abstract void paint(Graphics g);
    public void funcChanged(){
        needResize = true;
    }
    public void setMAP_SIZE(int map_size){
        MAP_SIZE = map_size;
        needResize = true;
        map = new double[map_size];
    }
}
