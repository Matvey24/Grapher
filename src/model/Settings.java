package model;

import controller.ModelUpdater;
import framesLib.Screen;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;

import javax.swing.*;

import static view.elements.ElementsList.OFFSET;

public abstract class Settings extends Screen {
    private Parameter mapSize;
    protected JToggleButton feels_time;
    protected TextElement el;
    protected static final int HEIGHT = 3 * OFFSET
            + ComboBoxParameter.HEIGHT + TextElement.HEIGHT;
    protected static final int WIDTH = ComboBoxParameter.WIDTH + 2 * OFFSET + 40;
    public Settings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (s) -> {
            if (getGraphic() != null) {
                int n = Integer.parseInt(s);
                if (n < 2) {
                    mapSize.setDefault(String.valueOf(getGraphic().MAP_SIZE));
                } else {
                    getGraphic().setMAP_SIZE(Integer.parseInt(s));
                    updater.runResize();
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, ComboBoxParameter.WIDTH);
        feels_time = new JToggleButton(Language.FEELS_TIME);
        feels_time.setFocusPainted(false);
        add(feels_time);
        feels_time.setBounds(OFFSET, 2*OFFSET + ComboBoxParameter.HEIGHT,
                ComboBoxParameter.WIDTH, TextElement.HEIGHT);
        feels_time.addActionListener((e)->{
            if(getGraphic() != null)
                getGraphic().feelsTime = feels_time.isSelected();
        });
    }
    public abstract Graphic getGraphic();
    public void setInfo(Graphic g, TextElement el){
        this.el = el;
        mapSize.setDefault(String.valueOf(g.MAP_SIZE));
        feels_time.setSelected(g.feelsTime);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        feels_time.setText(Language.FEELS_TIME);
    }
}
