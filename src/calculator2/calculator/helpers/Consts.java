package calculator2.calculator.helpers;

import calculator2.calculator.executors.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Consts<T> {
    private HashMap<String, Variable<T>> map;
    Consts(){
        map = new HashMap<>();
    }
    public Variable<T> getConst(String name){
        return map.get(name);
    }
    void addAll(List<Variable<T>> map) {
        for(Variable<T> c:map)
            this.map.put(c.getName(), c);
    }
    void clear(){
        map.clear();
    }
}
