package view.support_panels;

import controller.VersionController;
import framesLib.Screen;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UpdaterFrame extends Screen {
    public final JLabel label;
    public final JTextArea changes;
    public final JButton btn_cancel;
    public final JButton btn_update;
    private ActionListener listener;

    public UpdaterFrame(VersionController.UpdateInfo info) {
        setLayout(null);
        label = new JLabel("Found new version: " + info.full_name);
        label.setBounds(5, 5, 250, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        changes = new JTextArea();
        JScrollPane pane = new JScrollPane(changes);
        pane.setBounds(5, 50, 250, 80);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        changes.setEditable(false);
        changes.setLineWrap(true);
        changes.setText(info.changes);
        add(pane);

        btn_cancel = new JButton("Cancel");
        btn_cancel.setFocusPainted(false);
        btn_cancel.setBounds(5, 135, 120, 40);
        add(btn_cancel);

        btn_update = new JButton("Update!");
        btn_update.setFocusPainted(false);
        btn_update.setBounds(135, 135, 120, 40);
        add(btn_update);

        btn_cancel.addActionListener(e -> back());
        listener = e -> {
            btn_cancel.setEnabled(false);
            btn_update.setEnabled(false);
            label.setText("Loading..");
            new Thread(() -> {
                if (VersionController.update(info)) {
                    label.setText("Successful! You can open " + info.full_name);
                } else {
                    label.setText("Error downloading!");
                }
                btn_cancel.setEnabled(true);
                btn_cancel.setText("Ok");
                btn_update.removeActionListener(listener);
                btn_update.setEnabled(true);
                btn_update.setText("Open");
                btn_update.addActionListener((l) -> {
                    try {
                        Runtime.getRuntime().exec("java -jar " + info.full_name);
                        System.exit(0);
                    } catch (IOException ex) {
                        label.setText("Error opening!");
                    }
                });
            }).start();
        };
        btn_update.addActionListener(listener);
    }

    @Override
    public void onSetSize() {
        setSize(280, 230);
    }
}
