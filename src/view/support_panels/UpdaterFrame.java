package view.support_panels;

import controller.VersionController;
import framesLib.Screen;

import javax.swing.*;

public class UpdaterFrame extends Screen {
    public final JLabel label;
    public final JTextArea changes;
    public final JButton btn_cancel;
    public final JButton btn_update;
    public UpdaterFrame(VersionController.UpdateInfo info){
        setLayout(null);
        label = new JLabel("Found new version: Grapher" + info.version_name + ".jar");
        label.setBounds(5, 5, 250, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        changes = new JTextArea();
        JScrollPane pane = new JScrollPane(changes);
        pane.setBounds(5, 50, 250, 80);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        changes.setEditable(false);
        changes.setLineWrap(true);
        add(pane);

        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(5, 50, 120, 40);
        add(btn_cancel);

        btn_update = new JButton("Update!");
        btn_update.setBounds(135, 50, 120, 40);
        add(btn_update);

        btn_cancel.addActionListener(e -> back());
        btn_update.addActionListener(e-> {
            btn_cancel.setEnabled(false);
            btn_update.setEnabled(false);
            label.setText("Loading..");
            new Thread(()->{
                if(VersionController.update()){
                    label.setText("Successful! You can open Grapher" + info.version_name + ".jar");
                }else{
                    label.setText("Error downloading!");
                }
                btn_cancel.setEnabled(true);
                btn_cancel.setText("Ok");
            }).start();
        });
    }
    @Override
    public void onSetSize() {
        setSize(300,150);
    }
}
