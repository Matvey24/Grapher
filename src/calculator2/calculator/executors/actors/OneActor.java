package calculator2.calculator.executors.actors;

import calculator2.calculator.util.actions.functions.OneFunc;

public class OneActor<T> implements Expression<T> {
    private Expression<T> a;
    private OneFunc<T> func;
    private String name;
    public void setValues(OneFunc<T> func, Expression<T> a, String name){
        this.func = func;
        this.a = a;
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public T calculate() {
        return func.execute(a.calculate());
    }

    @Override
    public void free() {
        a.free();
        func = null;
        name = null;
    }

    @Override
    public String toString() {
        return getName() + "(" + a.toString() + ")";
    }
}
