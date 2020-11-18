package calculator2.calculator.helpers;

import java.util.HashMap;

public class Brackets {
    private final HashMap<Character, Boolean> names;
    private final HashMap<String, String> opposits;

    Brackets() {
        names = new HashMap<>();
        opposits = new HashMap<>();
        add('(', ')');
        add('[', ']');
        add('{', '}');
    }

    public void add(char c1, char c2) {
        names.put(c1, true);
        names.put(c2, false);
        opposits.put("" + c1, "" + c2);
        opposits.put("" + c2, "" + c1);
    }

    public boolean brOpens(String bracket) {
        return brOpens(bracket.charAt(0));
    }

    public String getOpposit(String bracket) {
        return opposits.get(bracket);
    }
    public boolean brLambda(String bracket){
        return bracket.charAt(0) == '}' || bracket.charAt(0) == '{';
    }
    public boolean brParam(String bracket){
        return brParam(bracket.charAt(0));
    }
    public boolean brParam(char br){
        return br == '$';
    }
    private boolean brOpens(char bracket) {
        return names.get(bracket);
    }

    public boolean isBracket(char bracket) {
        return names.containsKey(bracket);
    }
}
