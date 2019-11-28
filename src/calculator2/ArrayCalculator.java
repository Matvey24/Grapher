package calculator2;

import calculator2.calculator.Director;
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


    private List<String> funcNames;

    public ArrayCalculator(){
        vars = new ArrayList<>();
        funcNames = new ArrayList<>();
        funcs = new ArrayList<>();
        graphics = new ArrayList<>();
        expressions = new ArrayList<>();
        expressionVars = new ArrayList<>();
    }
    public void calculate(List<String> funcs, List<String> graphics, List<String> calc, AbstractType<T> type) {
        this.type = type;
        director = new Director<>(type);
        funcNames.clear();
        this.funcs.clear();
        this.graphics.clear();
        expressions.clear();
        for (String s : graphics) {
            String t = s.replaceAll("[ \t]", "");
            int n = t.indexOf('=');
            if (n == -1) {
                continue;
            }
            analise(t.substring(0, n), t.substring(n + 1));
        }
        for (String s : funcs) {
            String t = s.replaceAll("[ \t]", "");
            int n = t.indexOf('=');
            if (n == -1) {
                continue;
            }
            analise(t.substring(0, n), t.substring(n + 1));
        }
        director.renewType(type);
        for (int i = 0; i < this.funcs.size(); ++i) {
            try {
                director.update(funcNames.get(i));
            }catch (RuntimeException e){
                throw new RuntimeException(e.getMessage() + " in " + (i + 1) + " func");
            }
            if (director.getVars().size() == 0) {
                Variable<T> var = new Variable<>();
                var.setName("x");
                director.getVars().add(var);
            }
            try {
                this.funcs.get(i).setFunc(director.getTree(), director.getVars());
            }catch (RuntimeException e){
                throw new RuntimeException(e.getMessage() + " in " + (i + 1) + " func");
            }
            if (i < graphics.size()) {
                this.graphics.add(director.getTree());
                if(this.vars.size() == i)
                    this.vars.add(new ArrayList<>());
                this.vars.get(i).clear();
                this.vars.get(i).addAll(director.getVars());
            }
        }
        try {
            director.update(calc.get(0));
            expressions.add(director.getTree());
            for(int i = 1; i < calc.size(); ++i) {
                director.update(calc.get(i));
                expressions.add(director.getTree());
                if(expressionVars.size() == i - 1)
                    expressionVars.add(new ArrayList<>());
                expressionVars.get(i - 1).clear();
                if(director.getVars().size() == 0) {
                    expressionVars.get(i - 1).add(new Variable<>());
                }else
                    expressionVars.get(i - 1).addAll(director.getVars());
            }
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage() + " in calc");
        }
    }
    private void analise(String start, String end) {
        int pos = start.indexOf(':');
        if (pos == -1) {
            try {
                director.update(end);
            }catch (RuntimeException e){
                throw new RuntimeException(e.getMessage() + " in var " + start);
            }
            type.addConst(start, director.calculate());
        } else {
            AbstractFunc<T> func = new AbstractFunc<>();
            String name = start.substring(0, pos);
            int args = Integer.parseInt(start.substring(pos + 1));
            switch (args) {
                case 3:
                    type.addFunction(name, func.getTernary(), 5);
                    break;
                case 2:
                    type.addFunction(name, func.getBinary(), 5);
                    break;
                case 1:
                    type.addFunction(name, func.getUnary(), 5);
                    break;
                default:
                    throw new RuntimeException("Args count in func " + name + " is bad: " + args);
            }
            funcs.add(func);
            funcNames.add(end);
        }
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
    public void resetConstant(String name, T val){
        Variable<T> var = director.getHelper().consts.getConst(name);
        if(var != null)
            var.setValue(val);
    }
}
