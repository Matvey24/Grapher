package view.elements;

import calculator2.calculator.executors.Expression;
import controller.ViewElement;

import javax.swing.*;
import java.awt.*;

import static view.elements.ElementsList.OFFSET;
import static view.elements.TextElement.HEIGHT;
import static view.elements.ElementsList.WIDTH;
public class CalculatorView extends ViewElement {
    public static final int CALC_HEIGHT = 2 * HEIGHT + 2 * OFFSET;
    private final JTextField answer;
    private final JLabel name;
    private final JTextField field;
    private Expression<Double> func;
    public CalculatorView(Runnable calculate){
        name = new JLabel("Calculator");
        name.setFont(name_font);
        answer = new JTextField();
        answer.setEditable(false);
        answer.setText("'ans'");
        answer.setFont(new Font("arial", Font.PLAIN, 11));
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
        answer.setBounds(x + WIDTH / 2 - 3*OFFSET, y, 2*OFFSET + WIDTH / 2, HEIGHT);
        field.setBounds(x + OFFSET, y + HEIGHT + OFFSET, WIDTH - 2 * OFFSET, HEIGHT);
    }
    public void setAnswer(Expression<Double> func){
        this.func = func;
    }
    public void update(){
        answer.setText(String.valueOf(func.calculate()));
    }
    public String getText() {
        return field.getText();
    }
}
