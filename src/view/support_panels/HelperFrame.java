package view.support_panels;

import framesLib.Screen;
import model.Language;
import model.help.TextViewer;
import view.elements.ElementsList;
import view.elements.TextElement;

import javax.swing.*;

import static view.elements.ElementsList.OFFSET;

public class HelperFrame extends Screen {
    private final JButton[] btn_arr;
    private int height;
    public HelperFrame(){
        setLayout(null);
        btn_arr = new JButton[Language.HELP_NAMES.length];
        height = OFFSET;
        for(int i = 0; i < Language.HELP_NAMES.length; ++i){
            JButton btn = new JButton(Language.HELP_NAMES[i]);
            btn.setFocusPainted(false);
            btn.setBounds(OFFSET, height, TextElement.WIDTH, TextElement.HEIGHT);
            height += OFFSET + TextElement.HEIGHT;
            add(btn);
            final int j = i;
            btn.addActionListener(e->changeScreen(TextViewer.openText(TextViewer.help_names[j])));
            btn_arr[i] = btn;
        }
        height += OFFSET;
    }
    public void updateLanguage(){
        for(int i = 0; i < btn_arr.length; ++i)
            btn_arr[i].setText(Language.HELP_NAMES[i]);
        TextViewer.updateLanguage();
    }

    @Override
    public void onShow() {
        super.onShow();
        setTitle(Language.HELP);
    }

    @Override
    public void onSetSize() {
        setSize(ElementsList.WIDTH, height);
    }
}
