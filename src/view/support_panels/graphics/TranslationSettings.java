package view.support_panels.graphics;

import controller.ModelUpdater;
import model.GraphType;
import model.Language;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Translation;

import static view.elements.ElementsList.OFFSET;
public class TranslationSettings extends Settings {
    private Parameter lps;
    private Translation tr;
    public TranslationSettings(ModelUpdater updater){
        super(updater);
        lps = new Parameter(Language.LINES_PER_CELL, (s)->{
            if (tr != null) {
                int n = Integer.parseInt(s);
                if (n < 1) {
                    lps.setDefault(String.valueOf(tr.getMultiplyer()));
                } else {
                    tr.setMultiplyer(n);
                    updater.runResize();
                }
            }
        });
        lps.addTo(this);
        lps.setBounds(OFFSET, HEIGHT, TextElement.WIDTH);
    }
    public void setInfo(Translation tr, TextElement e) {
        super.setInfo(tr, e);
        this.tr = tr;
        lps.setDefault(String.valueOf(tr.getMultiplyer()));
    }
    @Override
    public Graphic getGraphic() {
        return tr;
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH,HEIGHT + ComboBoxParameter.HEIGHT + OFFSET);
    }
    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.TRANSLATION.ordinal()]);
    }
    public void updateLanguage(){
        super.updateLanguage();
        lps.setName(Language.LINES_PER_CELL);
    }
}
