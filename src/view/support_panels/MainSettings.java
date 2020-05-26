package view.support_panels;

import controller.ModelUpdater;
import framesLib.Screen;
import model.Language;
import view.elements.ComboBoxParameter;
import view.elements.Parameter;
import view.elements.TextElement;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.File;

import static view.elements.ElementsList.OFFSET;

public class MainSettings extends Screen {
    private final ModelUpdater modelUpdater;
    private final ComboBoxParameter language;
    private final FileChooser fileChooser;
    private final JButton btn_load;
    private final JButton btn_save;
    private Parameter lineSpace;
    private Parameter graphics_count;
    private boolean selectionSave;
    public MainSettings(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
        setLayout(null);
        lineSpace = new Parameter(Language.SETTINGS[1], (s) -> {
            int space = Integer.parseInt(s);
            if(space < 10) {
                lineSpace.setDefault("" + modelUpdater.getCoordinateSystem().getMinDelta());
                return;
            }
            modelUpdater.getCoordinateSystem().setMIN_DELTA(space);
            modelUpdater.runResize();
        });
        lineSpace.setBounds(OFFSET, 2 * OFFSET + TextElement.HEIGHT, ComboBoxParameter.WIDTH);
        lineSpace.addTo(this);

        graphics_count = new Parameter(Language.SETTINGS[2], (s) -> {
            int count = Integer.parseInt(s);
            if(count < 2 || count > 300){
                graphics_count.setDefault("" + modelUpdater.list.getMAX_SIZE());
                return;
            }else if(count == modelUpdater.list.getMAX_SIZE())
                return;
            modelUpdater.list.setMAX_SIZE(count);
            modelUpdater.rebounds();
        });
        graphics_count.setBounds(OFFSET,
                3 * OFFSET + TextElement.HEIGHT + ComboBoxParameter.HEIGHT,
                ComboBoxParameter.WIDTH);
        graphics_count.addTo(this);

        language = new ComboBoxParameter(Language.SETTINGS[0], Language.language_Names);
        language.setBounds(OFFSET, OFFSET*4 + 2*ComboBoxParameter.HEIGHT + TextElement.HEIGHT);
        language.setSelectedIndex(Language.LANGUAGE_INDEX);
        language.addTo(this);
        language.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    modelUpdater.setState(Language.loadLanguage(e.getItem().toString()));
                } catch (Exception ex) {
                    modelUpdater.setState(ex.toString());
                }
                modelUpdater.updateLanguage();
            }
        });
        fileChooser = new FileChooser();
        fileChooser.setActionListener((e)->{
            if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
                fileChooser.back();
            if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                File f = fileChooser.getSelectedFile();
                save(f);
                fileChooser.back();
            }
        });
        btn_save = new JButton(Language.SAVE_PRJ);
        btn_save.setFocusPainted(false);
        btn_save.addActionListener((e) -> {
            changeScreen(fileChooser);
            selectionSave = true;
        });
        btn_save.setBounds(OFFSET, OFFSET,
                ComboBoxParameter.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        add(btn_save);
        btn_load = new JButton(Language.LOAD_PRJ);
        btn_load.setFocusPainted(false);
        btn_load.addActionListener((e) -> {
            changeScreen(fileChooser);
            selectionSave = false;
        });
        add(btn_load);
        btn_load.setBounds(3 * OFFSET / 2 + ComboBoxParameter.WIDTH / 2, OFFSET,
                ComboBoxParameter.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
    }
    @Override
    public void onShow(){
        setTitle(Language.MAIN_SETTINGS);
        lineSpace.setDefault(String.valueOf(modelUpdater.getCoordinateSystem().getMinDelta()));
        graphics_count.setDefault(String.valueOf(modelUpdater.list.getMAX_SIZE()));
    }

    public void save(java.io.File file) {
        modelUpdater.dosave(selectionSave, file);
    }
    public void setLanguage(int idx) {
        language.setSelectedIndex(idx);
    }

    @Override
    public void onSetSize() {
        setSize(TextElement.WIDTH + 2 * OFFSET + 30, 4 * OFFSET + 3* ComboBoxParameter.HEIGHT + TextElement.HEIGHT + 80);
    }

    public void updateLanguage() {
        setTitle(Language.MAIN_SETTINGS);
        language.setName(Language.SETTINGS[0]);
        btn_save.setText(Language.SAVE_PRJ);
        btn_load.setText(Language.LOAD_PRJ);
        lineSpace.setName(Language.SETTINGS[1]);
        graphics_count.setName(Language.SETTINGS[2]);
    }
}
