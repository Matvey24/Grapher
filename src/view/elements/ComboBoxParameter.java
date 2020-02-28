package view.elements;

import model.ViewElement;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;

import static view.elements.ElementsList.OFFSET;

public class ComboBoxParameter extends ViewElement {
    public static final int HEIGHT = TextElement.HEIGHT * 2 + OFFSET;
    public static final int WIDTH = TextElement.WIDTH;
    private final JLabel name;
    private final JComboBox<Item> items;

    public ComboBoxParameter(String name, String... items){
        this.name = new JLabel(name);
        this.name.setFont(name_font);

        this.items = new JComboBox<>();
        for (String item : items) {
            this.items.addItem(new Item(item));
        }
    }
    public ComboBoxParameter(String name, List<String> items){
        this.name = new JLabel(name);
        this.name.setFont(name_font);
        this.items = new JComboBox<>();
        for(String item: items)
            this.items.addItem(new Item(item));
    }
    @Override
    public void addTo(Container container) {
        container.add(name);
        container.add(items);
    }
    public void setBounds(int x, int y){
        name.setBounds(x, y, WIDTH, TextElement.HEIGHT);
        items.setBounds(x, y + TextElement.HEIGHT + OFFSET, WIDTH, TextElement.HEIGHT);
    }
    public void addItemListener(ItemListener listener){
        items.addItemListener(listener);
    }
    public void setSelectedIndex(int selected){
        items.setSelectedIndex(selected);
    }
    public void setName(String name){
        this.name.setText(name);
    }
    public void setElementNames(String[] el){
        for(int i = 0; i < items.getItemCount(); ++i){
            Item it = items.getItemAt(i);
            it.name = el[i];
        }
        items.updateUI();
    }
}
