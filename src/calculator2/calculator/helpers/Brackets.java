package calculator2.calculator.helpers;

import java.util.HashMap;

public class Brackets {
    private final HashMap<Character, Boolean> names;
    Brackets(){
        names = new HashMap<>();
        add('(', ')');
        add('[', ']');
        add('{', '}');
    }
    public void add(char c1, char c2){
        names.put(c1, true);
        names.put(c2, false);
    }
    public boolean brOpens(String bracket){
        return brOpens(bracket.charAt(0));
    }
    private boolean brOpens(char bracket){
        return names.get(bracket);
    }
    public boolean isBracket(String bracket){return isBracket(bracket.charAt(0));}
    public boolean isBracket(char bracket){return names.containsKey(bracket);}
}
