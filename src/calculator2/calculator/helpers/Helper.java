package calculator2.calculator.helpers;

import calculator2.calculator.executors.Variable;
import calculator2.calculator.util.AbstractType;
import calculator2.calculator.util.actions.AbstractConst;
import calculator2.calculator.util.actions.Func;
import calculator2.calculator.util.actions.Sign;

public class Helper<T> {
    public final Brackets brackets;
    private AbstractType<T> type;

    public Helper() {
        brackets = new Brackets();
    }
    public void setType(AbstractType<T> type){
        this.type = type;
    }
    public boolean hasName(String name) {
        return isConst(name) || getFunc(name) != null || isVar(name) || type.funcNames.contains(name);
    }

    public boolean isConst(String name) {
        return type.consts.get(name) != null;
    }

    public Variable<T> getConst(String name) {
        return type.consts.get(name);
    }

    public Sign<T> getSign(String name) {
        return getSign(name.charAt(0));
    }

    public Sign<T> getSign(char c) {
        return type.signs.get(c);
    }

    public Func<T> getFunc(String name) {
        return type.funcs.get(name);
    }

    public T toValue(String text) {
        return type.toValue(text);
    }

    public boolean isType(char c) {
        return type.isType(c);
    }

    public boolean isDivider(char c) {
        return c == ',' || c == ';';
    }

    public boolean isVar(String s) {
        return s.equals("x") || s.equals("y") || s.equals("z") || s.equals("t") || (s.length() == 2 && s.charAt(0) == 'x');
    }

    public Sign<T> getMissingSign() {
        return type.getMissingSign();
    }
}
