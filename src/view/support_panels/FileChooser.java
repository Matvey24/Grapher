package view.support_panels;

import java.io.File;
import framesLib.Screen;

import javax.swing.*;

public class FileChooser extends Screen {
    private JFileChooser fileChooser;
    public FileChooser(MainSettings mainSettings){
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        add(fileChooser);
        fileChooser.addActionListener((e)->{
            if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
                back();
            if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                File f = fileChooser.getSelectedFile();
                mainSettings.save(f);
                System.out.println(f);
                back();
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    }
    @Override
    public void onSetSize() {
        setSize(600,400);
    }
}
