package calculator2.calculator.helpers;

import calculator2.values.util.actions.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class Consts<T> {
    private HashMap<String, Constant<T>> map;
    Consts(){
        map = new HashMap<>();
    }
    public Constant<T> getConst(String name){
        return map.get(name);
    }
    void addAll(ArrayList<Constant<T>> map) {
        for(Constant<T> c:map){
            this.map.put(c.name, c);
        }
    }
    void clear(){
        map.clear();
    }
}
