package calculator2.calculator.executors;

import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.util.actions.functions.MultiFunc;

import java.util.List;

public class LambdaContainer<T> implements Expression<T>, MultiFunc<T> {
    private List<FuncVariable<T>> vars;
    private Expression<T> func;
    public void setValues(Expression<T> func, List<FuncVariable<T>> a){
        this.func = func;
        this.vars = a;
    }
    public T execute(Expression<T>[] a){
        int size = Math.min(a.length, this.vars.size());
        for(int i = 0; i < size; ++i)
            this.vars.get(i).save();
        for(int i = 0; i < size; ++i)
            this.vars.get(i).setValue(a[i]);
        for(int i = 0; i < size; ++i)
            this.vars.get(i).set();
        T ret = func.calculate();
        for(int i = 0; i < size; ++i)
            this.vars.get(i).set();
        return ret;
    }
    public Expression<T> getFunc(){
        return func;
    }
    @Override
    public T calculate() {
        return func.calculate();
    }

    @Override
    public String getName() {
        return "~lambda";
    }

    @Override
    public String toString() {
        return "{" + func + '}';
    }

    @Override
    public void free() {
        for (Expression<T> exp : vars) {
            exp.free();
        }
        func.free();
        func = null;
        vars = null;
    }
}
