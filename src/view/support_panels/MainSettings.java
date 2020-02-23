package view.support_panels;

import controller.ModelUpdater;
import framesLib.Screen;
import model.Language;
import view.elements.ComboBoxParameter;

import java.awt.event.ItemEvent;

import static view.elements.ElementsList.OFFSET;

public class MainSettings extends Screen {
    private ModelUpdater modelUpdater;
    ComboBoxParameter language;
    public MainSettings(ModelUpdater modelUpdater){
        this.modelUpdater = modelUpdater;
        setLayout(null);
        language = new ComboBoxParameter(Language.LANGUAGE, Language.language_Names);
        language.setBounds(OFFSET,OFFSET);
        language.setSelectedIndex(Language.LANGUAGE_INDEX);
        language.addTo(this);
        language.addItemListener((e)->{
            if(e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    modelUpdater.setState(Language.loadLanguage(e.getItem().toString()));
                }catch (Exception ex){
                    modelUpdater.setState(ex.toString());
                }
                modelUpdater.updateLanguage();
            }
        });
    }
    @Override
    public void onSetSize() {
        setSize(200, 200);
    }
    public void updateLanguage(){
        language.setName(Language.LANGUAGE);
    }
}
