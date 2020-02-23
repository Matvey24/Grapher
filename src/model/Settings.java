package model;

import framesLib.Screen;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;

import javax.swing.*;

public abstract class Settings extends Screen {
    public abstract Graphic getGraphic();
    public abstract TextElement getTextElement();
}
