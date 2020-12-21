package view.support_panels.graphics;

import controller.ModelUpdater;
import framesLib.Screen;
import model.Language;
import view.elements.ComboBoxParameter;
import view.elements.ElementsList;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.support_panels.ColorChooser;


import javax.swing.*;

import java.awt.event.ActionListener;

import static view.elements.ElementsList.OFFSET;

public abstract class Settings extends Screen {
    protected final int WIDTH = ElementsList.WIDTH;
    private Parameter mapSize;
    protected final JToggleButton feels_time;
    protected final JButton open_cc;
    protected TextElement el;
    protected static final int HEIGHT = 3 * OFFSET
            + ComboBoxParameter.HEIGHT + TextElement.HEIGHT;
    private final ActionListener changeColor;

    public Settings(ModelUpdater updater) {
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (s) -> {
            if (getGraphic() != null) {
                int n = Integer.parseInt(s);
                if (!Graphic.checkValidDiscretization(n)) {
                    mapSize.setDefault(String.valueOf(getGraphic().MAP_SIZE));
                } else {
                    getGraphic().setMAP_SIZE(Integer.parseInt(s));
                    updater.runResize();
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, TextElement.WIDTH);
        feels_time = new JToggleButton(Language.FEELS_TIME);
        feels_time.setFocusPainted(false);
        add(feels_time);
        feels_time.setBounds(OFFSET, 2 * OFFSET + ComboBoxParameter.HEIGHT,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        feels_time.addActionListener((e) -> {
            if (getGraphic() != null)
                getGraphic().feelsTime = feels_time.isSelected();
        });
        changeColor = e ->
                updater.setColor(getGraphic(), el, updater.getSupportFrameManager().getColorc().getColor());
        open_cc = new JButton(Language.COLOR_CHOOSER);
        open_cc.setFocusPainted(false);
        add(open_cc);
        open_cc.setBounds(OFFSET + TextElement.WIDTH / 2 + OFFSET / 2, 2 * OFFSET + ComboBoxParameter.HEIGHT,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        open_cc.addActionListener((e) -> {
            if (getGraphic() != null) {
                ColorChooser cc = updater.getSupportFrameManager().getColorc();
                cc.set(getGraphic().color, changeColor);
                changeScreen(cc);
            }
        });

    }

    public abstract Graphic getGraphic();

    public void setInfo(Graphic g, TextElement el) {
        this.el = el;
        mapSize.setDefault(String.valueOf(g.MAP_SIZE));
        feels_time.setSelected(g.feelsTime);
    }

    public void updateLanguage() {
        mapSize.setName(Language.DISCRETIZATION);
        feels_time.setText(Language.FEELS_TIME);
        open_cc.setText(Language.COLOR_CHOOSER);
    }
}
