package calculator2.calculator.util.actions;

import calculator2.calculator.Element;
import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.calculator.util.actions.functions.MultiFunc;

import java.util.Stack;

public class AbstractConst<T> implements Runnable, MultiFunc<T>{
    private Expression<T> expression;
    private Variable<T> var;
    public Stack<Element> stack;
    public AbstractConst(Variable<T> var){
        this.var = var;
    }

    public void setExpression(Expression<T> expression) {
        this.expression = expression;
        this.stack = null;
    }

    public void setVar(Variable<T> var) {
        this.var = var;
    }

    public void run(){
        var.setValue(expression.calculate());
    }

    public Variable<T> getVar(){
        return var;
    }
    public T execute(Expression<T>[] a){
        return var.calculate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractConst<?> that = (AbstractConst<?>) o;
        return var.equals(that.var);
    }
}
