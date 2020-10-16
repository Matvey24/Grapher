package view.support_panels;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileDescriptor;

import framesLib.Screen;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser extends Screen {
    public static final int ALL_FILES = 0;
    public static final int GRAPHER_PROJECT = 1;
    private final JFileChooser fileChooser;
    private ActionListener listener;
    public FileChooser(){
        fileChooser = new JFileChooser();
        FileFilter ff = new FileNameExtensionFilter("Grapher project (.gr)", "gr");
        fileChooser.addChoosableFileFilter(ff);
        fileChooser.setMultiSelectionEnabled(false);
        add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }
    public void setSelectedFile(File f){
        fileChooser.setSelectedFile(f);
    }
    public void setSelectedFileFilter(int n){
        fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[n]);
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
