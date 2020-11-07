package calculator2;

import calculator2.calculator.CalcLanguage;
import calculator2.calculator.Director;
import calculator2.calculator.Element;
import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.executors.FuncVariable;
import calculator2.calculator.executors.Variable;
import calculator2.calculator.util.AbstractType;
import calculator2.calculator.util.actions.AbstractConst;
import calculator2.calculator.util.actions.AbstractFunc;

import java.util.*;

public class ArrayCalculator<T> {
    private final Director<T> director;
    private AbstractType<T> type;
    private final List<AbstractFunc<T>> funcs;
    private final List<AbstractConst<T>> consts;
    private final List<Expression<T>> graphics;
    private final List<List<FuncVariable<T>>> vars;

    private final List<Expression<T>> expressions;
    private final List<List<FuncVariable<T>>> expressionVars;

    private final List<Stack<Element>> funcTexts;

    public ArrayCalculator() {
        vars = new ArrayList<>();
        funcTexts = new ArrayList<>();
        funcs = new ArrayList<>();
        graphics = new ArrayList<>();
        expressions = new ArrayList<>();
        expressionVars = new ArrayList<>();
        consts = new ArrayList<>();
        director = new Director<>();
    }

    public void calculate(List<String> funcs, List<String> graphs, List<String> calc, AbstractType<T> type) {
        this.type = type;
        director.setType(type);
        clear();
        for (int i = 0; i < funcs.size(); ++i) {
            funcs.set(i, funcs.get(i).replaceAll("[ \t]", ""));
            int n = funcs.get(i).indexOf("=");
            if (n < 1)
                continue;
            String start = funcs.get(i).substring(0, n);
            if (type.consts.containsKey(start)) {
                continue;
            }
            type.addFuncName(start);
        }
        for (int i = 0; i < graphs.size(); ++i) {
            graphs.set(i, graphs.get(i).replaceAll("[ \t]", ""));
            type.addFuncName(graphs.get(i).substring(0, graphs.get(i).indexOf("=")));
        }
        int err = 0;
        try {
            for (; err < graphs.size(); ++err) {
                String s = graphs.get(err);
                int n = s.indexOf('=');
                analise(s.substring(0, n), s.substring(n + 1), true);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage() + " " + CalcLanguage.CALCULATOR_ERRORS[0] + " " + (err + 1) + " " + CalcLanguage.CALCULATOR_ERRORS[1]);
        }
        err = 0;
        try {
            for (; err < funcs.size(); ++err) {
                String s = funcs.get(err);
                int n = s.indexOf('=');
                if (n == -1)
                    continue;
                String start = s.substring(0, n);
                if (type.consts.containsKey(start)) {
                    int args = director.parse(s.substring(n + 1));
                    if (args != 0) {
                        throw new RuntimeException(CalcLanguage.CALCULATOR_ERRORS[6] + " " + CalcLanguage.CALCULATOR_ERRORS[0] + " " + CalcLanguage.CALCULATOR_ERRORS[7]);
                    }
                    Variable<T> var = type.consts.get(start);
                    AbstractConst<T> c = new AbstractConst<>(var);
                    c.stack = director.getStack();
                    consts.add(c);
                    continue;
                }
                analise(start, s.substring(n + 1), false);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage() + " " + CalcLanguage.CALCULATOR_ERRORS[0]
                    + " " + (err + 1) + " " + CalcLanguage.CALCULATOR_ERRORS[2]);
        }

        for (int i = 0; i < this.funcs.size(); ++i) {
            try {
                director.update(funcTexts.get(i));
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage() + " " + CalcLanguage.CALCULATOR_ERRORS[0]
                        + " " + CalcLanguage.CALCULATOR_ERRORS[3] + " " + (i + 1)
                        + " " + CalcLanguage.CALCULATOR_ERRORS[2]);
            }
            try {
                this.funcs.get(i).setFunc(director.getTree(), director.getVars());
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage() + " " + CalcLanguage.CALCULATOR_ERRORS[0]
                        + " " + CalcLanguage.CALCULATOR_ERRORS[4] + " "
                        + (i + 1) + " " + CalcLanguage.CALCULATOR_ERRORS[2]);
            }
            if (i < graphs.size()) {
                this.graphics.add(director.getTree());
                if (this.vars.size() == i)
                    this.vars.add(new ArrayList<>());
                this.vars.get(i).clear();
                this.vars.get(i).addAll(director.getVars());
            }
        }
        for (AbstractConst<T> aConst : consts) {
            director.update(aConst.stack);
            aConst.setExpression(director.getTree());
        }
        try {
            int n = director.parse(calc.get(0));
            if (n != 0) {
                throw new RuntimeException(CalcLanguage.CALCULATOR_ERRORS[6]);
            }
            director.update(director.getStack());
            expressions.add(director.getTree());
            for (int i = 1; i < calc.size(); ++i) {
                director.parse(calc.get(i));
                director.update(director.getStack());
                expressions.add(director.getTree());
                if (expressionVars.size() == i - 1)
                    expressionVars.add(new ArrayList<>());
                expressionVars.get(i - 1).clear();
                expressionVars.get(i - 1).addAll(director.getVars());
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage() + " " + CalcLanguage.CALCULATOR_ERRORS[0]
                    + " " + CalcLanguage.CALCULATOR_ERRORS[5]);
        }
    }

    private void analise(String start, String end, boolean graphic) {
        int args = director.parse(end);
        if (args == 0 && !graphic) {
            Variable<T> var = new Variable<>(start, null);
            AbstractConst<T> c = new AbstractConst<>(var);
            int n = consts.indexOf(c);
            if (n != -1) {
                var = consts.get(n).getVar();
                c.setVar(var);
                c.stack = director.getStack();
                consts.add(c);
                return;
            }
            var.setValue(type.def);
            c.stack = director.getStack();
            consts.add(c);
            type.addFunction(start, c, 0, 10);
            return;
        }
        AbstractFunc<T> func = new AbstractFunc<>();
        switch (args) {
            case 1:
                type.addFunctionUnary(start, func.getUnary(), 10);
                break;
            case 2:
                type.addFunction(start, func.getBinary(), 10);
                break;
            default:
                type.addFunction(start, func.getMulti(args), args, 10);
        }
        funcs.add(func);
        funcTexts.add(director.getStack());
    }
    private void clear(){
        funcTexts.clear();
        this.funcs.clear();
        for (Expression<T> graphic : graphics) {
            graphic.free();
        }
        this.graphics.clear();
        consts.clear();
        for(Expression<T> c: expressions){
            c.free();
        }
        expressions.clear();
        System.gc();
    }
    public List<Expression<T>> getGraphics() {
        return graphics;
    }

    public List<List<FuncVariable<T>>> getVars() {
        return vars;
    }

    public List<Expression<T>> getExpressions() {
        return expressions;
    }

    public List<List<FuncVariable<T>>> getExpressionVars() {
        return expressionVars;
    }

    public List<AbstractConst<T>> getConsts() {
        return consts;
    }

    public void resetConstant(String name, T val) {
        Variable<T> var = type.consts.get(name);
        if (var != null)
            var.setValue(val);
        else
            type.addConst(name, val);
    }
}
