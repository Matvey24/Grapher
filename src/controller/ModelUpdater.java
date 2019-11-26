package controller;

import calculator2.ArrayCalculator;
import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.values.Number;
import threads.Tasks;
import view.elements.CalculatorView;
import view.elements.ElementsList;
import view.elements.FunctionsView;
import view.elements.TextElement;
import view.grapher.CoordinateSystem;
import view.grapher.graphics.Function;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Parameter;
import view.settings.SettingsManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.Color.*;

public class ModelUpdater {
    private static final double deltaScale = 1.2;
    private Runnable repaint;
    private Runnable resize;

    private ArrayCalculator<Double> calculator;
    private ArrayList<Graphic> graphics;
    private ElementsList list;
    private FunctionsView functionsView;
    private CalculatorView calculatorView;
    private SettingsManager settingsManager;
    private Tasks tasks;

    private double offsetX = -3;
    private double offsetY = 5.5;
    private double scaleX = 100;
    private double scaleY = 100;
    private static List<Color> colors = Arrays.asList(RED, GREEN, BLUE, CYAN, magenta, ORANGE, YELLOW, GRAY, PINK, LIGHT_GRAY);
    private static List<String> func_names = Arrays.asList("f", "i", "j", "l", "m", "n", "o", "q", "r", "s");
    private boolean dangerState = false;
    public ModelUpdater(Runnable repaint) {
        this.repaint = repaint;
        calculator = new ArrayCalculator<>();
        settingsManager = new SettingsManager(this);
        tasks = new Tasks();
    }

    public void addVRemove(ActionEvent e) {
        if (e.getActionCommand().equals("remove")) {
            settingsManager.close();
            remove(e);
        } else if (e.getActionCommand().equals("add")) {
            add(e);
        } else {
            System.out.println("error " + e.getActionCommand());
        }
    }
    private void add(ActionEvent e){
        TextElement element = list.getElements().get(e.getID());
        Graphic graphic = new Function();
        graphics.add(graphic);

        element.addTextChangedListener((e1) -> recalculate());
        setColor(element, graphic);
        recalculate();
    }
    private void remove(ActionEvent e){
        graphics.remove(e.getID());
        recalculate();
    }

    public void startSettings(ActionEvent e){
        int id = e.getID();
        Graphic g = graphics.get(id);
        if(g instanceof Function){
            settingsManager.openFunctionSettings((Function) g, list.getElements().get(id));
        }else if(g instanceof Parameter){
            settingsManager.openParameterSettings((Parameter) g, list.getElements().get(id));
        }
    }
    public void makeFunction(Graphic g, TextElement e){
        int idx = graphics.indexOf(g);
        Function function = new Function();
        function.setColor(e.getColor());
        graphics.set(idx, function);
        Color c = e.getColor();
        e.setColor(WHITE);
        setColor(e, function);
        recalculate();
        startSettings(new ActionEvent(0,idx, ""));
    }
    public void makeParameter(Graphic g, TextElement e){
        int idx = graphics.indexOf(g);
        Parameter parameter = new Parameter();
        parameter.setColor(e.getColor());
        graphics.set(idx, parameter);
        recalculate();
        e.setName("param");
        startSettings(new ActionEvent(0,idx, ""));
    }

    private void setColor(TextElement e, Graphic g) {
        for (int i = 0; i < colors.size(); ++i) {
            Color c = colors.get(i);
            boolean hasColor = false;
            for (TextElement element : list.getElements()) {
                if (element.getColor() == c) {
                    hasColor = true;
                    break;
                }
            }
            if (!hasColor) {
                String varName;
                if(g instanceof Function){
                    if (((Function)g).abscissa)
                        varName = "x";
                    else
                        varName = "y";
                    e.setName(func_names.get(i) + "(" + varName + ")");
                    e.setColor(c);
                    g.setColor(c);
                }
                return;
            }
        }
    }

