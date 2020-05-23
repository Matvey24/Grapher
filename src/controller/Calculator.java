package controller;

import calculator2.ArrayCalculator;
import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.values.Number;
import model.GraphType;
import model.Language;
import model.help.FullModel;
import threads.Tasks;
import view.elements.CalculatorView;
import view.elements.FunctionsView;
import view.elements.TextElement;
import view.grapher.graphics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static model.Language.UPDATER_ERRORS;

public class Calculator {
    private final ModelUpdater updater;
    private final ArrayCalculator<Double> calculator;
    private final Runnable repaint;
    private Runnable resize;
    private FunctionsView functionsView;
    private CalculatorView calculatorView;
    private final Tasks tasks;
    public Calculator(ModelUpdater updater, Runnable repaint) {
        this.updater = updater;
        this.repaint = repaint;
        tasks = new Tasks();
        calculator = new ArrayCalculator<>(makeParams());
    }

    public void recalculate() {
        tasks.clearTasks();
        tasks.runTask(() -> {
            try {
                updater.setState(Language.CONVERTING);
                List<String> graphs = new ArrayList<>();
                List<String> calc = new ArrayList<>();
                calc.add(calculatorView.getText());
                for (int i = 0; i < updater.list.getElements().size(); ++i) {
                    TextElement e = updater.list.getElements().get(i);
                    if (e.getText().contains(":")) {
                        updater.graphics.get(i).type = GraphType.DOUBLE_FUNC;
                        String text = updater.list.getElements().get(i).getText();
                        String[] expressions = text.split(":");
                        if (expressions.length == 2) {
                            calc.add(expressions[0]);
                            calc.add(expressions[1]);
                        } else {
                            throw new RuntimeException(UPDATER_ERRORS[0]);
                        }
                    } else {
                        updater.graphics.get(i).type = GraphType.SINGLE_FUNC;
                        graphs.add(updater.graphics.get(i).name + "=" + e.getText());
                    }
                }
                Number n = new Number();
                calculator.calculate(
                        Arrays.asList(functionsView.getText().split("\n")),
                        graphs,
                        calc,
                        n
                );
                calculatorView.setAnswer(calculator.getExpressions().get(0));
                int funcs = 0;
                int parameters = 0;
                for (int i = 0; i < updater.graphics.size(); ++i) {
                    Graphic g = updater.graphics.get(i);
                    if (g.type == GraphType.DOUBLE_FUNC) {
                        List<Variable<Double>> varsA = calculator.getExpressionVars().get(parameters);
                        List<Variable<Double>> varsB = calculator.getExpressionVars().get(parameters+1);
                        int ax = checkFor("x", varsA);
                        int ay = checkFor("y", varsA);
                        int bx = checkFor("x", varsB);
                        int by = checkFor("y", varsB);
                        if(varsA.size() > 1 || varsB.size() > 1
                                || ax != -1 || ay != -1 || bx != -1 || by != -1){
                            if(varsA.size() > 2 || varsB.size() > 2 || notContainsOnly(varsA, "x", "y")
                                    || notContainsOnly(varsB, "x", "y"))
                                throw new RuntimeException(UPDATER_ERRORS[3] + " " + i + " " + UPDATER_ERRORS[2]);
                            Variable<Double> varAX, varAY, varBX, varBY;
                            varAX = (ax != -1)?varsA.get(ax):new Variable<>();
                            varAY = (ay != -1)?varsA.get(ay):new Variable<>();
                            varBX = (bx != -1)?varsB.get(bx):new Variable<>();
                            varBY = (by != -1)?varsB.get(by):new Variable<>();
                            if(!(g instanceof Translation)){
                                updater.makeTranslation(i, updater.list.getElements().get(i));
                                g = updater.graphics.get(i);
                            }
                            Expression<Double> funcX = calculator.getExpressions().get(parameters + 1);
                            Expression<Double> funcY = calculator.getExpressions().get(parameters + 2);
                            g.update(funcX, varAX);
                            ((Translation)g).updateY(funcY, varAY, varBX, varBY);
                        }else {
                            if (varsA.size() == 0)
                                varsA.add(new Variable<>());
                            if (varsB.size() == 0)
                                varsB.add(new Variable<>());
                            Variable<Double> varX = varsA.get(0);
                            Variable<Double> varY = varsB.get(0);
                            Expression<Double> funcX = calculator.getExpressions().get(parameters + 1);
                            Expression<Double> funcY = calculator.getExpressions().get(parameters + 2);
                            if (!(g instanceof Parametric)) {
                                updater.makeParametric(i, updater.list.getElements().get(i));
                                g = updater.graphics.get(i);
                            }
                            g.update(funcY, varY);
                            ((Parametric) g).updateX(funcX, varX);
                        }
                        parameters += 2;
                    } else if(g.type == GraphType.SINGLE_FUNC){
                        List<Variable<Double>> vars = calculator.getVars().get(funcs);
                        if (vars.size() >= 2) {
                            if (!(g instanceof Implicit)) {
                                updater.makeImplicit(i, updater.list.getElements().get(i));
                                g = updater.graphics.get(i);
                            }
                            Expression<Double> func = calculator.getGraphics().get(funcs);
                            if (vars.size() > 2)
                                throw new RuntimeException(UPDATER_ERRORS[3] + " " + Language.TYPE_TITLES[2]);
                            Variable<Double> varX = null;
                            Variable<Double> varY = null;
                            {
                                if (vars.get(0).getName().equals("x")) {
                                    varX = vars.get(0);
                                } else if (vars.get(0).getName().equals("y")) {
                                    varY = vars.get(0);
                                } else {
                                    throw new RuntimeException(UPDATER_ERRORS[3]);
                                }
                                if (vars.get(1).getName().equals("x")) {
                                    varX = vars.get(1);
                                } else if (vars.get(1).getName().equals("y")) {
                                    varY = vars.get(1);
                                } else {
                                    throw new RuntimeException(UPDATER_ERRORS[3]);
                                }
                            }
                            g.update(func, varX);
                            ((Implicit) g).updateY(varY);
                        } else {
                            if (!(g instanceof Function)) {
                                updater.makeFunction(i, updater.list.getElements().get(i));
                                g = updater.graphics.get(i);
                            }
                            Function f = (Function) g;
                            if (vars.size() == 0) {
                                Variable<Double> var = new Variable<>();
                                var.setName((f.abscissa) ? "x" : "y");
                                vars.add(var);
                            }
                            TextElement el = updater.list.getElements().get(i);
                            Variable<Double> var = vars.get(0);
                            if (var.getName().equals("y") && f.abscissa) {
                                f.abscissa = false;
                                el.setName(f.name + "(y)");
                            } else if (var.getName().equals("x") && !f.abscissa) {
                                f.abscissa = true;
                                el.setName(f.name + "(x)");
                            }
                            f.update(calculator.getGraphics().get(funcs), var);
                        }
                        ++funcs;
                    }
                }
                updater.dangerState = false;
                resetConstant("tm", updater.getSupportFrameManager().getTime());
                calculatorView.update();
                resize.run();
                repaint.run();
            } catch (Throwable e) {
                if (e instanceof NullPointerException) {
                    updater.error(e.toString());
                    e.printStackTrace();
                } else if(e instanceof RuntimeException){
                    updater.error(e.getMessage());
                }else
                    updater.error(e.toString());
            }
        });
    }
    private int checkFor(String name, List<Variable<Double>> vars){
        for(int i = 0; i < vars.size(); ++i)
            if(name.equals(vars.get(i).getName()))
                return i;
        return -1;
    }
    private boolean notContainsOnly(List<Variable<Double>> vars, String... names){
        int num = 0;
        for (String name : names)
            if (checkFor(name, vars) != -1)
                ++num;
        return num != vars.size();
    }
    public void runResize() {
        tasks.clearTasks();
        if (!updater.dangerState)
            tasks.runTask(() -> {
                try {
                    resize.run();
                    repaint.run();
                } catch (Throwable t) {
                    updater.error(t.getClass().getName());
                }
            });
    }

