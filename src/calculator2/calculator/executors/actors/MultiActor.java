package calculator2.calculator.executors.actors;

import calculator2.calculator.util.actions.functions.MultiFunc;

import java.util.Arrays;

public class MultiActor<T> implements Expression<T> {
    protected Expression<T>[] a;
    protected String name;
    protected MultiFunc<T> func;
    public void setValues(MultiFunc<T> func, String name, Expression<T>[] a){
        this.func = func;
        this.a = a;
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String arr = Arrays.toString(a);
        return getName() + "(" + arr.substring(1, arr.length() - 1) + ")";
    }

    @Override
    public void free() {
        for (Expression<T> tExpression : a) {
            tExpression.free();
        }
        a = null;
        name = null;
        func = null;
    }

    @Override
    public T calculate() {
        return func.execute(a);
    }
}
