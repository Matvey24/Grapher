package calculator2.calculator.util;

import calculator2.calculator.executors.Variable;
import calculator2.calculator.util.actions.Func;
import calculator2.calculator.util.actions.Sign;
import calculator2.calculator.util.actions.functions.BinarFunc;
import calculator2.calculator.util.actions.functions.MultiFunc;
import calculator2.calculator.util.actions.functions.OneFunc;
import calculator2.calculator.util.actions.functions.UnarFunc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractType<T> {
    public final int lambda_priority;
    public final HashMap<Character, Sign<T>> signs;
    public final HashMap<String, Variable<T>> consts;
    public final HashMap<String, Func<T>> funcs;
    public final List<String> funcNames;
    private Sign<T> missingSign;
    public final T def;
    protected AbstractType(int lambda_priority, T def) {
        this.def = def;
        this.lambda_priority = lambda_priority;
        signs = new HashMap<>();
        funcs = new HashMap<>();
        consts = new HashMap<>();
        funcNames = new ArrayList<>();
    }

    protected Sign<T> addSign(char name, BinarFunc<T> sign, int priority) {
        Sign<T> s = new Sign<>(name, sign, priority);
        signs.put(name, s);
        return s;
    }

    protected void unarySign(Sign<T> sign, OneFunc<T> func) {
        sign.canBeUnary = true;
        addFunction("" + sign.name, func, 5);
    }

    protected void setMissingSign(Sign<T> sign) {
        this.missingSign = sign;
    }

    public void addConst(String name, T state) {
        Variable<T> cons = consts.get(name);
        if (cons == null) {
            Variable<T> var = new Variable<>(name, state);
            consts.put(name, var);
        }
    }

    public void addFunction(String name, OneFunc<T> oneFunc, int priority) {
        funcs.put(name, new Func<>(name, oneFunc, priority));
    }

    public void addFunctionUnary(String name, UnarFunc<T> unarFunc, int priority) {
        funcs.put(name, new Func<>(name, unarFunc, priority));
    }

    public void addFunction(String name, BinarFunc<T> binarFunc, int priority) {
        funcs.put(name, new Func<>(name, binarFunc, priority));
    }

    public void addFunction(String name, MultiFunc<T> multiFunc, int args, int priority) {
        funcs.put(name, new Func<>(name, multiFunc, args, priority));
    }

    public abstract T toValue(String text);

    public abstract boolean isType(char c);

    public Sign<T> getMissingSign() {
        return missingSign;
    }

    public void addFuncName(String name) {
        funcNames.add(name);
    }
}
