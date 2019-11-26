package view.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextElement {
    public static final int NAME_WIDTH = 30;
    public static final int TEXT_WIDTH = 160;
    public static final int REMOVE_WIDTH = 25;
    public static final int HEIGHT = 25;
    public static final int WIDTH = NAME_WIDTH + TEXT_WIDTH + REMOVE_WIDTH;
    private JLabel name;
    private JTextField text;
    private JButton remove;
    private ActionListener settingsListener;
    public TextElement(int x, int y, String name){
        this.name = new JLabel(name);
        text = new JTextField();
        remove = new JButton("-");
        remove.setFont(new Font("arial", Font.PLAIN, 8));
        remove.setForeground(Color.RED);
        setBounds(x,y);
    }
    public void setBounds(int x, int y){
        name.setBounds(x,y,NAME_WIDTH, HEIGHT);
        text.setBounds(x + NAME_WIDTH, y, TEXT_WIDTH, HEIGHT);
        remove.setBounds(x + NAME_WIDTH + TEXT_WIDTH, y, REMOVE_WIDTH, HEIGHT);
    }
    public String getText(){
        return text.getText();
    }
    public void addTo(Container container){
        container.add(name);
        container.add(text);
        container.add(remove);
    }
    public void removeFrom(Container c) {
        c.remove(name);
        c.remove(text);
        c.remove(remove);
    }
    public void addRemoveListener(ActionListener listener){
        remove.addActionListener(listener);
    }

    public void addSettingsListener(MouseListener settingsListener) {
        remove.addMouseListener(settingsListener);
    }

    public void addTextChangedListener(ActionListener listener){
        text.addActionListener(listener);
    }
    public void setColor(Color c){
        name.setForeground(c);
    }
    public void setName(String name) {
        this.name.setText(name);
    }
    public String getName() {
        return name.getText();
    }

    public Color getColor(){
        return name.getForeground();
    }
}
