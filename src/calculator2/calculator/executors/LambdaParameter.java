package calculator2.calculator.executors;

import calculator2.calculator.executors.actors.Expression;
import calculator2.calculator.util.actions.functions.MultiFunc;

import java.util.Stack;

public class LambdaParameter<T> extends FuncVariable<T> implements MultiFunc<T>{
    private final Stack<LambdaContainer<T>> funcs;
    private final Stack<Boolean> isVar;
    private LambdaContainer<T> func;
    private boolean isLambda;
    private final Expression<T>[] empty;
    public LambdaParameter(Expression<T>[] empty) {
        super();
        this.empty = empty;
        this.funcs = new Stack<>();
        isVar = new Stack<>();
    }
    @Override
    public void save() {
        if(isLambda){
            funcs.push(func);
        }else{
            super.save();
        }
        isVar.push(isLambda);
    }

    @Override
    public void setValue(Expression<T> exp) {
        boolean lam = exp instanceof LambdaContainer;
        if(lam){
            LambdaContainer<T> e = (LambdaContainer<T>) exp;
            if(e.getFunc() instanceof LambdaParameter)
                funcs.push(((LambdaParameter<T>) e.getFunc()).func);
            else
                funcs.push((LambdaContainer<T>) exp);
        }else{
            super.setValue(exp);
        }
        isVar.push(lam);
    }

    @Override
    public void set() {
        isLambda = isVar.pop();
        if(isLambda){
            func = funcs.pop();
        }else{
            super.set();
        }
    }
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public T execute(Expression<T>[] a) {
        if(isLambda){
            return func.execute(a);
        }
        return super.calculate();
    }

    @Override
    public T calculate() {
        if(isLambda){
            return func.execute(empty);
        }
        return super.calculate();
    }

    @Override
    public void free() {
        super.free();
    }

}
