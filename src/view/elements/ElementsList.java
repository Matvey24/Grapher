package view.elements;

import controller.ViewElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ElementsList extends ViewElement {
    private JLabel name;
    private JButton btn_make_element;
    private ArrayList<TextElement> elements;
    private int height;
    private int max_size = 10;
    public static final int OFFSET = 5;
    public static final int NAME_HEIGHT = 30;
    public static final int WIDTH = TextElement.WIDTH + 2 * OFFSET;
    private Point pos;
    private Container c;
    public ElementsList(String name, int x, int y, ActionListener sizeChanged) {
        elements = new ArrayList<>();
        pos = new Point();
        this.name = new JLabel(name);
        this.name.setFont(name_font);
        btn_make_element = new JButton("+");
        setBounds(x,y);
        btn_make_element.addActionListener((e)->{
            int y2 = height - OFFSET - TextElement.HEIGHT;
            TextElement el = new TextElement(x + OFFSET, y2, "");
            el.addRemoveListener((e2)->{
                int id = elements.indexOf(el);
                elements.remove(id);
                el.removeFrom(c);
                setBounds(pos.x, pos.y);
                sizeChanged.actionPerformed(new ActionEvent(0, id, "remove"));
                if(elements.size() == max_size - 1){
                    c.add(btn_make_element);
                }
            });
            int id = elements.size();
            elements.add(el);
            el.addTo(c);
            setBounds(pos.x, pos.y);
            sizeChanged.actionPerformed(new ActionEvent(0, id, "add"));
            if(elements.size() == max_size){
                c.remove(btn_make_element);
            }
        });
    }
    public void addTo(Container c){
        c.add(name);
        for(TextElement e: elements){
            e.addTo(c);
        }
        c.add(btn_make_element);
        this.c = c;
    }
    public void setBounds(int x, int y){
        pos.setLocation(x,y);
        name.setBounds(x + OFFSET,y + OFFSET, TextElement.WIDTH, NAME_HEIGHT);
        height = 2 * OFFSET + NAME_HEIGHT;
        for(TextElement e: elements){
            e.setBounds(x + OFFSET, y + height);
            height += TextElement.HEIGHT + OFFSET;
        }
        btn_make_element.setBounds(x + OFFSET, height + y, TextElement.WIDTH, TextElement.HEIGHT);
        height += OFFSET + TextElement.HEIGHT;
    }
    public ArrayList<TextElement> getElements(){
        return elements;
    }
    public int getHeight(){
        return height;
    }
}
