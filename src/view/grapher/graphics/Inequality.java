package view.grapher.graphics;

import java.awt.*;

public class Inequality extends Graphic{
    private boolean[][] data;

    public Inequality() {
        data = new boolean[MAP_SIZE][MAP_SIZE];
    }

    @Override
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        if(needResize || offsetX != this.offsetX || this.scaleX != scaleX || offsetY != this.offsetY || this.scaleY != scaleY) {
            this.offsetY = offsetY;
            this.scaleY = scaleY;
            this.offsetX = offsetX;
            this.scaleX = scaleX;
        }

    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void setMAP_SIZE(int map_size) {
        needResize = true;
        MAP_SIZE = map_size;
        data = new boolean[map_size][map_size];
    }
}
