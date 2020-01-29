package view.settings;

import controller.GraphType;
import controller.Language;
import controller.ModelUpdater;
import controller.SupportFrameManager;
import framesLib.Screen;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Implicit;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ImplicitSettings extends Screen {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 400;

    private final Parameter mapSize;
    private final Parameter sensitivity;
    private Implicit imp;
    private TextElement el;
    private final JComboBox<String> spinner;
    private final JComboBox<String> spectrum;
    public ImplicitSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (e)->{
            if(imp != null){
                imp.setMAP_SIZE(Integer.parseInt(e.getSource().toString()));
                updater.runResize();
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(0,0, 150);
        sensitivity = new Parameter(Language.SENSITIVITY, e->{
            if(imp != null){
                imp.setSensitivity(Double.parseDouble(e.getSource().toString()));
                updater.runResize();
            }
        });
        sensitivity.addTo(this);
        sensitivity.setBounds(0, 70, 150);
        spinner = SupportFrameManager.createSpinner(this, 200, 150);
        spinner.addItemListener((e)->{
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(e.getItem() == GraphType.titles[GraphType.FUNCTION.ordinal()])
                    updater.makeFunction(imp, el);
                else if(e.getItem() == GraphType.titles[GraphType.PARAMETER.ordinal()])
                    updater.makeParameter(imp, el);
            }
        });
        spectrum = new JComboBox<>();
        spectrum.setBounds(10, 150, 150, 40);
        add(spectrum);
        spectrum.addItem(Language.INEQUALITY);
        spectrum.addItem(Language.SPECTRUM);
        spectrum.addItemListener((e)->{
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(e.getItem().equals(Language.INEQUALITY)){
                    imp.setSpectrum(false);
                }else{
                    imp.setSpectrum(true);
                }
                updater.frameResize();
            }
        });
    }
    public void setInfo(Implicit imp, TextElement e){
        this.imp = imp;
        this.el = e;
        sensitivity.setDefault(imp.getSensitivity() + "");
        spectrum.setSelectedIndex((imp.isSpectrum())?1:0);
        spinner.setSelectedIndex(GraphType.IMPLICIT.ordinal());
        mapSize.setDefault(imp.MAP_SIZE + "");
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
}
