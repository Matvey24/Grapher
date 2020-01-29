package view.grapher.graphics;

import calculator2.calculator.executors.Variable;

import java.awt.*;

import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class Implicit extends Graphic {
    private float[][] data;
    private int yMAP_SIZE;
    private Variable<Double> yVar;
    private boolean spectrum;
    private Color c;
    private double sensitivity;

    public Implicit() {
        setMAP_SIZE(200);
        sensitivity = 1;
    }

    @Override
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        if (needResize || offsetX != this.offsetX || this.scaleX != scaleX || offsetY != this.offsetY || this.scaleY != scaleY) {
            this.offsetY = offsetY;
            this.scaleY = scaleY;
            this.offsetX = offsetX;
            this.scaleX = scaleX;
            needResize = false;
            double deltaX = GRAPH_WIDTH / scaleX / MAP_SIZE;
            double deltaY = HEIGHT / scaleY / yMAP_SIZE;
            for (int i = 0; i < MAP_SIZE; ++i) {
                for (int j = 0; j < yMAP_SIZE; ++j) {
                    var.setValue(offsetX + i * deltaX);
                    yVar.setValue(offsetY - j * deltaY);
                    data[i][j] = func.calculate().floatValue();
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        float dw = (float) GRAPH_WIDTH / MAP_SIZE;
        float dh = (float) HEIGHT / yMAP_SIZE;
        if (spectrum) {
            for (int i = 0; i < MAP_SIZE; ++i) {
                for (int j = 0; j < yMAP_SIZE; ++j) {
                    float val = (float)(1 / (1 + Math.exp(-data[i][j] * sensitivity)));
                    int re = 0, gr = 0, bl = 0;
                    val *= 5;
                    int p = (int)Math.floor(val);
                    val %= 1;
                    int col = (int)(val * 255);
                    if(p == 0){
                        re = 255;
                        gr = col;
                    }else if(p == 1){
                        gr = 255;
                        re = 255 - col;
                    }else if(p == 2){
                        gr = 255;
                        bl = col;
                    }else if(p == 3){
                        bl = 255;
                        gr = 255 - col;
                    }else if(p == 4){
                        bl = 255;
                        re = col;
                    }else{
                        bl = 255;
                        re = 255;
                    }
                    g.setColor(new Color(re, gr, bl, 150));
                    int x = (int) (i * dw);
                    int y = (int) (j * dh);
                    g.fillRect(x, y, (int) (dw * (i + 1)) - x, (int) (dh * (j + 1)) - y);
                }
            }
        } else {
            g.setColor(c);
            for (int i = 0; i < MAP_SIZE; ++i) {
                for (int j = 0; j < yMAP_SIZE; ++j) {
                    if (data[i][j] != 0.0) {
                        int x = (int) (i * dw);
                        int y = (int) (j * dh);
                        g.fillRect(x, y, (int) (dw * (i + 1)) - x, (int) (dh * (j + 1)) - y);
                    }
                }
            }
        }
    }

    public void updateY(Variable<Double> yVar) {
        this.yVar = yVar;
        c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public boolean isSpectrum() {
        return spectrum;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSpectrum(boolean spectrum) {
        this.spectrum = spectrum;
    }

    @Override
    public void setMAP_SIZE(int map_size) {
        needResize = true;
        MAP_SIZE = map_size;
        float dw = (float) GRAPH_WIDTH / MAP_SIZE;
        yMAP_SIZE = (int) (HEIGHT / dw);
        data = new float[MAP_SIZE][yMAP_SIZE];
    }
}
