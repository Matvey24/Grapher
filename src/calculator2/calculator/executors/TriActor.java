package calculator2.calculator.executors;

import calculator2.values.util.actions.functions.TernarFunc;

public class TriActor<T> implements Expression<T> {
    private Expression<T> a;
    private Expression<T> b;
    private Expression<T> c;
    private TernarFunc<T> func;
    private String name;
    public void setValues(TernarFunc<T> func, Expression<T> a, Expression<T> b, Expression<T> c, String name){
        this.func = func;
        this.a = a;
        this.b = b;
        this.c = c;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + "(" + a.toString() + "," + b.toString() + "," + c.toString() + ")";
    }

    @Override
    public T calculate() {
        return func.execute(a.calculate(), b.calculate(), c.calculate());
    }
}
