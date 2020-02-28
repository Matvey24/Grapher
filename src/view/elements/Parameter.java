package view.elements;

import model.ViewElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetTime;

import static view.elements.ElementsList.OFFSET;

public class Parameter extends ViewElement {
    private final JLabel label;
    private final JTextField field;
    private ActionListener onChange;
    public Parameter(String name, ActionListener onChange){
        this.onChange = onChange;
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
    public void activate(){
        ActionEvent e = new ActionEvent(field.getText(), 0, "");
        onChange.actionPerformed(e);
    }
    public void setBounds(int x, int y, int width){
        label.setBounds(x, y, width, TextElement.HEIGHT);
        field.setBounds(x, y + TextElement.HEIGHT + OFFSET, width, TextElement.HEIGHT);
    }

    public void setName(String name){
        label.setText(name);
    }
    public String getText(){
        return field.getText();
    }
}
