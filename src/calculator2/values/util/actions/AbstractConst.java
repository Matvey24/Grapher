package calculator2.values.util.actions;

import calculator2.calculator.Element;
import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.values.util.actions.functions.MultiFunc;

import java.util.Stack;

public class AbstractConst<T> implements Runnable{
    private Expression<T> expression;
    private final Variable<T> var;
    public Stack<Element> stack;
    public AbstractConst(Variable<T> var){
        this.var = var;
    }
    public void setExpression(Expression<T> expression) {
        this.expression = expression;
        this.stack = null;
    }

    public void run(){
        var.setValue(expression.calculate());
    }

    public Variable<T> getVar(){
        return var;
    }
    private T execute(Expression<T>[] a){
        return var.calculate();
    }
    public MultiFunc<T> getFunc(){
        return this::execute;
    }
}
