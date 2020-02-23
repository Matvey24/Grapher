package view.support_panels;

import model.Language;
import controller.ModelUpdater;
import controller.SupportFrameManager;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Parametric;

public class ParametricSettings extends Settings {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 400;

    private final Parameter mapSize;
    private Parameter dimension;
    private Parametric p;
    private TextElement el;
    private ComboBoxParameter spinner;
    public ParametricSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (e)->{
            if(p != null){
                p.setMAP_SIZE(Integer.parseInt(e.getSource().toString()));
                updater.runResize();
            }
        });
        dimension = new Parameter(Language.DIMENSION, (e)->{
            if(p != null){
                try {
                    String[] dim = e.getSource().toString().split(":");
                    double start = Double.parseDouble(dim[0]);
                    double end = Double.parseDouble(dim[1]);
                    p.updateBoards(start, end);
                    updater.runResize();
                }catch (RuntimeException ex){
                    dimension.setDefault(p.getStartT() + ":" + p.getEndT());
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(0,0, 150);
        dimension.addTo(this);
        dimension.setBounds(0,80,150);
        spinner = SupportFrameManager.createSpinner(updater, this, 200);
    }
    public void setInfo(Parametric p, TextElement e){
        this.p = p;
        this.el = e;
        mapSize.setDefault(p.MAP_SIZE + "");
        dimension.setDefault(p.getStartT() + ":" + p.getEndT());
    }

    @Override
    public Graphic getGraphic() {
        return p;
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
        dimension.setName(Language.DIMENSION);
        spinner.setName(Language.TYPE);
        spinner.setElementNames(Language.TYPE_TITLES);
    }
}
