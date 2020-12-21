package view.elements;

import calculator2.calculator.executors.FuncVariable;
import calculator2.calculator.executors.LambdaContainer;
import calculator2.calculator.executors.actors.Expression;
import model.Language;
import model.ViewElement;
import model.help.TFunc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static view.elements.ElementsList.OFFSET;
import static view.elements.TextElement.HEIGHT;

public class CalculatorView extends ViewElement {
    public final int CALC_HEIGHT;
    private final JTextField answer;
    private final JLabel name;
    private final JTextField field;
    private Expression<Double> func;
    private final StringBuilder sb;
    private int answer_offset;
    @SuppressWarnings("unchecked")
    private final FuncVariable<Double>[] var = new FuncVariable[1];
    private int x, y;
    public CalculatorView(Runnable calculate, Runnable resize, TFunc<JTextField> func) {
        CALC_HEIGHT = 2 * HEIGHT + 2 * OFFSET;
        name = new JLabel();
        name.setFont(name_font);
        answer = new JTextField();
        answer.setEditable(false);
        answer.setText("0.0");
        answer.setFont(new Font("arial", Font.PLAIN, 11));
        field = new JTextField();
        field.addActionListener(e -> calculate.run());
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    resize.run();
                }
            }
        });
        func.execute(field);
        sb = new StringBuilder();
        var[0] = new FuncVariable<>();
        updateLanguage();
    }

    @Override
    public void addTo(Container c) {
        c.add(name);
        c.add(answer);
        c.add(field);
    }

    public void setBounds(int x, int y) {
        this.x = x;
        this.y = y;
        name.setBounds(x + OFFSET, y, TextElement.WIDTH / 2 - OFFSET / 2, HEIGHT);
        answer.setBounds(x + OFFSET + answer_offset, y, TextElement.WIDTH - answer_offset, HEIGHT);
        field.setBounds(x + OFFSET, y + HEIGHT + OFFSET, TextElement.WIDTH, HEIGHT);
    }

    public void setAnswer(Expression<Double> func) {
        this.func = func;
    }

    public void update() {
        if (func != null) {
            if (func instanceof LambdaContainer) {
                sb.setLength(0);
                var[0].setValue(-1.);
                int size = ((LambdaContainer<Double>) func).execute(var).intValue();
                for (int i = 0; i < size; ++i) {
                    if (i != 0)
                        sb.append(',');
                    var[0].setValue(i + .0);
                    double d = ((LambdaContainer<Double>) func).execute(var);
                    sb.append(d);
                }
                setAnswer(sb.toString());
            } else
                setAnswer(String.valueOf(func.calculate()));
        }
    }

    private void setAnswer(String text) {
        answer.setText(text);
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String s) {
        field.setText(s);
    }

    public void updateLanguage() {
        name.setText(Language.CALCULATOR);
        answer_offset = name.getPreferredSize().width + OFFSET;
        setBounds(x, y);
    }
}
