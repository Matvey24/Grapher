package controller;

import framesLib.MyFrame;
import framesLib.Screen;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Parametric;
import view.settings.FunctionSettings;
import view.settings.ParametricSettings;
import view.settings.TimerSettings;

import javax.swing.*;

public class SettingsManager {
    private MyFrame frame;
    private FunctionSettings functionSettings;
    private ParametricSettings parametricSettings;
    private TimerSettings timerSettings;
    SettingsManager(ModelUpdater updater){
        functionSettings = new FunctionSettings(updater);
        parametricSettings = new ParametricSettings(updater);
        timerSettings = new TimerSettings(updater);
    }
    void openFunctionSettings(Function f, TextElement e){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.clearStack();
        functionSettings.setInfo(f, e);
        frame.changeScreen(functionSettings);
        frame.setFocusable(true);
    }
    void openParameterSettings(Parametric p, TextElement e){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.clearStack();
        parametricSettings.setInfo(p, e);
        frame.changeScreen(parametricSettings);
        frame.setFocusable(true);
    }
    void openTimerSettings(){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.clearStack();
        frame.changeScreen(timerSettings);
        frame.setFocusable(true);
    }
    void close(){
        if(frame != null && frame.isVisible())
            frame.dispose();
    }
    public static JComboBox<String> createSpinner(Screen screen, int y, int width){
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
    Double getTime(){
        return timerSettings.getT();
    }
}
