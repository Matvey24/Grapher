package calculator2.calculator.executors;

import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.util.actions.functions.MultiFunc;

import java.util.List;

public class LambdaInitializer<T> implements Expression<T>, MultiFunc<T> {
    private List<FuncVariable<T>> a;
    private Expression<T> func;
    public void setValues(Expression<T> func, List<FuncVariable<T>> a){
        this.func = func;
        this.a = a;
    }
    public T execute(Expression<T>[] a){
        int size = Math.min(a.length, this.a.size());
        for(int i = 0; i < size; ++i)
            this.a.get(i).save();
        for(int i = 0; i < size; ++i)
            this.a.get(i).setValue(a[i]);
        for(int i = 0; i < size; ++i)
            this.a.get(i).set();
        T ret = func.calculate();
        for(int i = 0; i < size; ++i)
            this.a.get(i).set();
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
        for (Expression<T> exp : a) {
            exp.free();
        }
        func.free();
        func = null;
        a = null;
    }
}
