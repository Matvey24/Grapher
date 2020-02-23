package controller;

import framesLib.MyFrame;
import framesLib.TextPanel;
import model.GraphType;
import model.Language;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Item;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Implicit;
import view.grapher.graphics.Parametric;
import view.support_panels.*;

import java.awt.event.ItemEvent;

public class SupportFrameManager {
    private MyFrame frame;
    private final FunctionSettings functionSettings;
    private final ParametricSettings parametricSettings;
    private final ImplicitSettings implicitSettings;
    private final TimerSettings timerSettings;
    private final HelperFrame helperFrame;
    private final MainSettings mainSettings;
    SupportFrameManager(ModelUpdater updater){
        functionSettings = new FunctionSettings(updater);
        parametricSettings = new ParametricSettings(updater);
        timerSettings = new TimerSettings(updater);
        implicitSettings = new ImplicitSettings(updater);
        helperFrame = new HelperFrame(updater);
        mainSettings = new MainSettings(updater);
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
    public void openHelperFrame(){
        checkFrame();
        frame.changeScreen(helperFrame);
        frame.setFocusable(true);
    }
    public void openTextFrame(TextPanel panel){
        if(frame == null || !frame.isVisible())
            frame = new MyFrame(true);
        frame.changeScreen(panel);
        frame.setFocusable(true);
    }
    public void openMainSettings(){
        checkFrame();
        frame.changeScreen(mainSettings);
        frame.setFocusable(true);
    }
    void close(){
        if(frame != null && frame.isVisible())
            frame.dispose();
    }
    public static ComboBoxParameter createSpinner(ModelUpdater updater, Settings settings, int y){
        ComboBoxParameter spinner = new ComboBoxParameter(Language.TYPE, Language.TYPE_TITLES);
        spinner.setBounds(10, y);
        spinner.addTo(settings);
        if(settings instanceof FunctionSettings)
            spinner.setSelectedIndex(GraphType.FUNCTION.ordinal());
        else if(settings instanceof ParametricSettings)
            spinner.setSelectedIndex(GraphType.PARAMETER.ordinal());
        else if(settings instanceof ImplicitSettings)
            spinner.setSelectedIndex(GraphType.IMPLICIT.ordinal());
        spinner.addItemListener(e -> {
            if(!(e.getStateChange() == ItemEvent.SELECTED))
                return;
            Object item = ((Item)e.getItem()).name;
            if (Language.TYPE_TITLES[GraphType.FUNCTION.ordinal()] == item)
                updater.makeFunction(settings.getGraphic(), settings.getTextElement());
            else if (Language.TYPE_TITLES[GraphType.PARAMETER.ordinal()] == item)
                updater.makeParameter(settings.getGraphic(), settings.getTextElement());
            else if (Language.TYPE_TITLES[GraphType.IMPLICIT.ordinal()] == item)
                updater.makeImplicit(settings.getGraphic(), settings.getTextElement());
            if(settings instanceof FunctionSettings)
                spinner.setSelectedIndex(GraphType.FUNCTION.ordinal());
            else if(settings instanceof ParametricSettings)
                spinner.setSelectedIndex(GraphType.PARAMETER.ordinal());
            else if(settings instanceof ImplicitSettings)
                spinner.setSelectedIndex(GraphType.IMPLICIT.ordinal());
        });
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
    public void updateLanguage(){
        helperFrame.updateLanguage();
        mainSettings.updateLanguage();
        functionSettings.updateLanguage();
        implicitSettings.updateLanguage();
        parametricSettings.updateLanguage();
        timerSettings.updateLanguage();
    }
}
