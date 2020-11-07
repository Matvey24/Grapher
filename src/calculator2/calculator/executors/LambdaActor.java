package calculator2.calculator.executors;

import calculator2.calculator.executors.actors.Expression;

public class LambdaActor<T> implements Expression<T> {
    private LambdaParameter<T> parameter;
    private String name;
    private Expression<T>[] a;
    public void setValues(LambdaParameter<T> parameter, String name, Expression<T>[] a){
        this.parameter = parameter;
        this.name = name;
        this.a = a;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void free() {
        for (Expression<T> tExpression : a)
            tExpression.free();
        parameter = null;
        name = null;
        a = null;
    }

    @Override
    public T calculate() {
        return parameter.execute(a);
    }
}
