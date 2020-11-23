package view.grapher.graphics;

import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.executors.FuncVariable;
import model.GraphType;

import java.awt.*;

public abstract class Graphic {
    public GraphType type;
    public int MAP_SIZE = 500;
    public String name;
    final int MAX_DELTA = 1000;
    double[] map;
    protected Expression<Double> func;
    FuncVariable<Double> var;
    public Color color;
    double offsetX;
    double offsetY;
    double scaleY;
    double scaleX;
    double graph_width;
    double graph_height;
    boolean needResize;
    public boolean feelsTime;
    public boolean colorChanged;
    Graphic() {
        map = new double[MAP_SIZE];
        color = Color.BLACK;
        feelsTime = true;
    }
    Graphic(int MAP_SIZE, boolean feelsTime){
        this.map = new double[MAP_SIZE];
        this.MAP_SIZE = MAP_SIZE;
        this.feelsTime = feelsTime;
        color = Color.BLACK;
    }
    public void update(Expression<Double> func, FuncVariable<Double> var) {
        this.var = var;
        this.func = func;
        needResize = true;
    }

    public abstract void resize(double offsetX, double offsetY, double scaleX, double scaleY);

    public void setColor(Graphic g) {
        this.color = g.color;
        this.colorChanged = g.colorChanged;
    }
    public void changeColor(Color color){this.color = color;this.colorChanged = true;}

    public abstract void paint(Graphics g);

    public void timeChanged() {
        if(feelsTime)
            needResize = true;
    }
    public void update_graphic(){
        needResize = true;
    }
    public void free(){
        map = null;
        func = null;
        var = null;
    }
    public void setMAP_SIZE(int map_size) {
        MAP_SIZE = map_size;
        needResize = true;
        map = new double[map_size];
    }
}
