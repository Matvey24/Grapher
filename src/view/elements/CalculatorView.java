package view.elements;

import calculator2.calculator.executors.FuncVariable;
import calculator2.calculator.executors.LambdaInitializer;
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
import static view.elements.ElementsList.WIDTH;
public class CalculatorView extends ViewElement {
    public static final int CALC_HEIGHT = 2 * HEIGHT + 2 * OFFSET;
    private final JTextField answer;
    private final JLabel name;
    private final JTextField field;
    private Expression<Double> func;
    private final StringBuilder sb;
    @SuppressWarnings("unchecked")
    private final FuncVariable<Double>[] var = new FuncVariable[1];
    public CalculatorView(Runnable calculate, Runnable resize, TFunc<JTextField> func){
        name = new JLabel();
        name.setFont(name_font);
        answer = new JTextField();
        answer.setEditable(false);
        answer.setText("0");
        answer.setFont(new Font("arial", Font.PLAIN, 11));
        field = new JTextField();
        field.addActionListener(e->calculate.run());
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3){
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
        if(func != null) {
            if(func instanceof LambdaInitializer){
                sb.setLength(0);
                var[0].setValue(-1.);
                int size = ((LambdaInitializer<Double>) func).execute(var).intValue();
                for(int i = 0; i < size; ++i){
                    if(i != 0)
                        sb.append(',');
                    var[0].setValue(i + .0);
                    double d = ((LambdaInitializer<Double>) func).execute(var);
                    sb.append(d);
                }
                answer.setText(sb.toString());
            }else
                answer.setText(String.valueOf(func.calculate()));
        }
    }
    public String getText() {
        return field.getText();
    }
    public void setText(String s){
       field.setText(s);
    }
    public void updateLanguage(){
        name.setText(Language.CALCULATOR);
    }
}
