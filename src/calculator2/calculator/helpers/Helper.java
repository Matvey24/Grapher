package calculator2.calculator.helpers;

import calculator2.values.util.AbstractType;
import calculator2.values.util.actions.Sign;

public class Helper<T> {
    public final Brackets brackets;
    public final Consts<T> consts;
    public final Funcs<T> funcs;
    public final Signs<T> signs;
    private AbstractType<T> type;
    public Helper(AbstractType<T> type){
        brackets = new Brackets();
        consts = new Consts<>();
        funcs = new Funcs<>();
        signs = new Signs<>();
        renewType(type);
    }
    public void renewType(AbstractType<T> type){
        this.type = type;
        funcs.clear();
        consts.clear();
        signs.clear();
        for(int i = 0; i < type.funcs.size(); ++i)
            funcs.add(type.funcs.get(i));
        consts.addAll(type.consts);
        for(int i = 0; i < type.signs.size(); ++i)
            signs.add(type.signs.get(i));
    }
    public boolean hasName(String name){
        return consts.getConst(name) != null || funcs.getFunc(name) != null || isVar(name);
    }
    public T toValue(String text){
        return type.toValue(text);
    }
    public boolean isDivider(char c){
        return c == ',' || c == ';';
    }
    public boolean isVar(String s){
        return s.equals("x") || s.equals("y") || s.equals("z") || s.equals("var");
    }
    public boolean isType(char c){
        return  c >= '0' && c <= '9' || c == '.';
    }
    public Sign<T> getMissingSign() {
        return type.getMissingSign();
    }
}
