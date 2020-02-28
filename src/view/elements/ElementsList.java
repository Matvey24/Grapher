package view.elements;

import model.help.IntFunc;
import model.ViewElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ElementsList extends ViewElement {
    private final JLabel name;
    private final JTextField state;
    private final JButton btn_make_element;
    private final ArrayList<TextElement> elements;
    private int height;
    private static final int MAX_SIZE = 10;
    public static final int OFFSET = 5;
    private static final int NAME_HEIGHT = 30;
    public static final int WIDTH = TextElement.WIDTH + 2 * OFFSET;
    private final Point pos;
    private Container c;
    private ActionListener sizeChanged;
    private IntFunc settings;
    public ElementsList(int x, int y, ActionListener sizeChanged, IntFunc settings) {
        this.sizeChanged = sizeChanged;
        this.settings = settings;
        elements = new ArrayList<>();
        pos = new Point();
        this.name = new JLabel();
        this.name.setFont(name_font);
        btn_make_element = new JButton("+");
        state = new JTextField();
        state.setEditable(false);
        state.setFont(new Font("arial", Font.PLAIN, 12));
        setBounds(x,y);
        btn_make_element.addActionListener((e)->{
            addElement();
            sizeChanged.actionPerformed(new ActionEvent(0, elements.size() - 1, "add"));
        });
    }
    public void setName(String name){
        this.name.setText(name);
    }
    public void addTo(Container c){
        c.add(name);
        c.add(state);
        for(TextElement e: elements)
            e.addTo(c);
        c.add(btn_make_element);
        this.c = c;
    }
    public void setBounds(int x, int y){
        pos.setLocation(x,y);
        name.setBounds(x + OFFSET,y + OFFSET, TextElement.WIDTH, NAME_HEIGHT);
        state.setBounds(x + 2 * OFFSET + TextElement.WIDTH * 2 / 5, y + OFFSET, 3*TextElement.WIDTH / 5 - OFFSET, NAME_HEIGHT);
        height = 2 * OFFSET + NAME_HEIGHT;
        for(TextElement e: elements){
            e.setBounds(x + OFFSET, y + height);
            height += TextElement.HEIGHT + OFFSET;
        }
        btn_make_element.setBounds(x + OFFSET, height + y, TextElement.WIDTH, TextElement.HEIGHT);
        if(elements.size() != MAX_SIZE)
            height += OFFSET + TextElement.HEIGHT;
    }
    public void clear(){
        for(int i = elements.size() - 1; i >= 0; --i){
            TextElement e = elements.remove(i);
            e.removeFrom(c);
            sizeChanged.actionPerformed(new ActionEvent("", i, "remove"));
            if(elements.size() == MAX_SIZE - 1){
                c.add(btn_make_element);
            }
        }
        setBounds(pos.x, pos.y);
    }
    public void addElement(){
        TextElement e = new TextElement();
        e.addRemoveListener((e2)->{
            int id = elements.indexOf(e);
            elements.remove(id);
            e.removeFrom(c);
            setBounds(pos.x, pos.y);
            sizeChanged.actionPerformed(new ActionEvent(0, id, "remove"));
            if(elements.size() == MAX_SIZE - 1){
                c.add(btn_make_element);
            }
        });
        e.addSettingsListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent el) {
                if(el.getButton() == MouseEvent.BUTTON3){
                    settings.execute(elements.indexOf(e));
                }
            }
        });
        e.addTo(c);
        elements.add(e);
        setBounds(pos.x, pos.y);
        if(elements.size() == MAX_SIZE){
            c.remove(btn_make_element);
        }
    }
    public int getHeight() {
        return height;
    }

    public ArrayList<TextElement> getElements(){
        return elements;
    }

    public void setState(String text){
        this.state.setText(text);
    }
}