    public void translate(int dScreenX, int dScreenY) {
        double dOffsetX = dScreenX / scaleX;
        double dOffsetY = dScreenY / scaleY;
        offsetX -= dOffsetX;
        offsetY += dOffsetY;
        if (!dangerState)
            tasks.runTask(() -> {
                resize.run();
                repaint.run();
            });
    }
    public void resize(double delta, int x, int y) {
        double deltaX = x / scaleX;
        double deltaY = y / scaleY;
        scaleX /= Math.pow(deltaScale, delta);
        scaleY /= Math.pow(deltaScale, delta);
        offsetX += -x / scaleX + deltaX;
        offsetY += y / scaleY - deltaY;
        runResize();
    }
    public void recalculate() {
        tasks.clearTasks();
        tasks.runTask(() -> {
            setState("+");
            dangerState = false;
            try {
                List<String> graphs = new ArrayList<>();
                List<String> calc = new ArrayList<String>();
                calc.add(calculatorView.getText());
                for (int i = 0; i < list.getElements().size(); ++i) {
                    TextElement e = list.getElements().get(i);
                    if(graphics.get(i) instanceof Function) {
                        graphs.add(e.getName().substring(0, e.getName().length() - 3) + ":1=" + e.getText());
                    }
                    else if(graphics.get(i) instanceof Parameter){
                        String text = list.getElements().get(i).getText();
                        String[] expressions = text.split(":");
                        if(expressions.length == 2){
                            calc.add(expressions[0]);
                            calc.add(expressions[1]);
                        }else{
                            throw new RuntimeException("we need 2 funcs in parameter");
                        }
                    }
                }
                double ans = calculator.calculate(
                        Arrays.asList(functionsView.getText().split("\n")),
                        graphs,
                        calc,
                        new Number()
                );

                calculatorView.setAnswer(String.valueOf(ans));
                int funcs = 0;
                int parameters = 0;
                for (int i = 0; i < graphics.size(); ++i) {
                    Graphic g = graphics.get(i);
                    if(g instanceof Function) {
                        Variable<Double> var = calculator.getVars().get(funcs);
                        TextElement el = list.getElements().get(i);
                        if (var.getName().equals("y") && ((Function) g).abscissa) {
                            ((Function) g).abscissa = false;
                            el.setName(el.getName().substring(0, el.getName().length() - 3) + "(y)");
                        } else if (var.getName().equals("x") && !((Function) g).abscissa) {
                            ((Function) g).abscissa = true;
                            el.setName(el.getName().substring(0, el.getName().length() - 3) + "(x)");
                        }
                        g.update(calculator.getGraphics().get(funcs), var);
                        ++funcs;
                    }else if(g instanceof Parameter){
                        Variable<Double> varX = calculator.getExpressionVars().get(parameters);
                        Variable<Double> varY = calculator.getExpressionVars().get(parameters + 1);
                        Expression<Double> funcX = calculator.getExpressions().get(parameters);
                        Expression<Double> funcY = calculator.getExpressions().get(parameters + 1);
                        g.update(funcY, varY);
                        ((Parameter) g).updateX(funcX, varX);
                        parameters += 2;
                    }
                }
                resize.run();
                repaint.run();
            } catch (Exception e) {
                setState(e.getMessage());
                dangerState = true;
            }
        });
    }
    private void runResize(){
        if (!dangerState)
            tasks.runTask(() -> {
                resize.run();
                repaint.run();
            });
    }
    public void setStringElements(FunctionsView functions, CalculatorView calculator) {
        this.functionsView = functions;
        this.calculatorView = calculator;
    }
    private void setState(String text) {
        list.setState(text);
    }
    public void setResize(Runnable resize) {
        this.resize = resize;
        tasks.runTask(() -> {
            resize.run();
            repaint.run();
        });
    }
    public double getOffsetX() {
        return offsetX;
    }
    public double getOffsetY() {
        return offsetY;
    }
    public double getScaleX() {
        return scaleX;
    }
    public double getScaleY() {
        return scaleY;
    }
    public void setList(ElementsList list) {
        this.list = list;
    }
    public void setGraphics(ArrayList<Graphic> graphics) {
        this.graphics = graphics;
    }
}
