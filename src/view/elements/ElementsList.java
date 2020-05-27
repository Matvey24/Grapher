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
    private final JButton btn_move_up;
    private final JButton btn_move_down;
    private boolean scrollActive;
    private int position;
    private final ArrayList<TextElement> elements;
    private int height;
    private int MAX_SIZE = 10;
    public static final int OFFSET = 5;
    private static final int NAME_HEIGHT = 30;
    public static final int WIDTH = TextElement.WIDTH + 2 * OFFSET;
    private final Point pos;
    private Container c;
    private final ActionListener sizeChanged;
    private final IntFunc settings;
    public ElementsList(int x, int y, ActionListener sizeChanged, IntFunc settings) {
        this.sizeChanged = sizeChanged;
        this.settings = settings;
        elements = new ArrayList<>();
        pos = new Point();
        this.name = new JLabel();
        this.name.setFont(name_font);
        btn_make_element = new JButton("+");
        btn_make_element.setFocusPainted(false);
        state = new JTextField();
        state.setEditable(false);
        state.setFont(new Font("arial", Font.PLAIN, 12));
        btn_make_element.addActionListener((e)->{
            addElement();
            setBounds(pos.x, pos.y);
            sizeChanged.actionPerformed(new ActionEvent(0, elements.size() - 1, "add"));
        });
        btn_move_up = new JButton("˄");
        btn_move_up.setFocusPainted(false);
        btn_move_down = new JButton("˅");
        btn_move_down.setFocusPainted(false);
        btn_move_up.addActionListener((e)->move(-1));
        btn_move_down.addActionListener((e)->move(1));
        setBounds(x,y);
    }
    public void setName(String name){
        this.name.setText(name);
    }
    public void addTo(Container c){
        c.add(name);
        c.add(state);
        c.add(btn_make_element);
        this.c = c;
    }
    public void move(int dir){
        position += dir;
        setBounds(pos.x, pos.y);
    }
    public void setBounds(int x, int y){
        pos.setLocation(x,y);
        name.setBounds(x + OFFSET,y + OFFSET, TextElement.WIDTH, NAME_HEIGHT);
        state.setBounds(x + 2 * OFFSET + TextElement.WIDTH * 2 / 5, y + OFFSET, 3*TextElement.WIDTH / 5 - OFFSET, NAME_HEIGHT);
        height = 2 * OFFSET + NAME_HEIGHT;
        {
            if(position > elements.size() - MAX_SIZE)
                position = elements.size() - MAX_SIZE;
            if(position < 0)
                position = 0;
            boolean scroll = elements.size() > MAX_SIZE;
            int size = scroll? MAX_SIZE: elements.size();
            for (int i = 0; i < elements.size(); ++i) {
                TextElement element = elements.get(i);
                if (element.attached && (i < position || i > position + MAX_SIZE - 1)) {
                    element.removeFrom(c);
                    element.attached = false;
                }
            }
            for (int i = 0; i < size; ++i) {
                TextElement e = elements.get(position + i);
                e.setBounds(x + OFFSET, y + height);
                if(!e.attached){
                    e.attached = true;
                    e.addTo(c);
                }
                height += TextElement.HEIGHT + OFFSET;
            }
            if(scroll){
                if(!scrollActive){
                    scrollActive = true;
                    c.add(btn_move_up);
                    c.add(btn_move_down);
                }
                if(position == 0 && btn_move_up.isEnabled()){
                    btn_move_up.setEnabled(false);
                }else if(position != 0 && !btn_move_up.isEnabled()){
                    btn_move_up.setEnabled(true);
                }
                if(position == elements.size() - MAX_SIZE && btn_move_down.isEnabled()){
                    btn_move_down.setEnabled(false);
                }else if(position != elements.size() - MAX_SIZE && !btn_move_down.isEnabled()){
                    btn_move_down.setEnabled(true);
                }
                int width = (TextElement.WIDTH >> 1) - (OFFSET >> 1);
                btn_move_up.setBounds(x + OFFSET, height + y,
                        width - (OFFSET >> 1), TextElement.HEIGHT >> 1);
                btn_move_down.setBounds(x + OFFSET, height + y + (TextElement.HEIGHT >> 1),
                        width - (OFFSET >> 1), TextElement.HEIGHT >> 1);
                btn_make_element.setBounds(x + width + 2*OFFSET, height + y,
                        width, TextElement.HEIGHT);
            }else {
                if(scrollActive){
                    scrollActive = false;
                    c.remove(btn_move_up);
                    c.remove(btn_move_down);
                }
                btn_make_element.setBounds(x + OFFSET, height + y, TextElement.WIDTH, TextElement.HEIGHT);
            }
            height += TextElement.HEIGHT + OFFSET;
        }
    }
    public void clear(){
        for(int i = elements.size() - 1; i >= 0; --i){
            TextElement e = elements.remove(i);
            if(e.attached)
                e.removeFrom(c);
            sizeChanged.actionPerformed(new ActionEvent("", i, "remove"));
        }
        setBounds(pos.x, pos.y);
    }
    public void addElement(){
        TextElement e = new TextElement();
        e.addRemoveListener((e2)->{
            int id = elements.indexOf(e);
            elements.remove(id);
            if(e.attached)
                e.removeFrom(c);
            setBounds(pos.x, pos.y);
            sizeChanged.actionPerformed(new ActionEvent(0, id, "remove"));
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
        e.attached = true;
        elements.add(e);
        position = elements.size() - MAX_SIZE;
    }
    public int getHeight() {
        return height;
    }

    public ArrayList<TextElement> getElements(){
        return elements;
    }
    public void updateGUI(){
        setBounds(pos.x, pos.y);
    }
    public void setState(String text){
        this.state.setText(text);
    }
    public void setMAX_SIZE(int max_size){
        this.MAX_SIZE = max_size;
        setBounds(pos.x, pos.y);
    }
    public int getMAX_SIZE() {
        return MAX_SIZE;
    }
}