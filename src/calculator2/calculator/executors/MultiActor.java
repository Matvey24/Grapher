package calculator2.calculator.executors;

import calculator2.values.util.actions.functions.MultiFunc;

import java.util.Arrays;

public class MultiActor<T> implements Expression<T> {
    private Expression<T>[] a;
    private String name;
    private MultiFunc<T> func;
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
    public T calculate() {
        return func.execute(a);
    }
}
