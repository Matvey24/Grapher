package view.support_panels;

import model.Language;
import controller.ModelUpdater;
import controller.SupportFrameManager;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Item;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Implicit;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ImplicitSettings extends Settings {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 400;

    private final Parameter mapSize;
    private final Parameter sensitivity;
    private Implicit imp;
    private TextElement el;
    private final JComboBox<Item> spectrum;
    private ComboBoxParameter spinner;
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
        spectrum = new JComboBox<>();
        spectrum.setBounds(10, 150, 150, 40);
        add(spectrum);
        spectrum.addItem(new Item(Language.INEQUALITY));
        spectrum.addItem(new Item(Language.SPECTRUM));
        spectrum.addItemListener((e)->{
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(((Item)e.getItem()).equal(Language.INEQUALITY)){
                    imp.setSpectrum(false);
                }else{
                    imp.setSpectrum(true);
                }
                updater.frameResize();
            }
        });
        spinner = SupportFrameManager.createSpinner(updater, this, 200);
    }
    public void setInfo(Implicit imp, TextElement e){
        this.imp = imp;
        this.el = e;
        sensitivity.setDefault(imp.getSensitivity() + "");
        spectrum.setSelectedIndex((imp.isSpectrum())?1:0);
        mapSize.setDefault(imp.MAP_SIZE + "");
    }

    @Override
    public Graphic getGraphic() {
        return imp;
    }

    @Override
    public TextElement getTextElement() {
        return el;
    }

    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        sensitivity.setName(Language.SENSITIVITY);

        spectrum.getItemAt(0).name = Language.INEQUALITY;
        spectrum.getItemAt(1).name = Language.SPECTRUM;
        spectrum.updateUI();

        spinner.setName(Language.TYPE);
        spinner.setElementNames(Language.TYPE_TITLES);
    }
}
