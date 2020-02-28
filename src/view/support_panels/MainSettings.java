package view.support_panels;

import controller.DataBase;
import controller.ModelUpdater;
import framesLib.Screen;
import model.Language;
import view.elements.ComboBoxParameter;
import view.elements.TextElement;

import javax.swing.*;
import java.awt.event.ItemEvent;

import static view.elements.ElementsList.OFFSET;

public class MainSettings extends Screen {
    private ModelUpdater modelUpdater;
    private ComboBoxParameter language;
    private FileChooser fileChooser;
    private JButton btn_load;
    private JButton btn_save;
    private boolean selectionSave;
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
        fileChooser = new FileChooser(this);
        btn_save = new JButton(Language.SAVE_PRJ);
        btn_save.addActionListener((e)->{
            changeScreen(fileChooser);
            selectionSave = true;
        });
        btn_save.setBounds(OFFSET, 4*OFFSET + ComboBoxParameter.HEIGHT,
                ComboBoxParameter.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        add(btn_save);
        btn_load = new JButton(Language.LOAD_PRJ);
        btn_load.addActionListener((e)->{
            changeScreen(fileChooser);
            selectionSave = false;
        });
        add(btn_load);
        btn_load.setBounds(2*OFFSET + ComboBoxParameter.WIDTH/2, 4*OFFSET + ComboBoxParameter.HEIGHT,
                ComboBoxParameter.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
    }
    public void save(java.io.File file){
        modelUpdater.dosave(selectionSave, file);
    }
    public void setLanguage(int idx){
        language.setSelectedIndex(idx);
    }
    @Override
    public void onSetSize() {
        setSize( TextElement.WIDTH + 2*OFFSET + 30, 200);
    }
    public void updateLanguage(){
        language.setName(Language.LANGUAGE);
        btn_save.setText(Language.SAVE_PRJ);
        btn_load.setText(Language.LOAD_PRJ);
    }
}
