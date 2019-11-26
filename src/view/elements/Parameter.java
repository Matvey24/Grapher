package view.elements;

import controller.ViewElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Parameter extends ViewElement {
    private JLabel label;
    private JTextField field;

    public Parameter(String name, ActionListener onChange){
        label = new JLabel(name);
        label.setFont(name_font);
        field = new JTextField();
        field.addActionListener((e)->{
            e.setSource(field.getText());
            onChange.actionPerformed(e);
        });
    }
    @Override
    public void addTo(Container container) {
        container.add(label);
        container.add(field);
    }
    public void setDefault(String text){
        field.setText(text);
    }
    public void setBounds(int x, int y, int width){
        label.setBounds(x + 10, y + 10, width, 30);
        field.setBounds(x + 10, y + 40, width, 30);
    }
}
