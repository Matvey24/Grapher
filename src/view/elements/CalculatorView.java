package view.elements;

import controller.ViewElement;
import view.elements.TextElement;

import javax.swing.*;
import java.awt.*;

import static view.elements.ElementsList.OFFSET;
import static view.elements.TextElement.HEIGHT;
import static view.elements.ElementsList.WIDTH;
public class CalculatorView extends ViewElement {
    public static final int CALC_HEIGHT = 2 * HEIGHT + 2 * OFFSET;
    private JLabel answer;
    private JLabel name;
    private JTextField field;
    public CalculatorView(Runnable calculate){
        name = new JLabel("Calculator");
        name.setFont(name_font);
        answer = new JLabel("\'ans\'");
        field = new JTextField();
        field.addActionListener(e->calculate.run());
    }
    @Override
    public void addTo(Container c){
        c.add(name);
        c.add(answer);
        c.add(field);
    }
    public void setBounds(int x, int y){
        name.setBounds(x + OFFSET,y,WIDTH / 2 - 2 * OFFSET, HEIGHT);
        answer.setBounds(x + WIDTH / 2, y, WIDTH / 2, HEIGHT);
        field.setBounds(x + OFFSET, y + HEIGHT + OFFSET, WIDTH - 2 * OFFSET, HEIGHT);
    }
    public void setAnswer(String ans){
        answer.setText(ans);
    }
    public String getText() {
        return field.getText();
    }
}
