package view.support_panels;

import model.Language;
import controller.ModelUpdater;
import controller.SupportFrameManager;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Graphic;

public class FunctionSettings extends Settings {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 300;

    private final Parameter mapSize;
    private Function f;
    private TextElement el;
    private ComboBoxParameter spinner;
    public FunctionSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (e)->{
            if(f != null) {
                f.setMAP_SIZE(Integer.parseInt(e.getSource().toString()));
                updater.runResize();
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(0,0, 150);
        spinner = SupportFrameManager.createSpinner(updater, this,  90);
    }

    @Override
    public Graphic getGraphic() {
        return f;
    }

    @Override
    public TextElement getTextElement() {
        return el;
    }

    public void setInfo(Function f, TextElement e){
        this.f = f;
        this.el = e;
        mapSize.setDefault(String.valueOf(f.MAP_SIZE));
    }

    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        spinner.setName(Language.TYPE);
        spinner.setElementNames(Language.TYPE_TITLES);
    }
}
