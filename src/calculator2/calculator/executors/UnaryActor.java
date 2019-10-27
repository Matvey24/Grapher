package calculator2.calculator.executors;

import calculator2.values.util.actions.functions.UnarFunc;

public class UnaryActor<T> implements Expression<T> {
    private Expression<T> a;
    private UnarFunc<T> func;
    private String name;
    public void setValues(UnarFunc<T> func, Expression<T> a, String name){
        this.func = func;
        this.a = a;
        this.name = name;
    }

    public void setExpression(Expression<T> a){
        this.a = a;
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
    public String toString() {
        return getName() + "(" + a.toString() + ")";
    }
}
