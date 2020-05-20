package view.support_panels;

import java.awt.event.ActionListener;
import java.io.File;
import framesLib.Screen;

import javax.swing.*;

public class FileChooser extends Screen {
    private final JFileChooser fileChooser;
    private ActionListener listener;
    public FileChooser(){
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }
    public void setSelectedFile(File f){
        fileChooser.setSelectedFile(f);
    }
    public File getSelectedFile(){
        return fileChooser.getSelectedFile();
    }
    public void setActionListener(ActionListener listener){
        if(this.listener != null)
            fileChooser.removeActionListener(this.listener);
        fileChooser.addActionListener(listener);
        this.listener = listener;
    }
    @Override
    public void onSetSize() {
        setSize(600,400);
    }
}
