package view.elements;
import controller.Language;
import controller.ViewElement;

import javax.swing.*;

import java.awt.*;

import static view.elements.ElementsList.OFFSET;
import static view.elements.TextElement.HEIGHT;
import static view.elements.ElementsList.WIDTH;
public class FunctionsView extends ViewElement {
    private static final int AREA_HEIGHT = 150;
    public static final int FUNC_HEIGHT = AREA_HEIGHT + HEIGHT + OFFSET;
    private final JLabel name;
    private final JTextArea area;
    private final JButton btn_update;
    private final JScrollPane scrollPane;
    public FunctionsView(Runnable onUpdate){
        area = new JTextArea();
        area.setLineWrap(true);
        scrollPane = new JScrollPane(area);
        name = new JLabel(Language.FUNCTIONS);
        name.setFont(name_font);
        btn_update = new JButton(Language.UPDATE);
        btn_update.addActionListener(e -> onUpdate.run());
    }
    @Override
    public void addTo(Container c){
        c.add(scrollPane);
        c.add(name);
        c.add(btn_update);
    }
    public String getText(){
        return area.getText();
    }
    public void setBounds(int x, int y){
        name.setBounds(x + OFFSET,y,WIDTH / 2 - OFFSET, HEIGHT);
        btn_update.setBounds(x + WIDTH / 2 + OFFSET, y, WIDTH / 2 - 2 * OFFSET, HEIGHT);
        scrollPane.setBounds(x + OFFSET,y + HEIGHT + OFFSET, WIDTH - 2 * OFFSET, AREA_HEIGHT);
    }
}
