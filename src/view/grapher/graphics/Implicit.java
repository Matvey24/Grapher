package view.grapher.graphics;


import calculator2.calculator.executors.BinaryActor;
import calculator2.calculator.executors.Variable;

import java.awt.*;

import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class Implicit extends Graphic {
    private float[][] data;
    private boolean[][] booleans;
    private int yMAP_SIZE;
    private Variable<Double> yVar;
    private Color c;
    private double sensitivity = 1;
    private int type;
    private static final int SPECTRUM = 0;
    private static final int INEQUALITY = 1;
    private static final int EQUALITY = 2;
    public Implicit() {
        setMAP_SIZE(500);
    }
    public Implicit(int map_size){
        setMAP_SIZE(map_size);
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
            if(type == EQUALITY){
                boolean nsign;
                for (int i = 0; i < MAP_SIZE; ++i) {
                    for (int j = 0; j < yMAP_SIZE; ++j) {
                        booleans[i][j] = false;
                    }
                }
                for (int i = 0; i < MAP_SIZE; ++i) {
                    for (int j = 0; j < yMAP_SIZE; ++j) {
                        nsign = data[i][j] > 0;
                        if((i < MAP_SIZE - 1 && (data[i + 1][j] < 0) == nsign) && Math.abs(data[i + 1][j] - data[i][j]) < sensitivity){
                            booleans[i + 1][j] = true;
                            booleans[i][j] = true;
                        }
                        if((j < yMAP_SIZE - 1 && (data[i][j + 1] < 0 == nsign)) && Math.abs(data[i][j + 1] - data[i][j]) < sensitivity){
                            booleans[i][j + 1] = true;
                            booleans[i][j] = true;
                        }
                        if((i > 0 && (data[i - 1][j] < 0) == nsign) && Math.abs(data[i - 1][j] - data[i][j]) < sensitivity){
                            booleans[i - 1][j] = true;
                            booleans[i][j] = true;
                        }
                        if((j > 0 && (data[i][j - 1] < 0 == nsign)) && Math.abs(data[i][j - 1] - data[i][j]) < sensitivity){
                            booleans[i][j - 1] = true;
                            booleans[i][j] = true;
                        }
                        if(data[i][j] == 0)
                            booleans[i][j] = true;
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        float dw = (float) GRAPH_WIDTH / MAP_SIZE;
        float dh = (float) HEIGHT / yMAP_SIZE;
        if (type == SPECTRUM) {
            for (int i = 0; i < MAP_SIZE; ++i) {
                for (int j = 0; j < yMAP_SIZE; ++j) {
                    float val = (float) (1 / (1 + Math.exp(-data[i][j] * sensitivity)));
                    int re = 0, gr = 0, bl = 0;
                    val *= 5;
                    int p = (int) Math.floor(val);
                    val %= 1;
                    int col = (int) (val * 255);
                    if (p == 0) {
                        re = 255;
                        gr = col;
                    } else if (p == 1) {
                        gr = 255;
                        re = 255 - col;
                    } else if (p == 2) {
                        gr = 255;
                        bl = col;
                    } else if (p == 3) {
                        bl = 255;
                        gr = 255 - col;
                    } else if (p == 4) {
                        bl = 255;
                        re = col;
                    } else {
                        bl = 255;
                        re = 255;
                    }
                    g.setColor(new Color(re, gr, bl, 150));
                    int x = (int) (i * dw);
                    int y = (int) (j * dh);
                    g.fillRect(x, y, (int) (dw * (i + 1)) - x, (int) (dh * (j + 1)) - y);
                }
            }
        } else if(type == INEQUALITY) {
            g.setColor(c);
            for (int i = 0; i < yMAP_SIZE; ++i) {
                for (int j = 0; j < MAP_SIZE; ++j) {
                    if ((data[j][i] != 0)) {
                        int k = j + 1;
                        for(; k < MAP_SIZE; ++k)
                            if((data[k][i] == 0))
                                break;
                        int x = (int) (j * dw);
                        int y = (int) (i * dh);
                        g.fillRect(x, y, (int) (dw * k) - x, (int) (dh * (i + 1)) - y);
                        j = k;
                    }
                }
            }
        }else{
            g.setColor(color);
            for (int i = 0; i < yMAP_SIZE; ++i) {
                for (int j = 0; j < MAP_SIZE; ++j) {
                    if (booleans[j][i]) {
                        int k = j + 1;
                        for(; k < MAP_SIZE; ++k)
                            if(!booleans[k][i])
                                break;
                        int x = (int) (j * dw);
                        int y = (int) (i * dh);
                        g.fillRect(x, y, (int) (dw * k) - x, (int) (dh * (i + 1)) - y);
                        j = k;
                    }
                }
            }
        }
    }
    public void updateY(Variable<Double> yVar) {
        this.yVar = yVar;
        c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 130);
        if(func.getName().equals("=")){
            type = EQUALITY;
            BinaryActor<Double> actor = (BinaryActor<Double>)func;
            actor.setFunc((a, b)->a.calculate() - b.calculate());
            if(booleans == null)
                booleans = new boolean[MAP_SIZE][yMAP_SIZE];
        }else if(func.getName().equals("<") || func.getName().equals(">") || func.getName().equals("0.0")){
            type = INEQUALITY;
        }else {
            type = SPECTRUM;
        }
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
        needResize = true;
    }
    public double getSensitivity() {
        return sensitivity;
    }

    @Override
    public void setMAP_SIZE(int map_size) {
        needResize = true;
        MAP_SIZE = map_size;
        float dw = (float) GRAPH_WIDTH / MAP_SIZE;
        yMAP_SIZE = (int) (HEIGHT / dw);
        data = new float[MAP_SIZE][yMAP_SIZE];
        if(booleans != null)
            booleans = new boolean[MAP_SIZE][yMAP_SIZE];
    }
}
