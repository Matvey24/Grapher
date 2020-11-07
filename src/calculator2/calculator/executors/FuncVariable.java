package calculator2.calculator.executors;

import calculator2.calculator.executors.actors.Expression;

import java.util.Stack;

public class FuncVariable<T> extends Variable<T> {
    private final Stack<T> tmp;
    public FuncVariable() {
        super();
        tmp = new Stack<>();
    }

    @Override
    public void free() {
        super.free();
        tmp.clear();
    }

    public void save(){
        tmp.push(calculate());
    }
    public void setValue(Expression<T> exp){
        tmp.push(exp.calculate());
    }
    public void set(){
        this.setValue(tmp.pop());
    }
}
