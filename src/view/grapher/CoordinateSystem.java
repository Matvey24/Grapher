package view.grapher;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class CoordinateSystem {
    private static final int MAX_DELTA = 190;
    private static final int MIN_DELTA = 80;
    private static final Color EXTRA_LINE_COLOR = new Color(0.3f, 0.3f, 0.3f, 0.4f);
    private static final Color MAIN_COLOR = Color.BLACK;
    private double offsetX;
    private double offsetY;
    private double scaleX;
    private double scaleY;
    private double deltaX = 1;
    private double deltaY = 1;
    private int deltaXpow = 0;
    private int deltaYpow = 0;
    public void resize(double offsetX, double offsetY, double scaleX, double scaleY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        if (deltaX * scaleX > MAX_DELTA) {
            if ((deltaXpow - 2) % 3 == 0) {
                deltaX *= 0.4;
            } else {
                deltaX *= 0.5;
            }
            --deltaXpow;
        } else if (deltaX * scaleX < MIN_DELTA) {
            if ((deltaXpow - 1) % 3 == 0) {
                deltaX *= 2.5;
            } else {
                deltaX *= 2;
            }
            ++deltaXpow;
        }
        if (deltaY * scaleY > MAX_DELTA) {
            if ((deltaYpow - 2) % 3 == 0) {
                deltaY *= 0.4;
            } else {
                deltaY *= 0.5;
            }
            --deltaYpow;
        } else if (deltaY * scaleY < MIN_DELTA) {
            if ((deltaYpow - 1) % 3 == 0) {
                deltaY *= 2.5;
            } else {
                deltaY *= 2;
            }
            ++deltaYpow;
        }
        if (deltaX <= 0.5001 && deltaX >= 0.4999) {
            deltaX = 0.5;
        }
        if (deltaY <= 0.5001 && deltaY >= 0.4999) {
            deltaY = 0.5;
        }
    }

    public void draw(Graphics gr) {
        gr.setColor(MAIN_COLOR);
        {
            int x;
            boolean drawLine = false;
            if(offsetX * scaleX > -GRAPH_WIDTH + 60){
                x = 0;
                if(offsetX < 0){
                    drawLine = true;
                    x = (int)(-offsetX * scaleX);
                }
            }else{
                x = GRAPH_WIDTH - 60;
            }
            for (int i = 0, m = (int) (HEIGHT / scaleY / deltaY) + 1; i < m; ++i) {
                int y = (int) ((mod(offsetY, deltaY) + (m - i - 1) * deltaY) * scaleY);
                gr.drawString(dts(ceil(offsetY + (i - m) * deltaY, deltaY)), x + 5, y + 15);
                gr.setColor(EXTRA_LINE_COLOR);
                gr.drawLine(0, y, GRAPH_WIDTH, y);
                gr.setColor(MAIN_COLOR);
            }
            if(drawLine) {
                gr.drawLine(x, 0, x, HEIGHT);
                gr.drawString("y", (int) (-offsetX * scaleX - 10), 15);
            }
        }
        {
            int y;
            boolean drawLine = false;
            if (offsetY * scaleY > 0) {
                y = HEIGHT - 60;
                if (offsetY * scaleY < HEIGHT - 60) {
                    y = (int) (offsetY * scaleY);
                    drawLine = true;
                }
            } else {
                y = 0;
            }
            for (int i = 0, m = (int) (GRAPH_WIDTH / scaleX / deltaX) + 1; i < m; ++i) {
                int x = (int) ((-mod(offsetX, deltaX) + (i + 1) * deltaX) * scaleX);
                gr.drawString(dts(ceil(offsetX + i * deltaX, deltaX)), x + 5, y + 15);
                gr.setColor(EXTRA_LINE_COLOR);
                gr.drawLine(x, 0, x, HEIGHT);
                gr.setColor(MAIN_COLOR);
            }
            if(drawLine) {
                gr.drawLine(0, y, GRAPH_WIDTH, y);
                gr.drawString("x", GRAPH_WIDTH - 25, (int) (offsetY * scaleY - 10));
            }
        }
    }

    private static double ceil(double a, double m) {
        return m * Math.ceil(a / m);
    }

    private String dts(double d) {
        if(d % 1 == 0){
            return String.valueOf((long)d);
        }
        String val = BigDecimal.valueOf(d)
                .toPlainString();
        return BigDecimal.valueOf(Double.parseDouble(val.substring(0, Math.min(val.length(), 16))))
                .stripTrailingZeros()
                .toPlainString();

    }

    private static double mod(double a, double b) {
        double c = a % b;
        if (c <= 0)
            c += b;
        return c;
    }
}
