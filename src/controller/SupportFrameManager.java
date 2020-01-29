package controller;

import framesLib.MyFrame;
import framesLib.Screen;
import framesLib.TextPanel;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Implicit;
import view.grapher.graphics.Parametric;
import view.settings.*;

import javax.swing.*;

public class SupportFrameManager {
    private MyFrame frame;
    private final FunctionSettings functionSettings;
    private final ParametricSettings parametricSettings;
    private final ImplicitSettings implicitSettings;
    private final TimerSettings timerSettings;
    SupportFrameManager(ModelUpdater updater){
        functionSettings = new FunctionSettings(updater);
        parametricSettings = new ParametricSettings(updater);
        timerSettings = new TimerSettings(updater);
        implicitSettings = new ImplicitSettings(updater);
    }
    void openFunctionSettings(Function f, TextElement e){
        checkFrame();
        functionSettings.setInfo(f, e);
        frame.changeScreen(functionSettings);
        frame.setFocusable(true);
    }
    void openParameterSettings(Parametric p, TextElement e){
        checkFrame();
        parametricSettings.setInfo(p, e);
        frame.changeScreen(parametricSettings);
        frame.setFocusable(true);
    }
    void openImplicitSettings(Implicit imp, TextElement e){
        checkFrame();
        implicitSettings.setInfo(imp, e);
        frame.changeScreen(implicitSettings);
        frame.setFocusable(true);
    }
    void openTimerSettings(){
        checkFrame();
        frame.changeScreen(timerSettings);
        frame.setFocusable(true);
    }
    void openUpdaterFrame(String version){
        checkFrame();
        frame.changeScreen(new UpdaterFrame(version));
        frame.setFocusable(true);
    }
    public void openTextFrame(TextPanel panel){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.changeScreen(panel);
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
        else {
            frame.clearStack();
        }
    }
    Double getTime(){
        return timerSettings.getT();
    }
}
