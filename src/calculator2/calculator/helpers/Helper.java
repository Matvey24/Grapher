package calculator2.calculator.helpers;

import calculator2.values.util.AbstractType;
import calculator2.values.util.actions.Sign;

public class Helper<T> {
    public final Brackets brackets;
    public final Consts<T> consts;
    public final Funcs<T> funcs;
    public final Signs<T> signs;
    private AbstractType<T> type;
    public Helper(){
        brackets = new Brackets();
        consts = new Consts<>();
        funcs = new Funcs<>();
        signs = new Signs<>();
    }
    public void renewType(AbstractType<T> type){
        this.type = type;
        funcs.clear();
        consts.clear();
        signs.clear();
        funcs.addAll(type.funcs);
        consts.addAll(type.consts);
        signs.addAll(type.signs);
    }
    public boolean hasName(String name){
        return consts.getConst(name) != null || funcs.getFunc(name) != null || isVar(name) || type.funcNames.contains(name);
    }
    public boolean isConstant(String name){
        return consts.getConst(name) != null;
    }
    public T toValue(String text){
        return type.toValue(text);
    }
    public boolean isDivider(char c){
        return c == ',' || c == ';';
    }
    public boolean isVar(String s){
        return s.equals("x") || s.equals("y") || s.equals("z") || s.equals("t") || (s.length() == 2 && s.charAt(0) == 'x');
    }
    public boolean isType(char c){
        return  c >= '0' && c <= '9' || c == '.';
    }
    public Sign<T> getMissingSign() {
        return type.getMissingSign();
    }
}
