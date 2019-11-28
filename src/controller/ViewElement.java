package controller;

import java.awt.*;

public abstract class ViewElement {
    protected static Font name_font = new Font("arial", Font.PLAIN, 20);
    public abstract void addTo(Container container);
}
