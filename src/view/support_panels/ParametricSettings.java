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
    private final Parameter dimension;
    private Parametric p;

    public ParametricSettings(ModelUpdater updater) {
        super(updater);
        dimension = new Parameter(Language.DIMENSION, (s) -> {
            if (p != null) {
                String[] dim = s.split(":");
                double start = Double.parseDouble(dim[0]);
                double end = Double.parseDouble(dim[1]);
                p.updateBoards(start, end);
                updater.runResize();
            }
        });
        dimension.addTo(this);
        dimension.setBounds(OFFSET, HEIGHT, ComboBoxParameter.WIDTH);
    }

    public void setInfo(Parametric p, TextElement e) {
        super.setInfo(p, e);
        this.p = p;
        dimension.setDefault(p.getStartT() + ":" + p.getEndT());
    }

    @Override
    public Graphic getGraphic() {
        return p;
    }

    @Override
    public void onSetSize() {
        setSize(WIDTH,HEIGHT + ComboBoxParameter.HEIGHT + OFFSET);
    }
    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.PARAMETRIC.ordinal()]);
    }
    public void updateLanguage() {
        super.updateLanguage();
        dimension.setName(Language.DIMENSION);
    }
}
