package view.grapher.graphics;


import calculator2.calculator.executors.BinaryActor;
import calculator2.calculator.executors.Variable;
import model.GraphType;
import view.MainPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class Implicit extends Graphic {
    private static final Color VOID = new Color(0, 0, 0, 0);
    private float[][] data;
    private BufferedImage data1;
    private final MainPanel panel;
    private int yMAP_SIZE;
    private Variable<Double> yVar;
    private Color c;

    private double sensitivity = 1;
    private int type;
    private static final int SPECTRUM = 0;
    private static final int INEQUALITY = 1;
    private static final int EQUALITY = 2;

    public int viewType;
    public static final int RAY_SPECTRUM = 0;
    public static final int INFRARED_IMAGER = 1;

    public Implicit(MainPanel panel) {
        this.panel = panel;
        setMAP_SIZE(500);
        super.type = GraphType.IMPLICIT;
        viewType = INFRARED_IMAGER;
    }

    public Implicit(MainPanel panel, int map_size, boolean feelsTime) {
        this.panel = panel;
        setMAP_SIZE(map_size);
        this.feelsTime = feelsTime;
        super.type = GraphType.IMPLICIT;
        viewType = INFRARED_IMAGER;
    }

    @Override
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        if (needResize || offsetX != this.offsetX || this.scaleX != scaleX
                || offsetY != this.offsetY || this.scaleY != scaleY) {
            this.offsetY = offsetY;
            this.scaleY = scaleY;
            this.offsetX = offsetX;
            this.scaleX = scaleX;
            needResize = false;
            double deltaX = GRAPH_WIDTH / scaleX / MAP_SIZE;
            double deltaY = HEIGHT / scaleY / yMAP_SIZE;
            if (type == SPECTRUM) {
                if (viewType == INFRARED_IMAGER)
                    for (int i = 0; i < MAP_SIZE; ++i) {
                        for (int j = 0; j < yMAP_SIZE; ++j) {
                            var.setValue(offsetX + i * deltaX);
                            yVar.setValue(offsetY - j * deltaY);
                            data1.setRGB(i, j, 0xb6ffffff&Color.HSBtoRGB((float)
                                    ((2f / 3) / (1 + Math.exp(func.calculate() * sensitivity))), 1, 1));
                        }
                    }
                else if(viewType == RAY_SPECTRUM)
                    for (int i = 0; i < MAP_SIZE; ++i) {
                        for (int j = 0; j < yMAP_SIZE; ++j) {
                            var.setValue(offsetX + i * deltaX);
                            yVar.setValue(offsetY - j * deltaY);

                            data1.setRGB(i, j, 0xb6ffffff&Color.HSBtoRGB(5f/6*(float)
                                    (1 / (1 + Math.exp(-func.calculate() * sensitivity))), 1, 1));
                        }
                    }
            } else if (type == INEQUALITY) {
                for (int i = 0; i < MAP_SIZE; ++i) {
                    for (int j = 0; j < yMAP_SIZE; ++j) {
                        var.setValue(offsetX + i * deltaX);
                        yVar.setValue(offsetY - j * deltaY);
                        data1.setRGB(i, j, ((func.calculate() != 0) ? c : VOID).getRGB());
                    }
                }
            } else {
                boolean nsign;
                for (int i = 0; i < MAP_SIZE; ++i) {
                    for (int j = 0; j < yMAP_SIZE; ++j) {
                        var.setValue(offsetX + i * deltaX);
                        yVar.setValue(offsetY - j * deltaY);
                        data[i][j] = func.calculate().floatValue();
                        data1.setRGB(i, j, VOID.getRGB());
                    }
                }
                for (int i = 0; i < MAP_SIZE; ++i) {
                    for (int j = 0; j < yMAP_SIZE; ++j) {
                        nsign = data[i][j] > 0;
                        if ((i < MAP_SIZE - 1 && (data[i + 1][j] < 0) == nsign) && Math.abs(data[i + 1][j] - data[i][j]) < sensitivity) {
                            data1.setRGB(i, j, color.getRGB());
                            data1.setRGB(i + 1, j, color.getRGB());
                        }
                        if ((j < yMAP_SIZE - 1 && (data[i][j + 1] < 0 == nsign)) && Math.abs(data[i][j + 1] - data[i][j]) < sensitivity) {
                            data1.setRGB(i, j + 1, color.getRGB());
                            data1.setRGB(i, j, color.getRGB());
                        }
                        if ((i > 0 && (data[i - 1][j] < 0) == nsign) && Math.abs(data[i - 1][j] - data[i][j]) < sensitivity) {
                            data1.setRGB(i - 1, j, color.getRGB());
                            data1.setRGB(i, j, color.getRGB());
                        }
                        if ((j > 0 && (data[i][j - 1] < 0 == nsign)) && Math.abs(data[i][j - 1] - data[i][j]) < sensitivity) {
                            data1.setRGB(i, j - 1, color.getRGB());
                            data1.setRGB(i, j, color.getRGB());
                        }
                        if (data[i][j] == 0)
                            data1.setRGB(i, j, color.getRGB());
                    }
                }
            }
        }
    }
    public BufferedImage getData1(){
        for(int i = 0; i < data1.getWidth(); ++i){
            for(int j = 0; j < data1.getHeight(); ++j){
                if(data1.getRGB(i, j) >> 24 != 0)
                    data1.setRGB(i, j, data1.getRGB(i, j) | 0xff000000);
            }
        }
        return data1;
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(data1, 0, 0, GRAPH_WIDTH, HEIGHT, panel);
    }

    public void updateY(Variable<Double> yVar) {
        this.yVar = yVar;
        if (func.getName().equals("=")) {
            type = EQUALITY;
            BinaryActor<Double> actor = (BinaryActor<Double>) func;
            actor.setFunc((a, b) -> a.calculate() - b.calculate());
        } else if (func.getName().equals("<") || func.getName().equals(">") || func.getName().equals("0.0")) {
            type = INEQUALITY;
            c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 130);
        } else {
            type = SPECTRUM;
        }
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
        needResize = true;
    }

    public void setViewType(int type) {
        if (viewType == type)
            return;
        viewType = type;
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
        data1 = new BufferedImage(MAP_SIZE, yMAP_SIZE, BufferedImage.TYPE_INT_ARGB);
    }
}
