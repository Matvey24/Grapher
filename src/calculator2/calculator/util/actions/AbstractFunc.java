package calculator2.calculator.util.actions;

import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.executors.FuncVariable;
import calculator2.calculator.util.actions.functions.BinarFunc;
import calculator2.calculator.util.actions.functions.MultiFunc;
import calculator2.calculator.util.actions.functions.UnarFunc;

import java.util.ArrayList;
import java.util.List;

public class AbstractFunc<T> {
    private Expression<T> expression;
    private FuncVariable<T> a;
    private FuncVariable<T> b;
    private List<FuncVariable<T>> vars;
    private int args;

    private T execute(Expression<T> _a) {
        a.save();
        a.setValue(_a);
        a.set();
        T ret = expression.calculate();
        a.set();
        return ret;
    }

    private T execute(Expression<T> _a, Expression<T> _b) {
        a.save();
        b.save();
        a.setValue(_a);
        b.setValue(_b);
        a.set();
        b.set();
        T ret = expression.calculate();
        a.set();
        b.set();
        return ret;
    }

    private T execute(Expression<T>[] _arr) {
        for(FuncVariable<T> var: vars)
            var.save();
        for (int i = 0; i < vars.size(); ++i)
            vars.get(i).setValue(_arr[i]);
        for(FuncVariable<T> var: vars)
            var.set();
        T ret = expression.calculate();
        for(FuncVariable<T> var: vars)
            var.set();
        return ret;
    }

    public void setFunc(Expression<T> expression, List<FuncVariable<T>> vars) {
        this.expression = expression;
        if (vars.size() != args) {
            throw new RuntimeException(String.format("We need %d vars, now %d", args, vars.size()));
        }
        switch (vars.size()) {
            case 2:
                b = vars.get(1);
            case 1:
                a = vars.get(0);
                break;
            default:
                this.vars = new ArrayList<>(vars);
        }
    }

    public UnarFunc<T> getUnary() {
        args = 1;
        return this::execute;
    }

    public BinarFunc<T> getBinary() {
        args = 2;
        return this::execute;
    }

    public MultiFunc<T> getMulti(int args) {
        this.args = args;
        return this::execute;
    }
}
