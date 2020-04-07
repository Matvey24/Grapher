package calculator2.values.util.actions;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.values.util.actions.functions.BinarFunc;
import calculator2.values.util.actions.functions.MultiFunc;
import calculator2.values.util.actions.functions.UnarFunc;

import java.util.ArrayList;
import java.util.List;

public class AbstractFunc<T>{
    private Expression<T> expression;
    private Variable<T> a;
    private Variable<T> b;
    private List<Variable<T>> arr;
    private List<T> buffer;
    private int args;

    private T execute(T a) {
        this.a.setValue(a);
        return expression.calculate();
    }
    private T execute(Expression<T> a, Expression<T> b) {
        T va = a.calculate();
        T vb = b.calculate();
        this.a.setValue(va);
        this.b.setValue(vb);
        return expression.calculate();
    }
    private T execute(Expression<T>[] arr){
        for(int i = 0; i < arr.length; ++i)
            buffer.set(i, arr[i].calculate());
        for(int i = 0; i < arr.length; ++i)
            this.arr.get(i).setValue(buffer.get(i));
        return expression.calculate();
    }
    public void setFunc(Expression<T> expression, List<Variable<T>> vars){
        this.expression = expression;
        if(vars.size() != args){
            throw new RuntimeException(String.format("We need %d vars, now %d", args,vars.size()));
        }
        switch (vars.size()){
            case 2:
                b = vars.get(1);
            case 1:
                a = vars.get(0);
                break;
            default:
                arr = new ArrayList<>(vars);
                buffer = new ArrayList<>(vars.size());
                for(int i = 0; i < vars.size(); ++i)
                    buffer.add(null);
        }
    }
    public UnarFunc<T> getUnary(){
        args = 1;
        return this::execute;
    }
    public BinarFunc<T> getBinary(){
        args = 2;
        return this::execute;
    }
    public MultiFunc<T> getMulti(int args){
        this.args = args;
        return this::execute;
    }

}
