package calculator2.calculator;

import calculator2.calculator.helpers.Helper;
import calculator2.values.util.actions.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static calculator2.calculator.Element.ElementType.*;

class Parser<T> {
    private StringBuilder sb;
    private Helper<T> helper;

    Parser(Helper<T> helper) {
        this.helper = helper;
        sb = new StringBuilder();
    }

    Stack<Element> parse(String str) {
        str = str.replaceAll("[ \\n\\t]", "");
        List<Element> list = toList(str);
        normaliseBrackets(list);
        normaliseLetters(list);
        findUnarySigns(list);
        addMissingSigns(list);
        simpleCheck(list);
        return toStack(list);
    }

    private Element.ElementType type(char c) {
        Element.ElementType type;
        if (helper.isType(c)) {
            type = Element.ElementType.NUMBER;
        } else if (helper.brackets.isBracket(c)) {
            type = Element.ElementType.BRACKET;
        } else if (helper.signs.getSign(c) != null) {
            type = Element.ElementType.SIGN;
        } else if (helper.isDivider(c)) {
            type = Element.ElementType.DIVIDER;
        } else {
            type = FUNCTION;
        }
        return type;
    }

    private List<Element> toList(String str) {
        List<Element> list = new ArrayList<>();
        if (str.isEmpty())
            str = "0";
        Element.ElementType lt;
        sb.append(str.charAt(0));
        lt = type(str.charAt(0));
        for (int i = 1; i < str.length(); ++i) {
            char c = str.charAt(i);
            Element.ElementType type = type(c);
            if (lt != type || type == Element.ElementType.BRACKET || type == Element.ElementType.SIGN) {
                Element e = new Element(sb.toString(), lt);
                sb.setLength(0);
                list.add(e);
            }
            lt = type;
            sb.append(c);
        }
        Element e = new Element(sb.toString(), lt);
        sb.setLength(0);
        list.add(e);
        return list;
    }

    private void normaliseBrackets(List<Element> list) {
        int brackets = 0;
        for (Element e : list) {
            if (e.type == Element.ElementType.BRACKET) {
                if (helper.brackets.brOpens(e.symbol)) {
                    ++brackets;
                } else {
                    --brackets;
                }
            }
        }
        for (; brackets > 0; --brackets) {
            list.add(new Element(")", Element.ElementType.BRACKET));
        }
        for (; brackets < 0; ++brackets) {
            list.add(0, new Element("(", Element.ElementType.BRACKET));
        }
    }

    private void normaliseLetters(List<Element> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            Element e = list.get(i);
            if (e.type == FUNCTION) {
                if (!helper.hasName(e.symbol)) {
                    list.remove(i);
                    for (int j = e.symbol.length() - 1; j >= 0; --j) {
                        String s = e.symbol.substring(j);
                        if (helper.hasName(s) || helper.isVar(s)) {
                            Element nE = new Element(s,
                                    (helper.funcs.getFunc(s) != null) ? FUNCTION :
                                            (helper.consts.getConst(s) != null) ? CONSTANT :
                                                    VAR);
                            list.add(i, nE);
                            e.symbol = e.symbol.substring(0, j);
                        }
                    }
                    if (e.symbol.length() != 0) {
                        throw new RuntimeException("Couldn't understand symbol '" + e + "'");
                    }
                }else if(helper.isVar(e.symbol)){
                    e.type = VAR;
                }else if(helper.consts.getConst(e.symbol) != null){
                    e.type = CONSTANT;
                }
            }
        }
    }

    private void findUnarySigns(List<Element> list){
        Element last = list.get(0);
        if(last.type == Element.ElementType.SIGN && helper.signs.getSign(last.symbol).canBeUnary){
            last.type = FUNCTION;
        }
        for(int i = 1; i < list.size(); ++i){
            Element e = list.get(i);
            if(e.type == Element.ElementType.SIGN && helper.signs.getSign(e.symbol).canBeUnary &&
                    (helper.brackets.isBracket(last.symbol) && helper.brackets.brOpens(last.symbol)
                            || last.type == Element.ElementType.DIVIDER || last.type == Element.ElementType.SIGN)){
                e.type = FUNCTION;
            }
            last = e;
        }
    }

    private void addMissingSigns(List<Element> list) {
        Sign<T> sign = helper.getMissingSign();
        if (sign == null)
            return;
        Element last = list.get(list.size() - 1);
        for (int i = list.size() - 2; i >= 0; --i) {
            Element element = list.get(i);
            boolean needSign = false;
            switch (element.type){
                case BRACKET:
                    if(helper.brackets.brOpens(element.symbol))
                        break;
                case CONSTANT:
                case VAR:
                case NUMBER:
                    needSign = last.type == CONSTANT || last.type == FUNCTION || last.type == NUMBER || last.type == VAR || openBracket(last);
                    break;
            }
            if (needSign) {
                list.add(i + 1, new Element(String.valueOf(sign.name), SIGN));
            }
            last = element;
        }
    }
    private Stack<Element> toStack(List<Element> list) {
        Stack<Element> stack = new Stack<>();
        while (!list.isEmpty()) {
            stack.push(list.remove(list.size() - 1));
        }
        return stack;
    }
    private void simpleCheck(List<Element> list){
        int needArgs = 0;

        int valsCount = 0;

        for (Element e : list) {
            switch (e.type) {
                case SIGN:
                    needArgs += 2;
                    ++valsCount;
                    break;
                case FUNCTION:
                    needArgs += helper.funcs.getFunc(e.symbol).argcount();
                case VAR:
                case NUMBER:
                case CONSTANT:
                    ++valsCount;
                    break;
            }
        }
        int returns = valsCount - needArgs;
        if(returns < 1)
            throw new RuntimeException("Too many functions!");
        else if(returns > 1)
            throw new RuntimeException("Too many arguments!");
    }
    private boolean openBracket(Element e){
        return e.type == BRACKET && helper.brackets.brOpens(e.symbol);
    }
}
