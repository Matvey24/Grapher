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

import static view.elements.ElementsList.OFFSET;

public class ParametricSettings extends Settings {
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
        mapSize.setBounds(OFFSET,OFFSET, ComboBoxParameter.WIDTH);
        dimension.addTo(this);
        dimension.setBounds(OFFSET,2 * OFFSET + ComboBoxParameter.HEIGHT, ComboBoxParameter.WIDTH);
        spinner = SupportFrameManager.createSpinner(updater, this, 3 * OFFSET + 2 * ComboBoxParameter.HEIGHT);
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
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40, ComboBoxParameter.HEIGHT * 3 + 4 * OFFSET + 80);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        dimension.setName(Language.DIMENSION);
        spinner.setName(Language.TYPE);
        spinner.setElementNames(Language.TYPE_TITLES);
    }
}
