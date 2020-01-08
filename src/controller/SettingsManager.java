package controller;

import framesLib.MyFrame;
import framesLib.Screen;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Implicit;
import view.grapher.graphics.Parametric;
import view.settings.*;

import javax.swing.*;

public class SettingsManager {
    private MyFrame frame;
    private FunctionSettings functionSettings;
    private ParametricSettings parametricSettings;
    private ImplicitSettings implicitSettings;
    private TimerSettings timerSettings;
    SettingsManager(ModelUpdater updater){
        functionSettings = new FunctionSettings(updater);
        parametricSettings = new ParametricSettings(updater);
        timerSettings = new TimerSettings(updater);
        implicitSettings = new ImplicitSettings(updater);
    }
    void openFunctionSettings(Function f, TextElement e){
        checkFrame();
        frame.clearStack();
        functionSettings.setInfo(f, e);
        frame.changeScreen(functionSettings);
        frame.setFocusable(true);
    }
    void openParameterSettings(Parametric p, TextElement e){
        checkFrame();
        frame.clearStack();
        parametricSettings.setInfo(p, e);
        frame.changeScreen(parametricSettings);
        frame.setFocusable(true);
    }
    void openImplicitSettings(Implicit imp, TextElement e){
        checkFrame();
        frame.clearStack();
        implicitSettings.setInfo(imp, e);
        frame.changeScreen(implicitSettings);
        frame.setFocusable(true);
    }
    void openTimerSettings(){
        checkFrame();
        frame.clearStack();
        frame.changeScreen(timerSettings);
        frame.setFocusable(true);
    }
    void openUpdaterFrame(String version){
        checkFrame();
        frame.changeScreen(new UpdaterFrame(version));
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
        for(int i = 0; i < GraphType.titles.length; ++i)
            spinner.addItem(GraphType.titles[i]);
        spinner.setBounds(10,y + 40, width, 30);
        return spinner;
    }
    private void checkFrame(){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
    }
    Double getTime(){
        return timerSettings.getT();
    }
}