    public void timerResize() {
        tasks.clearTasks();
        tasks.runTask(() -> {
            if (!updater.dangerState) {
                try {
                    for(int i = 0; i < calculator.getConsts().size(); ++i)
                        calculator.getConsts().get(i).run();
                    for (Graphic g : updater.graphics)
                        g.timeChanged();
                    calculatorView.update();
                    resize.run();
                    repaint.run();
                } catch (Throwable e) {
                    updater.error(e.getClass().getName());
                    e.printStackTrace();
                }
            }
        });
    }

    public void setResize(Runnable resize) {
        this.resize = resize;
        runResize();
    }

    public void resetConstant(String name, double time) {
        calculator.resetConstant(name, time);
    }

    public void setElements(CalculatorView calculatorView, FunctionsView functionsView) {
        this.calculatorView = calculatorView;
        this.functionsView = functionsView;
    }

    void run(Runnable r) {
        tasks.runTask(r);
    }
    private ArrayList<Variable<Double>> makeParams(){
        ArrayList<Variable<Double>> params = new ArrayList<>();
        Variable<Double> lookX = new Variable<Double>("lookX", null){
            @Override
            public void setValue(Double value) {
                super.setValue(value);
                updater.lookAtX(value);
            }
        };
        params.add(lookX);
        Variable<Double> lookY = new Variable<Double>("lookY", null){
            @Override
            public void setValue(Double value) {
                super.setValue(value);
                updater.lookAtY(value);
            }
        };
        params.add(lookY);
        return params;
    }
    void makeModel(FullModel m) {
        List<String> graphs = new ArrayList<>();
        List<String> graphicsInfo = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < updater.graphics.size(); ++i) {
            TextElement view = updater.list.getElements().get(i);
            graphs.add(view.getText());
            Graphic graphic = updater.graphics.get(i);
            sb.append(graphic.name);
            sb.append('\n');
            sb.append(graphic.MAP_SIZE);
            sb.append('\n');
            sb.append(graphic.feelsTime);
            sb.append('\n');
            if (graphic instanceof Function) {
                sb.append("Function");
            } else if (graphic instanceof Parametric) {
                sb.append("Parametric\n");
                sb.append(((Parametric) graphic).getStartT());
                sb.append(':');
                sb.append(((Parametric) graphic).getEndT());
            } else if (graphic instanceof Implicit) {
                sb.append("Implicit\n");
                sb.append(((Implicit) graphic).getSensitivity());
                sb.append("\n");
                sb.append(((Implicit) graphic).viewType);
            } else if(graphic instanceof Translation){
                sb.append("Translation\n");
                sb.append(((Translation) graphic).getMultiplyer());
            }
            graphicsInfo.add(sb.toString());
            sb.setLength(0);
        }
        m.graphics = graphs;
        m.graphics_info = graphicsInfo;
        m.functions = this.functionsView.getText();
        m.calculator = this.calculatorView.getText();
    }

    public void fromModel(FullModel m) {
        for (int i = 0; i < m.graphics.size(); ++i) {
            String graphic = m.graphics.get(i);
            String params = m.graphics_info.get(i);
            if (params.equals(""))
                continue;
            updater.add(graphic, params);
        }
        calculatorView.setText(m.calculator);
        functionsView.setText(m.functions);
    }
}
