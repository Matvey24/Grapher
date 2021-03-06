package view.support_panels;

import controller.ModelUpdater;
import framesLib.Screen;
import model.Language;
import model.FullModel;
import view.elements.ComboBoxParameter;
import view.elements.ElementsList;
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
    private final Parameter lineSpace;
    private Parameter graphics_count;
    private boolean selectionSave;
    public MainSettings(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
        setLayout(null);
        lineSpace = new Parameter(Language.SETTINGS[1], this::setNetWidth);
        lineSpace.setBounds(OFFSET, 2 * OFFSET + TextElement.HEIGHT, TextElement.WIDTH);
        lineSpace.addTo(this);

        graphics_count = new Parameter(Language.SETTINGS[2], (s) -> {
            int count = Integer.parseInt(s);
            if(!ElementsList.checkValidCount(count)){
                graphics_count.setDefault("" + modelUpdater.list.getMAX_SIZE());
                return;
            }else if(count == modelUpdater.list.getMAX_SIZE())
                return;
            modelUpdater.list.setMAX_SIZE(count);
            modelUpdater.rebounds();
        });
        graphics_count.setBounds(OFFSET,
                3 * OFFSET + TextElement.HEIGHT + ComboBoxParameter.HEIGHT,
                TextElement.WIDTH);
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
        fileChooser.setSelectedFileFilter(FileChooser.GRAPHER_PROJECT);
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
        btn_save.addActionListener((e) -> {
            changeScreen(fileChooser);
            setSelectionSave(true);
        });
        btn_save.setBounds(OFFSET, OFFSET,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        add(btn_save);
        btn_load = new JButton(Language.LOAD_PRJ);
        btn_load.addActionListener((e) -> {
            changeScreen(fileChooser);
            setSelectionSave(false);
        });
        add(btn_load);
        btn_load.setBounds(3 * OFFSET / 2 + TextElement.WIDTH / 2, OFFSET,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
    }
    private void setNetWidth(String s){
        s = s.replaceAll("[ \n\t\r]", "");
        if(s.charAt(0) == '-'){
            s = s.substring(1);
            modelUpdater.getMainPanel().getGraphicsView().draw_coords = false;
        }else{
            modelUpdater.getMainPanel().getGraphicsView().draw_coords = true;
        }
        int space = Integer.parseInt(s);
        if(space < 10) {
            lineSpace.setDefault("" + modelUpdater.getCoordinateSystem().getMinDelta());
            return;
        }
        modelUpdater.getCoordinateSystem().setMIN_DELTA(space);
        modelUpdater.runResize();
    }
    private String getLineSpaceText(){
        String s = String.valueOf(modelUpdater.getCoordinateSystem().getMinDelta());
        if(modelUpdater.getMainPanel().getGraphicsView().draw_coords){
            return s;
        }else{
            return "-" + s;
        }
    }
    public void setSelectionSave(boolean selectionSave) {
        this.selectionSave = selectionSave;
        if(selectionSave){
            fileChooser.setCurrTitle(Language.SAVE_PRJ);
        }else{
            fileChooser.setCurrTitle(Language.LOAD_PRJ);
        }
    }

    public FileChooser getFileChooser(){
        return fileChooser;
    }
    @Override
    public void onShow(){
        setTitle(Language.MAIN_SETTINGS);
        lineSpace.setDefault(getLineSpaceText());
        graphics_count.setDefault(String.valueOf(modelUpdater.list.getMAX_SIZE()));
    }

    public void save(java.io.File file) {
        modelUpdater.dosave(selectionSave, file);
    }
    public void setLanguage(int idx) {
        language.setSelectedIndex(idx);
    }
    public void makeModel(FullModel m){
        m.main_settings = getLineSpaceText();
    }
    public void fromModel(FullModel fm){
        String st = fm.main_settings;
        if(st.isEmpty())
            return;
        String[] arr = st.split("\n");
        if(arr.length > 0){
            String space = arr[0];
            setNetWidth(space);
        }
        if(arr.length > 1){
            String name = arr[1];
            int idx = Language.language_Names.indexOf(name);
            if(idx != -1)
                setLanguage(idx);
        }
    }
    @Override
    public void onSetSize() {
        setSize(TextElement.WIDTH + 2 * OFFSET, 4 * OFFSET + 3* ComboBoxParameter.HEIGHT + TextElement.HEIGHT + 40);
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
