package view.settings;

import controller.GraphType;
import controller.ModelUpdater;
import controller.ViewElement;
import framesLib.MyFrame;
import framesLib.Screen;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Parameter;

import javax.swing.*;

public class SettingsManager {
    private MyFrame frame;
    private FunctionSettings functionSettings;
    private ParameterSettings parameterSettings;
    public SettingsManager(ModelUpdater updater){
        functionSettings = new FunctionSettings(updater);
        parameterSettings = new ParameterSettings(updater);
    }
    public void openFunctionSettings(Function f, TextElement e){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.clearStack();
        functionSettings.setInfo(f, e);
        frame.changeScreen(functionSettings);
    }
    public void openParameterSettings(Parameter p, TextElement e){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.clearStack();
        parameterSettings.setInfo(p, e);
        frame.changeScreen(parameterSettings);
    }
    public void close(){
        if(frame != null && frame.isVisible())
            frame.dispose();
    }
    static JComboBox<String> createSpinner(Screen screen, int y, int width){
        JLabel spinnerName = new JLabel("Type");
        spinnerName.setBounds(10,y + 10, width,30);
        spinnerName.setFont(ViewElement.name_font);
        screen.add(spinnerName);
        JComboBox<String> spinner = new JComboBox<>();
        screen.add(spinner);
        spinner.addItem(GraphType.titles[0]);
        spinner.addItem(GraphType.titles[1]);
        spinner.setBounds(10,y + 40, width, 30);
        return spinner;
    }
}
