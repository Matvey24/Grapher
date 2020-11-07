package view.support_panels;

import controller.ModelUpdater;
import controller.VersionController;
import framesLib.Screen;
import framesLib.TextPanel;
import model.Language;
import model.help.TextViewer;
import view.elements.ElementsList;
import view.elements.TextElement;

import javax.swing.*;

import static view.elements.ElementsList.OFFSET;

public class HelperFrame extends Screen {
    private final JButton[] btn_arr;
    private final JButton btn_versionLog;
    private int height;

    public HelperFrame(ModelUpdater mu) {
        setLayout(null);
        btn_arr = new JButton[Language.HELP_NAMES.length];
        height = OFFSET;
        for (int i = 0; i < btn_arr.length; ++i) {
            JButton btn = new JButton(Language.HELP_NAMES[i]);
            btn.setFocusPainted(false);
            btn.setBounds(OFFSET, height, TextElement.WIDTH, TextElement.HEIGHT);
            height += OFFSET + TextElement.HEIGHT;
            add(btn);
            final int j = i;
            btn.addActionListener(e -> changeScreen(TextViewer.openText(TextViewer.help_names[j])));
            btn_arr[i] = btn;
        }
        btn_versionLog = new JButton(Language.VERSION_LOG);
        btn_versionLog.setFocusPainted(false);
        btn_versionLog.setBounds(OFFSET, height, TextElement.WIDTH, TextElement.HEIGHT);
        height += (OFFSET << 1) + TextElement.HEIGHT;
        add(btn_versionLog);
        btn_versionLog.addActionListener(e ->
                mu.run(() -> {
                    String[][] log = VersionController.loadLog();
                    if (log == null) {
                        mu.setState(Language.LOADING_LOG);
                    } else {
                        changeScreen(new TextPanel(log, Language.VERSION_LOG, Language.BACK));
                    }
                }));
    }

    public void updateLanguage() {
        for (int i = 0; i < btn_arr.length; ++i)
            btn_arr[i].setText(Language.HELP_NAMES[i]);
        TextViewer.updateLanguage();
        btn_versionLog.setText(Language.VERSION_LOG);
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
