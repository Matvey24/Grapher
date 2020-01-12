package view.settings;

import controller.VersionController;
import framesLib.Screen;

import javax.swing.*;

public class UpdaterFrame extends Screen {
    public final JLabel label;
    public final JButton btn_cancel;
    public final JButton btn_update;
    public UpdaterFrame(String version){
        setLayout(null);
        label = new JLabel("Found new version: Grapher" + version + ".jar");
        label.setBounds(5, 5, 250, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
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
                    label.setText("Successful! You can open Grapher" + version + ".jar");
                }else{
                    label.setText("Error downloading!");
                }
                btn_cancel.setEnabled(true);
            }).start();
        });
    }
    @Override
    public void onSetSize() {
        setSize(300,150);
    }
}
