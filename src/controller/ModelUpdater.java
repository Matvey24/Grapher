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
import view.grapher.graphics.Function;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Implicit;
import view.grapher.graphics.Parametric;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.Color.*;

public class ModelUpdater {
    private static final List<Color> colors = Arrays.asList(BLUE, RED, GREEN, CYAN, magenta, GRAY, ORANGE, PINK, YELLOW, LIGHT_GRAY);
    private static final List<String> func_names = Arrays.asList("f", "i", "j", "l", "m", "n", "o", "q", "r", "s");
    private static final double deltaScale = 1.2;
    private final Runnable repaint;
    private Runnable resize;

    private final ArrayCalculator<Double> calculator;
    private List<Graphic> graphics;
    private ElementsList list;
    private FunctionsView functionsView;
    private CalculatorView calculatorView;
    private final SupportFrameManager supportFrameManager;
    private final Tasks tasks;

    private double offsetX = -3;
    private double offsetY = 5.5;
    private double scaleX = 100;
    private double scaleY = 100;
    private boolean dangerState = false;

    public ModelUpdater(Runnable repaint) {
        this.repaint = repaint;
        calculator = new ArrayCalculator<>();
        supportFrameManager = new SupportFrameManager(this);
        tasks = new Tasks();
        new Thread(() -> {
            int version = VersionController.checkUpdates();
            if (version != -1) {
                supportFrameManager.openUpdaterFrame(VersionController.getName(version));
            }
        }).start();
    }

    public void addVRemove(ActionEvent e) {
        if (e.getActionCommand().equals("remove")) {
            supportFrameManager.close();
            remove(e);
        } else if (e.getActionCommand().equals("add")) {
            add(e);
        } else {
            System.out.println("error " + e.getActionCommand());
        }
    }

    private void add(ActionEvent e) {
        TextElement element = list.getElements().get(e.getID());
        Graphic graphic = new Function();
        graphics.add(graphic);

        element.addTextChangedListener((e1) -> recalculate());
        setColor(element, graphic);
        recalculate();
    }

    private void remove(ActionEvent e) {
        graphics.remove(e.getID());
        recalculate();
    }

    public void startSettings(ActionEvent e) {
        int id = e.getID();
        Graphic g = graphics.get(id);
        if (g instanceof Function) {
            supportFrameManager.openFunctionSettings((Function) g, list.getElements().get(id));
        } else if (g instanceof Parametric) {
            supportFrameManager.openParameterSettings((Parametric) g, list.getElements().get(id));
        } else if (g instanceof Implicit) {
            supportFrameManager.openImplicitSettings((Implicit) g, list.getElements().get(id));
        }
    }

    public void openTimer() {
        supportFrameManager.openTimerSettings();
    }

    public void makeFunction(Graphic g, TextElement e) {
        if (g instanceof Function)
            return;
        int idx = graphics.indexOf(g);
        Function function = new Function();
        function.setColor(e.getColor());
        graphics.set(idx, function);
        e.setColor(WHITE);
        setColor(e, function);
        recalculate();
        startSettings(new ActionEvent(0, idx, ""));
    }

    public void makeParameter(Graphic g, TextElement e) {
        if (g instanceof Parametric)
            return;
        int idx = graphics.indexOf(g);
        Parametric parametric = new Parametric();
        parametric.setColor(e.getColor());
        graphics.set(idx, parametric);
        e.setName("xy(t)");
        recalculate();
        startSettings(new ActionEvent(0, idx, ""));
    }

