package view.grapher.graphics;

import java.awt.*;

import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class FunctionX extends Graphic {
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
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
    public void paint(Graphics g) {
        g.setColor(color);
        for (int i = 0; i < MAP_SIZE - 1; ++i) {
            if(Double.isNaN(map[i]) || Double.isNaN(map[i + 1]) || Math.abs(map[i] - map[i + 1]) * scaleY > MAX_DELTA)
                continue;
            int y1 = (int) ((offsetY - map[i]) * scaleY);
            int y2 = (int) ((offsetY - map[i + 1]) * scaleY);
            if(y1 < 0 && y2 < 0 || y1 > HEIGHT && y2 > HEIGHT)
                continue;
            g.drawLine(i * GRAPH_WIDTH / MAP_SIZE, y1,
                    (i + 1) * GRAPH_WIDTH / MAP_SIZE, y2);
        }
    }
}
