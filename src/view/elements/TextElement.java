package view.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextElement {
    private static final int NAME_WIDTH = 30;
    private static int TEXT_WIDTH;
    private static final int REMOVE_WIDTH = 25;
    public static final int HEIGHT = 25;
    public static int WIDTH = NAME_WIDTH + TEXT_WIDTH + REMOVE_WIDTH;
    private final JLabel name;
    private final JTextField text;
    private final JButton remove;
    boolean attached;
    static {
        setTextWidth(160);
    }
    public static void setTextWidth(int width){
        TEXT_WIDTH = width;
        WIDTH = NAME_WIDTH + TEXT_WIDTH + REMOVE_WIDTH;
    }
    TextElement(){
        this.name = new JLabel("");
        text = new JTextField();
        remove = new JButton("-");
        remove.setFont(new Font("arial", Font.PLAIN, 8));
        remove.setForeground(Color.RED);
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
    void removeFrom(Container c) {
        c.remove(name);
        c.remove(text);
        c.remove(remove);
    }
    void addRemoveListener(ActionListener listener){
        remove.addActionListener(listener);
    }

    void addSettingsListener(MouseListener settingsListener) {
        remove.addMouseListener(settingsListener);
    }
    public JTextField getField(){
        return text;
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
    public void setText(String text){
        this.text.setText(text);
    }
    public Color getColor(){
        return name.getForeground();
    }
}