    public void makeImplicit(Graphic g, TextElement e) {
        if (g instanceof Implicit)
            return;
        int idx = graphics.indexOf(g);
        Implicit implicit = new Implicit();
        implicit.setColor(e.getColor());
        graphics.set(idx, implicit);
        e.setName("Imp");
        recalculate();
        startSettings(new ActionEvent(0, idx, ""));
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
                if (g instanceof Function) {
                    e.setName(func_names.get(i) + "(" + ((((Function) g).abscissa) ? "x" : "y") + ")");
                    ((Function) g).name = func_names.get(i);
                    e.setColor(c);
                    g.setColor(c);
                }
                return;
            }
        }
    }

    public void translate(int dScreenX, int dScreenY) {
        if (dangerState)
            return;
        double dOffsetX = dScreenX / scaleX;
        double dOffsetY = dScreenY / scaleY;
        offsetX -= dOffsetX;
        offsetY += dOffsetY;
        runResize();
    }

    public void resize(double delta, int x, int y, int line) {
        if (dangerState)
            return;
        double deltaX = x / scaleX;
        double deltaY = y / scaleY;
        if (line == 0) {
            scaleX /= Math.pow(deltaScale, delta);
            scaleY /= Math.pow(deltaScale, delta);
            offsetX += -x / scaleX + deltaX;
            offsetY += y / scaleY - deltaY;
        } else if (line == 1) {
            scaleX /= Math.pow(deltaScale, delta);
            offsetX += -x / scaleX + deltaX;
        } else if (line == 2) {
            scaleY /= Math.pow(deltaScale, delta);
            offsetY += y / scaleY - deltaY;
        }
        runResize();
    }

    public void resizeBack() {
        if (dangerState)
            return;
        double yc = offsetY * scaleY;
        scaleY = 1 * scaleX;
        offsetY = yc / scaleY;
        runResize();
    }

    public void recalculate() {
        tasks.clearTasks();
        tasks.runTask(() -> {
            try {
                setState(Language.CONVERTING);
                List<String> graphs = new ArrayList<>();
                List<String> calc = new ArrayList<>();
                calc.add(calculatorView.getText());
                for (int i = 0; i < list.getElements().size(); ++i) {
                    TextElement e = list.getElements().get(i);
                    if (graphics.get(i) instanceof Function) {
                        graphs.add(((Function) graphics.get(i)).name + "=" + e.getText());
                    } else if (graphics.get(i) instanceof Parametric) {
                        String text = list.getElements().get(i).getText();
                        String[] expressions = text.split(":");
                        if (expressions.length == 2) {
                            calc.add(expressions[0]);
                            calc.add(expressions[1]);
                        } else {
                            throw new RuntimeException("we need 2 funcs in parameter");
                        }
                    } else if (graphics.get(i) instanceof Implicit) {
                        String text = list.getElements().get(i).getText();
                        calc.add(text);
                    }
                }
                calculator.calculate(
                        Arrays.asList(functionsView.getText().split("\n")),
                        graphs,
                        calc,
                        new Number()
                );
                calculatorView.setAnswer(calculator.getExpressions().get(0));
                int funcs = 0;
                int parameters = 0;
                for (int i = 0; i < graphics.size(); ++i) {
                    Graphic g = graphics.get(i);
                    if (g instanceof Function) {
                        Function f = (Function) g;
                        List<Variable<Double>> vars = calculator.getVars().get(funcs);
                        TextElement el = list.getElements().get(i);
                        Variable<Double> var = vars.get(0);
                        if (var.getName().equals("y") && f.abscissa) {
                            f.abscissa = false;
                            el.setName(f.name + "(y)");
                        } else if (var.getName().equals("x") && !f.abscissa) {
                            f.abscissa = true;
                            el.setName(f.name + "(x)");
                        }
                        f.update(calculator.getGraphics().get(funcs), var);
                        ++funcs;
                    } else if (g instanceof Parametric) {
                        List<Variable<Double>> vars = calculator.getExpressionVars().get(parameters);
                        if (vars.size() == 0)
                            vars.add(new Variable<>());
                        Variable<Double> varX = vars.get(0);
                        vars = calculator.getExpressionVars().get(parameters + 1);
                        if (vars.size() == 0)
                            vars.add(new Variable<>());
                        Variable<Double> varY = vars.get(0);
                        Expression<Double> funcX = calculator.getExpressions().get(parameters + 1);
                        Expression<Double> funcY = calculator.getExpressions().get(parameters + 2);
                        g.update(funcY, varY);
                        ((Parametric) g).updateX(funcX, varX);
                        parameters += 2;
                    } else if (g instanceof Implicit) {
                        List<Variable<Double>> vars = calculator.getExpressionVars().get(parameters);
                        Expression<Double> func = calculator.getExpressions().get(parameters + 1);
                        if (vars.size() > 2)
                            throw new RuntimeException("We need x and y wars in Implicit");
                        Variable<Double> varX = null;
                        Variable<Double> varY = null;
                        checkingVars:
                        {
                            if (vars.size() == 0) {
                                varX = new Variable<>();
                                varY = new Variable<>();
                                break checkingVars;
                            } else if (vars.get(0).getName().equals("x")) {
                                varX = vars.get(0);
                            } else if (vars.get(0).getName().equals("y")) {
                                varY = vars.get(0);
                            } else {
                                throw new RuntimeException("We need x and y wars in Implicit");
                            }
                            if (vars.size() == 1 && varX == null) {
                                varX = new Variable<>();
                            } else if (vars.size() == 1) {
                                varY = new Variable<>();
                            } else if (vars.get(1).getName().equals("x")) {
                                varX = vars.get(1);
                            } else if (vars.get(1).getName().equals("y")) {
                                varY = vars.get(1);
                            } else {
                                throw new RuntimeException("We need x and y wars in Implicit");
                            }
                        }
                        g.update(func, varX);
                        ((Implicit) g).updateY(varY);
                        ++parameters;
                    }
                }
                dangerState = false;
                setTime(supportFrameManager.getTime());
                calculatorView.update();
                resize.run();
                repaint.run();
            } catch (Exception e) {
                if (e instanceof NullPointerException) {
                    setState(e.toString());
                } else
                    setState(e.getMessage());
                dangerState = true;
            }
        });
    }

    public void frameResize() {
        tasks.clearTasks();
        tasks.runTask(() -> {
            if (!dangerState) {
                try {
                    for (Graphic g : graphics) {
                        g.funcChanged();
                    }
                    calculatorView.update();
                    resize.run();
                    repaint.run();
                } catch (Throwable e) {
                    error(e.getClass().getName());
                    e.printStackTrace();
                }
            }
        });
    }

    public void runResize() {
        tasks.clearTasks();
        if (!dangerState)
            tasks.runTask(() -> {
                try {
                    resize.run();
                    repaint.run();
                } catch (Throwable t) {
                    error(t.getClass().getName());
                }
            });
    }

    public void setStringElements(FunctionsView functions, CalculatorView calculator) {
        this.functionsView = functions;
        this.calculatorView = calculator;
    }

    public void setState(String text) {
        list.setState(text);
    }

    public void setResize(Runnable resize) {
        this.resize = resize;
        runResize();
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

    public SupportFrameManager getSupportFrameManager() {
        return supportFrameManager;
    }

    public void error(String message) {
        dangerState = true;
        setState(message);
    }

    public void setTime(double time) {
        calculator.resetConstant("tm", time);
    }
}
