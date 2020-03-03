package calculator2.calculator.executors;

import calculator2.values.util.actions.functions.BinarFunc;

public class BinaryActor<T> implements Expression<T> {
    private Expression<T> a;
    private Expression<T> b;
    private BinarFunc<T> func;
    private String name;
    public void setValues(BinarFunc<T> func, Expression<T> a, Expression<T> b, String name){
        this.func = func;
        this.a = a;
        this.b = b;
        this.name = name;
    }

    public void setFunc(BinarFunc<T> func) {
        this.func = func;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + "(" + a.toString() + "," + b.toString() + ")";
    }

    @Override
    public T calculate() {
        return func.execute(a, b);
    }
}
