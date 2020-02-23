package view.support_panels;

import controller.ModelUpdater;
import framesLib.Screen;
import model.Language;
import model.help.TextViewer;
import view.elements.TextElement;

import javax.swing.*;

import static view.elements.ElementsList.OFFSET;

public class HelperFrame extends Screen {
    private JButton btn_help;
    private JButton btn_calc_help;
    public HelperFrame(ModelUpdater updater){
        setLayout(null);
        btn_help = new JButton(Language.USER_HELP);
        btn_help.setBounds(OFFSET,OFFSET,200, TextElement.HEIGHT);
        add(btn_help);
        btn_help.addActionListener(e->updater.getSupportFrameManager().openTextFrame(TextViewer.openText("help")));
        btn_calc_help = new JButton(Language.CALC_HELP);
        btn_calc_help.setBounds(OFFSET,TextElement.HEIGHT + 2 * OFFSET,200,TextElement.HEIGHT);
        add(btn_calc_help);
        btn_calc_help.addActionListener((e)->  updater.getSupportFrameManager().openTextFrame(TextViewer.openText("calc_help")));

    }
    public void updateLanguage(){
        btn_help.setText(Language.USER_HELP);
        btn_calc_help.setText(Language.CALC_HELP);
        TextViewer.updateLanguage();
    }
    @Override
    public void onSetSize() {
        setSize(230, 110);
    }
}
