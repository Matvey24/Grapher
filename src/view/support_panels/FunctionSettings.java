package view.support_panels;

import model.GraphType;
import model.Language;
import controller.ModelUpdater;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Graphic;

import static view.elements.ElementsList.OFFSET;

public class FunctionSettings extends Settings {
    private Parameter mapSize;
    private Function f;
    private TextElement el;

    public FunctionSettings(ModelUpdater updater) {
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (s) -> {
            if (f != null) {
                int n = Integer.parseInt(s);
                if (n < 2) {
                    mapSize.setDefault(String.valueOf(f.MAP_SIZE));
                } else {
                    f.setMAP_SIZE(Integer.parseInt(s));
                    updater.runResize();
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, ComboBoxParameter.WIDTH);
    }

    @Override
    public Graphic getGraphic() {
        return f;
    }

    @Override
    public TextElement getTextElement() {
        return el;
    }

    public void setInfo(Function f, TextElement e) {
        this.f = f;
        this.el = e;
        mapSize.setDefault(String.valueOf(f.MAP_SIZE));
    }

    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.FUNCTION.ordinal()]);
    }

    @Override
    public void onSetSize() {
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40, ComboBoxParameter.HEIGHT + 2 * OFFSET + 80);
    }

    public void updateLanguage() {
        mapSize.setName(Language.DISCRETIZATION);
    }
}
