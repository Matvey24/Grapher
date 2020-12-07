package view.support_panels;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import framesLib.Screen;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import static view.elements.ElementsList.OFFSET;

public class FileChooser extends Screen {
    public static final int ALL_FILES = 0;
    public static final int GRAPHER_PROJECT = 1;
    private final JFileChooser fileChooser;
    private ActionListener listener;
    private String title = "";
    private int width = 500+2*OFFSET;
    private int height = 350+2*OFFSET;

    public FileChooser(){
        setLayout(null);
        fileChooser = new JFileChooser();
        FileFilter ff = new FileNameExtensionFilter("Grapher project (.gr)", "gr");
        fileChooser.addChoosableFileFilter(ff);
        fileChooser.setMultiSelectionEnabled(false);
        add(fileChooser);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        registerKeyboardAction((e)->back(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = getWidth();
                height = getHeight();
                fileChooser.setBounds(OFFSET, OFFSET, width-2*OFFSET, height-2*OFFSET);
            }
        });
    }
    public void setCurrTitle(String title) {
        this.title = title;
        setTitle(title);
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
    public void onShow() {
        setTitle(title);
    }

    @Override
    public void onSetSize() {
        setSize(width,height);
    }
}
