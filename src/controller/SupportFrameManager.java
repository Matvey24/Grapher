package controller;

import framesLib.screenables.InternalPanel;

import framesLib.screenables.PanelFrameOperator;
import view.elements.TextElement;
import view.grapher.graphics.Function;
import view.grapher.graphics.Implicit;
import view.grapher.graphics.Parametric;
import view.grapher.graphics.Translation;
import view.support_panels.*;

public class SupportFrameManager {
    private PanelFrameOperator pfo;
    private final FunctionSettings functionSettings;
    private final ParametricSettings parametricSettings;
    private final ImplicitSettings implicitSettings;
    private final TranslationSettings translationSettings;
    private final TimerSettings timerSettings;
    private final HelperFrame helperFrame;
    private final MainSettings mainSettings;
    private final ColorChooser colorc;
    private boolean openedGraphic;
    SupportFrameManager(ModelUpdater updater){
        functionSettings = new FunctionSettings(updater);
        parametricSettings = new ParametricSettings(updater);
        timerSettings = new TimerSettings(updater);
        implicitSettings = new ImplicitSettings(updater);
        translationSettings = new TranslationSettings(updater);
        helperFrame = new HelperFrame(updater);
        mainSettings = new MainSettings(updater);
        colorc = new ColorChooser();
    }
    public void setPanel(InternalPanel panel){
        this.pfo = new PanelFrameOperator(panel);
    }

    void openFunctionSettings(Function f, TextElement e){
        functionSettings.setInfo(f, e);
        pfo.changeScreenClearing(functionSettings, true);
        openedGraphic = true;
    }
    void openParameterSettings(Parametric p, TextElement e){
        parametricSettings.setInfo(p, e);
        pfo.changeScreenClearing(parametricSettings, true);
        openedGraphic = true;
    }
    void openImplicitSettings(Implicit imp, TextElement e){
        implicitSettings.setInfo(imp, e);
        pfo.changeScreenClearing(implicitSettings, true);
        openedGraphic = true;
    }
    void openTranslationSettings(Translation tr, TextElement e){
        translationSettings.setInfo(tr, e);
        pfo.changeScreenClearing(translationSettings, true);
        openedGraphic = true;
    }
    void openTimerSettings(){
        pfo.changeScreenClearing(timerSettings, true);
        openedGraphic = false;
    }
    void openUpdaterFrame(VersionController.UpdateInfo info){
        pfo.changeScreen(new UpdaterFrame(info));
        openedGraphic = false;
    }
    public void openHelperFrame(){
        pfo.changeScreenClearing(helperFrame, true);
        openedGraphic = false;
    }
    public void openMainSettings(){
        pfo.changeScreenClearing(mainSettings, true);
        openedGraphic = false;
    }
    public void openFileChooser(boolean save){
        pfo.changeScreenClearing(mainSettings.getFileChooser(), true);
        mainSettings.setSelectionSave(save);
        openedGraphic = false;
    }
    Double getTime(){
        return timerSettings.getT();
    }
    public TimerSettings getTimer(){
        return timerSettings;
    }

    public boolean isOpenedGraphic() {
        return openedGraphic;
    }

    public MainSettings getMainSettings() {
        return mainSettings;
    }
    public void onPanelResize(){
        pfo.onPanelResize();
    }
    public void close(){
        pfo.clearStack();
        openedGraphic = false;
    }

    public ColorChooser getColorc() {
        return colorc;
    }

    public void updateLanguage(){
        helperFrame.updateLanguage();
        mainSettings.updateLanguage();
        functionSettings.updateLanguage();
        parametricSettings.updateLanguage();
        implicitSettings.updateLanguage();
        translationSettings.updateLanguage();
        timerSettings.updateLanguage();
        colorc.updateLanguage();
    }
}
