package calculator2.calculator.helpers;

import calculator2.values.util.actions.Func;

import java.util.HashMap;
import java.util.List;

public class Funcs<T> {
    private final HashMap<String, Func<T>> functions;
    Funcs(){
        functions = new HashMap<>();
    }
    void addAll(List<Func<T>> list) {
        for(Func<T> f: list)
            this.functions.put(f.name, f);
    }
    public Func<T> getFunc(String name){
        return functions.get(name);
    }
    void clear(){
        functions.clear();
    }
}
