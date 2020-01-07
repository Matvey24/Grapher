package calculator2;

import calculator2.calculator.Director;
import calculator2.calculator.Element;
import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.values.util.AbstractType;
import calculator2.values.util.actions.AbstractFunc;

import java.util.*;

public class ArrayCalculator<T> {
    private Director<T> director;
    private AbstractType<T> type;
    private List<AbstractFunc<T>> funcs;

    private List<Expression<T>> graphics;
    private List<List<Variable<T>>> vars;


    private List<Expression<T>> expressions;
    private List<List<Variable<T>>> expressionVars;


    private List<Stack<Element>> funcNames;

    public ArrayCalculator() {
        vars = new ArrayList<>();
        funcNames = new ArrayList<>();
        funcs = new ArrayList<>();
        graphics = new ArrayList<>();
        expressions = new ArrayList<>();
        expressionVars = new ArrayList<>();
        director = new Director<>();
    }

    public void calculate(List<String> funcs, List<String> graphics, List<String> calc, AbstractType<T> type) {
        this.type = type;
        director.renewType(type);
        funcNames.clear();
        this.funcs.clear();
        this.graphics.clear();
        expressions.clear();
        for (int i = 0; i < funcs.size(); ++i) {
            funcs.set(i, funcs.get(i).replaceAll("[ \t]", ""));
            int n = funcs.get(i).indexOf("=");
            if(n == -1)
                continue;
            type.addFuncName(funcs.get(i).substring(0, n));
        }
        for (int i = 0; i < graphics.size(); ++i) {
            graphics.set(i, graphics.get(i).replaceAll("[ \t]", ""));
            type.addFuncName(graphics.get(i).substring(0, graphics.get(i).indexOf("=")));
        }
        for (String s: graphics) {
            int n = s.indexOf('=');
            analise(s.substring(0, n), s.substring(n + 1), true);
        }
        for (String s : funcs) {
            int n = s.indexOf('=');
            if (n != -1)
                analise(s.substring(0, n), s.substring(n + 1), false);
        }
        director.renewType(type);
        for (int i = 0; i < this.funcs.size(); ++i) {
            try {
                director.update(funcNames.get(i));
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage() + " in " + (i + 1) + " func grammar");
            }
            if ((director.getVars().size() == 0) && i < graphics.size()) {
                Variable<T> var = new Variable<>();
                var.setName("x");
                director.getVars().add(var);
            }
            try {
                this.funcs.get(i).setFunc(director.getTree(), director.getVars());
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage() + " in " + (i + 1) + " func vars");
            }
            if (i < graphics.size()) {
                this.graphics.add(director.getTree());
                if (this.vars.size() == i)
                    this.vars.add(new ArrayList<>());
                this.vars.get(i).clear();
                this.vars.get(i).addAll(director.getVars());
            }
        }
        try {
            director.parse(calc.get(0));
            director.update(director.getStack());
            expressions.add(director.getTree());
            for (int i = 1; i < calc.size(); ++i) {
                director.parse(calc.get(i));
                director.update(director.getStack());
                expressions.add(director.getTree());
                if (expressionVars.size() == i - 1)
                    expressionVars.add(new ArrayList<>());
                expressionVars.get(i - 1).clear();
                if (director.getVars().size() == 0) {
                    expressionVars.get(i - 1).add(new Variable<>());
                } else
                    expressionVars.get(i - 1).addAll(director.getVars());
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage() + " in calc");
        }
    }

    private void analise(String start, String end, boolean needVar) {
        AbstractFunc<T> func = new AbstractFunc<>();
        int args = director.parse(end);
        if (needVar) {
            type.addFunction(start, func.getUnary(), 10);
        } else {
            switch (args) {
                case 2:
                    type.addFunction(start, func.getBinary(), 10);
                    break;
                case 1:
                    type.addFunction(start, func.getUnary(), 10);
                    break;
                default:
                    type.addFunction(start, func.getMulti(args), args, 10);
            }
        }
        funcs.add(func);
        funcNames.add(director.getStack());
    }

    public List<Expression<T>> getGraphics() {
        return graphics;
    }

    public List<List<Variable<T>>> getVars() {
        return vars;
    }

    public List<Expression<T>> getExpressions() {
        return expressions;
    }

    public List<List<Variable<T>>> getExpressionVars() {
        return expressionVars;
    }

    public void resetConstant(String name, T val) {
        Variable<T> var = director.getHelper().consts.getConst(name);
        if (var != null)
            var.setValue(val);
    }
}
