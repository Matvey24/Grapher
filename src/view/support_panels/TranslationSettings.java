package view.support_panels;

import controller.ModelUpdater;
import model.GraphType;
import model.Language;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Translation;

import static view.elements.ElementsList.OFFSET;

public class TranslationSettings extends Settings {
    private Parameter mapSize;
    private Parameter lps;
    private Translation tr;
    private TextElement el;
    public TranslationSettings(ModelUpdater updater){
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (s) -> {
            if (tr != null) {
                int n = Integer.parseInt(s);
                if (n < 2) {
                    mapSize.setDefault(String.valueOf(tr.MAP_SIZE));
                } else {
                    tr.setMAP_SIZE(Integer.parseInt(s));
                    updater.runResize();
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, ComboBoxParameter.WIDTH);
        lps = new Parameter(Language.LINES_PER_CELL, (s)->{
            if (tr != null) {
                int n = Integer.parseInt(s);
                if (n < 1) {
                    mapSize.setDefault(String.valueOf(tr.multiplyer));
                } else {
                    tr.multiplyer = n;
                    updater.runResize();
                }
            }
        });
        lps.addTo(this);
        lps.setBounds(OFFSET, 2*OFFSET+ComboBoxParameter.HEIGHT, ComboBoxParameter.WIDTH);
    }
    public void setInfo(Translation tr, TextElement e) {
        this.tr = tr;
        this.el = e;
        mapSize.setDefault(String.valueOf(tr.MAP_SIZE));
        lps.setDefault(String.valueOf(tr.multiplyer));
    }
    @Override
    public Graphic getGraphic() {
        return tr;
    }

    @Override
    public TextElement getTextElement() {
        return el;
    }

    @Override
    public void onSetSize() {
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40, 2*ComboBoxParameter.HEIGHT + 3 * OFFSET + 80);
    }
    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.TRANSLATION.ordinal()]);
    }
    public void updateLanguage(){
        mapSize.setName(Language.DISCRETIZATION);
        lps.setName(Language.LINES_PER_CELL);
    }
}
