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

import static view.elements.ElementsList.OFFSET;

public class FunctionSettings extends Settings {
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
        mapSize.setBounds(OFFSET,OFFSET, ComboBoxParameter.WIDTH);
        spinner = SupportFrameManager.createSpinner(updater, this,  OFFSET * 2 + ComboBoxParameter.HEIGHT);
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
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40, ComboBoxParameter.HEIGHT * 2 + 3 * OFFSET + 80);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        spinner.setName(Language.TYPE);
        spinner.setElementNames(Language.TYPE_TITLES);
    }
}
