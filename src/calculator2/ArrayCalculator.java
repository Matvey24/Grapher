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
    private List<Variable<T>> vars;

    private List<String> funcNames;

    public T calculate(List<String> funcs, List<String> graphics, String calc, AbstractType<T> type) {
        this.type = type;
        director = new Director<>(type);
        vars = new ArrayList<>();
        funcNames = new ArrayList<>();
        this.funcs = new ArrayList<>();
        this.graphics = new ArrayList<>();
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
                this.vars.add(director.getVars().get(0));
            }
        }
        try {
            director.update(calc);
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage() + " in calc");
        }
        return director.calculate();
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

    public List<Variable<T>> getVars() {
        return vars;
    }
}
