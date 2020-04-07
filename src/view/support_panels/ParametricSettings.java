package view.support_panels;

import model.GraphType;
import model.Language;
import controller.ModelUpdater;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Parametric;

import static view.elements.ElementsList.OFFSET;

public class ParametricSettings extends Settings {
    private Parameter mapSize;
    private Parameter dimension;
    private Parametric p;
    private TextElement el;

    public ParametricSettings(ModelUpdater updater) {
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (s) -> {
            if (p != null) {
                int n = Integer.parseInt(s);
                if (n < 2) {
                    mapSize.setDefault(String.valueOf(p.MAP_SIZE));
                } else {
                    p.setMAP_SIZE(Integer.parseInt(s));
                    updater.runResize();
                }
            }
        });
        dimension = new Parameter(Language.DIMENSION, (s) -> {
            if (p != null) {
                String[] dim = s.split(":");
                double start = Double.parseDouble(dim[0]);
                double end = Double.parseDouble(dim[1]);
                p.updateBoards(start, end);
                updater.runResize();
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, ComboBoxParameter.WIDTH);
        dimension.addTo(this);
        dimension.setBounds(OFFSET, 2 * OFFSET + ComboBoxParameter.HEIGHT, ComboBoxParameter.WIDTH);
    }

    public void setInfo(Parametric p, TextElement e) {
        this.p = p;
        this.el = e;
        mapSize.setDefault(String.valueOf(p.MAP_SIZE));
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
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40, ComboBoxParameter.HEIGHT * 2 + 3 * OFFSET + 80);
    }
    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.PARAMETRIC.ordinal()]);
    }
    public void updateLanguage() {
        mapSize.setName(Language.DISCRETIZATION);
        dimension.setName(Language.DIMENSION);
    }
}
