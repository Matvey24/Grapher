package calculator2.values.util.actions;

import calculator2.values.util.actions.functions.BinarFunc;
import calculator2.values.util.actions.functions.MultiFunc;
import calculator2.values.util.actions.functions.UnarFunc;

public class Func<T> {
    public final UnarFunc<T> unarFunc;
    public final BinarFunc<T> binarFunc;
    public final MultiFunc<T> multiFunc;
    public final int args;
    public final String name;
    public final int priority;
    public Func(String name, UnarFunc<T> function, int priority){
        this.name = name;
        this.unarFunc = function;
        this.priority = priority;
        this.args = 1;
        binarFunc = null;
        multiFunc = null;
    }
    public Func(String name, BinarFunc<T> function, int priority){
        this.name = name;
        this.binarFunc = function;
        this.priority = priority;
        this.args = 2;
        unarFunc = null;
        multiFunc = null;
    }
    public Func(String name, MultiFunc<T> function, int args,  int priority){
        this.name = name;
        this.multiFunc = function;
        this.priority = priority;
        this.args = args;
        unarFunc = null;
        binarFunc = null;
    }
}
