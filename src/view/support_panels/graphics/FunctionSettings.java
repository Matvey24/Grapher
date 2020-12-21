package view.support_panels.graphics;

import model.GraphType;
import model.Language;
import controller.ModelUpdater;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Graphic;

public class FunctionSettings extends Settings {
    private Function f;

    public FunctionSettings(ModelUpdater updater) {
        super(updater);
    }

    @Override
    public Graphic getGraphic() {
        return f;
    }

    public void setInfo(Function f, TextElement e) {
        this.f = f;
        super.setInfo(f, e);
    }

    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.FUNCTION.ordinal()]);
    }

    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
}
