package calculator2.values.util.actions;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.values.util.actions.functions.BinarFunc;
import calculator2.values.util.actions.functions.TernarFunc;
import calculator2.values.util.actions.functions.UnarFunc;

import java.util.ArrayList;

public class AbstractFunc<T>{
    private Expression<T> expression;
    private Variable<T> a;
    private Variable<T> b;
    private Variable<T> c;
    private int argcount;

    private T execute(T a) {
        this.a.setValue(a);
        return expression.calculate();
    }
    private T execute(T a, T b) {
        this.a.setValue(a);
        this.b.setValue(b);
        return expression.calculate();
    }
    private T execute(T a, T b, T c) {
        this.a.setValue(a);
        this.b.setValue(b);
        this.c.setValue(c);
        return expression.calculate();
    }

    public void setFunc(Expression<T> expression, ArrayList<Variable<T>> vars){
        this.expression = expression;
        if(vars.size() != argcount){
            throw new RuntimeException(String.format("We need %d vars in function, now %d", argcount,vars.size()));
        }
        switch (vars.size()){
            case 3:
                c = vars.get(2);
            case 2:
                b = vars.get(1);
            case 1:
                a = vars.get(0);
        }
    }
    public UnarFunc<T> getUnary(){
        argcount = 1;
        return this::execute;
    }
    public BinarFunc<T> getBinary(){
        argcount = 2;
        return this::execute;
    }
    public TernarFunc<T> getTernary(){
        argcount = 3;
        return this::execute;
    }

}
