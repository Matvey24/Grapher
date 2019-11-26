package view.settings;

import controller.GraphType;
import controller.ModelUpdater;
import framesLib.Screen;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Function;

import javax.jws.WebParam;
import javax.swing.*;
import java.awt.event.ItemEvent;

public class FunctionSettings extends Screen {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 300;

    private Parameter mapSize;
    private Function f;
    private TextElement el;
    private JComboBox<String> spinner;
    public FunctionSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter("Frequency", (e)->{
            if(f != null)
                f.setMAP_SIZE(Integer.parseInt(e.getSource().toString()));
        });
        mapSize.addTo(this);
        mapSize.setBounds(0,0, 150);
        spinner = SettingsManager.createSpinner(this,  90, 150);
        spinner.addItemListener((e)->{
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(e.getItem() == GraphType.titles[GraphType.PARAMETER.ordinal()])
                    updater.makeParameter(f, el);
            }
        });
    }
    void setInfo(Function f, TextElement e){
        this.f = f;
        this.el = e;
        spinner.setSelectedIndex(GraphType.FUNCTION.ordinal());
        mapSize.setDefault(f.MAP_SIZE + "");
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
}
