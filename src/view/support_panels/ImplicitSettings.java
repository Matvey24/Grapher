package view.support_panels;

import model.Language;
import controller.ModelUpdater;
import controller.SupportFrameManager;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Implicit;
import static view.elements.ElementsList.OFFSET;

public class ImplicitSettings extends Settings {
    private final Parameter mapSize;
    private final Parameter sensitivity;
    private Implicit imp;
    private TextElement el;
    public ImplicitSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (e)->{
            if(imp != null){
                imp.setMAP_SIZE(Integer.parseInt(e.getSource().toString()));
                updater.runResize();
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, ComboBoxParameter.WIDTH);
        sensitivity = new Parameter(Language.SENSITIVITY, e->{
            if(imp != null){
                imp.setSensitivity(Double.parseDouble(e.getSource().toString()));
                updater.runResize();
            }
        });
        sensitivity.addTo(this);
        sensitivity.setBounds(OFFSET, 2 * OFFSET + ComboBoxParameter.HEIGHT, ComboBoxParameter.WIDTH);
    }
    public void setInfo(Implicit imp, TextElement e){
        this.imp = imp;
        this.el = e;
        sensitivity.setDefault(imp.getSensitivity() + "");
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
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40,
                ComboBoxParameter.HEIGHT * 2 + 3 * OFFSET + 80);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        sensitivity.setName(Language.SENSITIVITY);
    }
}
