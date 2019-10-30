package controller;

import calculator2.ArrayCalculator;
import calculator2.calculator.executors.Variable;
import calculator2.values.Number;
import threads.Tasks;
import view.elements.CalculatorView;
import view.elements.FunctionsView;
import view.elements.ElementsList;
import view.elements.TextElement;
import view.grapher.graphics.FunctionX;
import view.grapher.graphics.FunctionY;
import view.grapher.graphics.Graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.Color.*;

public class ModelUpdater {
    public static final double deltaScale = 1.2;
    private Runnable repaint;
    private Runnable resize;

    private ArrayCalculator<Double> calculator;
    private ArrayList<Graphic> graphics;
    private ElementsList list;
    private FunctionsView functionsView;
    private CalculatorView calculatorView;

    private Tasks tasks;

    private double offsetX = -3;
    private double offsetY = 6;
    private double scaleX = 100;
    private double scaleY = 100;
    private static List<Color> colors = Arrays.asList(RED, GREEN, BLUE, CYAN, magenta, ORANGE, YELLOW, GRAY, PINK, LIGHT_GRAY);
    private static List<String> func_names = Arrays.asList("f", "i", "j", "l", "m", "n", "o", "q", "r", "s");
    private boolean dangerState = false;
    public ModelUpdater(Runnable repaint) {
        this.repaint = repaint;
        calculator = new ArrayCalculator<>();
        tasks = new Tasks();
    }

    public void setGraphics(ArrayList<Graphic> graphics) {
        this.graphics = graphics;
    }

    public void setList(ElementsList list) {
        this.list = list;
    }

    public void addVRemove(ActionEvent e) {
        if (e.getActionCommand().equals("remove")) {
            graphics.remove(e.getID());
            recalculate();
        } else if (e.getActionCommand().equals("add")) {
            TextElement element = list.getElements().get(e.getID());
            Graphic graphic = new FunctionX();
            graphics.add(graphic);

            element.addTextChangedListener((e1) -> {
                recalculate();
            });
            setColor(element, graphic);
            recalculate();
        } else {
            System.out.println("error " + e.getActionCommand());
        }
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
                if(g instanceof FunctionX){
                    varName = "x";
                }else if(g instanceof FunctionY){
                    varName = "y";
                }else{
                    varName = "x";
                }
                e.setName(func_names.get(i) + "(" + varName + ")");
                e.setColor(c);
                g.setColor(c);
                return;
            }
        }
    }

    public void translate(int dScreenX, int dScreenY) {
        double dOffsetX = dScreenX / scaleX;
        double dOffsetY = dScreenY / scaleY;
        offsetX -= dOffsetX;
        offsetY += dOffsetY;
        if(!dangerState)
        tasks.runTask(() -> {
            resize.run();
            repaint.run();
        });
    }

    public void resize(int delta, int x, int y) {
        if (delta == 1) {
            double deltaX = x / scaleX;
            double deltaY = y / scaleY;
            scaleX /= deltaScale;
            scaleY /= deltaScale;
            offsetX += -x / scaleX + deltaX;
            offsetY += y / scaleY - deltaY;
        }
        if (delta == -1) {
            double deltaX = x / scaleX;
            double deltaY = y / scaleY;
            scaleX *= deltaScale;
            scaleY *= deltaScale;
            offsetX += -x / scaleX + deltaX;
            offsetY += y / scaleY - deltaY;
        }
        if(!dangerState)
        tasks.runTask(() -> {
            resize.run();
            repaint.run();
        });
    }

    public void recalculate() {
        tasks.clearTasks();
        tasks.runTask(() -> {
            setState("+");
            dangerState = false;
            try {
                List<String> graphs = new ArrayList<>();
                for (TextElement e : list.getElements()) {
                    graphs.add(e.getName().substring(0, e.getName().length() - 3) + ":1=" + e.getText());
                }
                double ans = calculator.calculate(
                        Arrays.asList(functionsView.getText().split("\n")),
                        graphs,
                        calculatorView.getText(),
                        new Number()
                );

                calculatorView.setAnswer(ans);
                for (int i = 0; i < graphics.size(); ++i) {
                    Graphic g = graphics.get(i);
                    Variable<Double> var = calculator.getVars().get(i);
                    TextElement el = list.getElements().get(i);
                    if(var.getName().equals("y") && !(g instanceof FunctionY)){
                        g = new FunctionY();
                        graphics.set(i, g);
                        g.setColor(el.getColor());
                        String t = el.getName().substring(0,1);
                        el.setName(t + "(y)");
                    }else if(var.getName().equals("x") && !(g instanceof FunctionX)){
                        g = new FunctionX();
                        graphics.set(i, g);
                        g.setColor(el.getColor());
                        String t = el.getName().substring(0,1);
                        el.setName(t + "(x)");
                    }
                    g.update(calculator.getGraphics().get(i), var);
                }
                resize.run();
                repaint.run();
            } catch (Exception e) {
                setState(e.getMessage());
                dangerState = true;
            }
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
}
