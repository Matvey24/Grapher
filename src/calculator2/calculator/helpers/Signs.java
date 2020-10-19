package calculator2.calculator.helpers;

import calculator2.calculator.util.actions.Sign;

import java.util.HashMap;
import java.util.List;

public class Signs<T> {
    private final HashMap<Character, Sign<T>> signs;
    Signs(){
        signs = new HashMap<>();
    }
    public void addAll(List<Sign<T>> signs){
        for(Sign<T> sign: signs)
            this.signs.put(sign.name, sign);
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
