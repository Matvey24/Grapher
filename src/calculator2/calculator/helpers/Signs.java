package calculator2.calculator.helpers;

import calculator2.values.util.actions.Sign;

import java.util.HashMap;

public class Signs<T> {
    private HashMap<Character, Sign<T>> signs;
    Signs(){
        signs = new HashMap<>();
    }
    public void add(Sign<T> sign){
        signs.put(sign.name, sign);
    }
    public Sign<T> getSign(String sign){
        return getSign(sign.charAt(0));
    }
    public Sign<T> getSign(char sign){
        return signs.get(sign);
    }
    void clear(){
        signs.clear();
    }
}
