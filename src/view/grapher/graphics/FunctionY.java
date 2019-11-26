package view.grapher.graphics;

import java.awt.*;

import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class FunctionY extends Graphic {
    @Override
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        this.offsetX = offsetX;
        this.scaleX = scaleX;
        if(needResize || offsetY != this.offsetY || this.scaleY != scaleY) {
            needResize = false;
            this.offsetY = offsetY;
            this.scaleY = scaleY;
            for (int i = 0; i < MAP_SIZE; ++i) {
                var.setValue(offsetY - (double) i * HEIGHT / MAP_SIZE / scaleY);
                map[i] = func.calculate();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        for (int i = 0; i < MAP_SIZE - 1; ++i) {
            if(Double.isNaN(map[i]) || Double.isNaN(map[i + 1]) || Math.abs(map[i] - map[i + 1]) * scaleX > MAX_DELTA)
                continue;
            int x1 = (int) ((-offsetX + map[i]) * scaleX);
            int x2 = (int) ((-offsetX + map[i + 1]) * scaleX);
            if(x1 < 0 || x2 < 0 || x1 > GRAPH_WIDTH && x2 > GRAPH_WIDTH)
                continue;
            g.drawLine(x1,i * HEIGHT / MAP_SIZE,
                    x2,(i + 1) * HEIGHT / MAP_SIZE);
        }
    }
}
