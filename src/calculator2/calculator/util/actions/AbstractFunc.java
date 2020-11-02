package calculator2.calculator.util.actions;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.calculator.util.actions.functions.BinarFunc;
import calculator2.calculator.util.actions.functions.MultiFunc;
import calculator2.calculator.util.actions.functions.UnarFunc;

import java.util.ArrayList;
import java.util.List;

public class AbstractFunc<T>{
    private Expression<T> expression;
    private Variable<T> a;
    private Variable<T> b;
    private List<Variable<T>> arr;
    private List<List<T>> buffer;
    private int args;

    private T execute(T a) {
        T fa = this.a.calculate();
        this.a.setValue(a);
        T ret = expression.calculate();
        this.a.setValue(fa);
        return ret;
    }
    private T execute(Expression<T> a, Expression<T> b) {
        T va = a.calculate();
        T vb = b.calculate();
        T fa = this.a.calculate();
        T fb = this.b.calculate();
        this.a.setValue(va);
        this.b.setValue(vb);
        T ret = expression.calculate();
        this.a.setValue(fa);
        this.b.setValue(fb);
        return ret;
    }
    private T execute(Expression<T>[] arr){
        List<T> b;
        if(buffer.size() > 1){
            b = buffer.remove(buffer.size() - 1);
            for(int i = 0; i < b.size(); ++i)
                b.set(i, arr[i].calculate());
            for(int i = b.size(); i < arr.length; ++i)
                b.add(arr[i].calculate());
        }else{
            b = new ArrayList<>(arr.length);
            for (Expression<T> e : arr)
                b.add(e.calculate());
        }
        for(int i = 0; i < arr.length; ++i) {
            T t = this.arr.get(i).calculate();
            this.arr.get(i).setValue(b.get(i));
            b.set(i, t);
        }
        T ret = expression.calculate();
        for(int i = 0; i < arr.length; ++i){
            this.arr.get(i).setValue(b.get(i));
        }
        buffer.add(b);
        return ret;
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
                buffer = new ArrayList<>(2);
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
