package view.elements;

import model.ViewElement;
import model.help.Exeptable;

import javax.swing.*;
import java.awt.*;

import static view.elements.ElementsList.OFFSET;

public class Parameter extends ViewElement {
    private final JLabel label;
    private final JTextField field;
    private Exeptable onChange;
    private String def;
    public Parameter(String name, Exeptable onChange){
        this.onChange = onChange;
        label = new JLabel(name);
        label.setFont(name_font);
        field = new JTextField();
        field.addActionListener((e)->{
            try{
                onChange.execute(field.getText());
                def = field.getText();
            }catch (Exception ex){
                field.setText(def);
            }
        });
    }
    @Override
    public void addTo(Container container) {
        container.add(label);
        container.add(field);
    }
    public void setDefault(String text){
        def = text;
        field.setText(text);
    }
    public void activate(){
        try{
            onChange.execute(field.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
