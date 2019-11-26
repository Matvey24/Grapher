package view.settings;

import controller.GraphType;
import controller.ModelUpdater;
import framesLib.Screen;
import view.elements.Parameter;
import view.elements.TextElement;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ParameterSettings extends Screen {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 400;

    private Parameter mapSize;
    private Parameter dimension;
    private view.grapher.graphics.Parameter p;
    private TextElement el;
    private JComboBox<String> spinner;
    public ParameterSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter("Frequency", (e)->{
            if(p != null)
                p.setMAP_SIZE(Integer.parseInt(e.getSource().toString()));
        });
        dimension = new Parameter("Dimension", (e)->{
            if(p != null){
                try {
                    String[] dim = e.getSource().toString().split(":");
                    double start = Double.parseDouble(dim[0]);
                    double end = Double.parseDouble(dim[1]);
                    p.updateBoards(start, end);
                }catch (RuntimeException ex){
                    dimension.setDefault(p.getStartT() + ":" + p.getEndT());
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(0,0, 150);
        dimension.addTo(this);
        dimension.setBounds(0,80,150);
        spinner = SettingsManager.createSpinner(this, 200, 150);
        spinner.addItemListener((e)->{
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(e.getItem() == GraphType.titles[GraphType.FUNCTION.ordinal()])
                    updater.makeFunction(p, el);
            }
        });
    }
    void setInfo(view.grapher.graphics.Parameter p, TextElement e){
        this.p = p;
        this.el = e;
        spinner.setSelectedIndex(GraphType.PARAMETER.ordinal());
        mapSize.setDefault(p.MAP_SIZE + "");
        dimension.setDefault(p.getStartT() + ":" + p.getEndT());
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
}
