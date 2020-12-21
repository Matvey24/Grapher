package calculator2.calculator;

import calculator2.calculator.helpers.Helper;
import calculator2.calculator.util.actions.Sign;

import java.util.*;

import static calculator2.calculator.CalcLanguage.PARSER_ERRORS;
import static calculator2.calculator.Element.ElementType.*;

public class Parser<T> {
    private final StringBuilder sb;
    private final Helper<T> helper;
    private Stack<Element> stack;
    private final List<String> varNames;
    private final Stack<Integer> ints;
    private final Stack<String> bracketCheck;

    public Parser(Helper<T> helper) {
        this.helper = helper;
        sb = new StringBuilder();
        varNames = new Stack<>();
        ints = new Stack<>();
        bracketCheck = new Stack<>();
    }

    public int parse(String str) {
        str = str.replaceAll("[ \\n\\t]", "");
        List<Element> list = toList(str);
        normaliseBrackets(list);
        normaliseLetters(list);
        findUnarySigns(list);
        addMissingSigns(list);
        int size = calculateVars(list);
        this.stack = toStack(list);
        return size;
    }

    private Element.ElementType type(char c) {
        Element.ElementType type;
        if (helper.isType(c)) {
            type = NUMBER;
        } else if (helper.brackets.isBracket(c)) {
            type = BRACKET;
        } else if (helper.getSign(c) != null) {
            type = SIGN;
        } else if (helper.isDivider(c)) {
            type = DIVIDER;
        } else if (helper.brackets.brParam(c)) {
            type = LAMBDA_PARAM;
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
        for (int i = 0; i < list.size(); ++i) {
            Element e = list.get(i);
            if (e.type == BRACKET) {
                if (helper.brackets.brOpens(e.symbol)) {
                    bracketCheck.push(helper.brackets.getOpposit(e.symbol));
                } else {
                    if (bracketCheck.empty()) {
                        list.add(0, new Element(helper.brackets.getOpposit(e.symbol), BRACKET));
                        ++i;
                    } else if (!bracketCheck.pop().equals(e.symbol)) {
                        throw new RuntimeException(String.format(PARSER_ERRORS[4], PARSER_ERRORS[5]));
                    }
                }
            }
        }
        while (!bracketCheck.empty()) {
            list.add(new Element(bracketCheck.pop(), BRACKET));
        }
    }

    private void normaliseLetters(List<Element> list) {
        sb.setLength(0);
        for (int i = list.size() - 1; i >= 0; --i) {
            Element e = list.get(i);
            if (e.type == FUNCTION) {
                if (!helper.hasName(e.symbol)) {
                    list.remove(i);
                    for (int j = e.symbol.length() - 1; j >= 0; --j) {
                        String s = e.symbol.substring(j);
                        if (helper.hasName(s)) {
                            Element nE = new Element(s,
                                    helper.isVar(s) ? VAR :
                                            helper.isConst(s) ? CONSTANT :
                                                    helper.isLambda(s) ? LAMBDA :
                                                            FUNCTION);
                            list.add(i, nE);
                            e.symbol = e.symbol.substring(0, j);
                        }
                    }
                    if (e.symbol.length() != 0) {
                        throw new RuntimeException(String.format(PARSER_ERRORS[0], e.symbol));
                    }
                } else if (helper.isVar(e.symbol)) {
                    e.type = VAR;
                } else if (helper.isConst(e.symbol)) {
                    e.type = CONSTANT;
                } else if (helper.isLambda(e.symbol)) {
                    e.type = LAMBDA;
                }
            }
        }
    }

    private void findUnarySigns(List<Element> list) {
        Element last = list.get(0);
        if (last.type == SIGN && helper.getSign(last.symbol).canBeUnary) {
            last.type = FUNCTION;
        }
        for (int i = 1; i < list.size(); ++i) {
            Element e = list.get(i);
            if (e.type == SIGN && helper.getSign(e.symbol).canBeUnary &&
                    (last.type == BRACKET && helper.brackets.brOpens(last.symbol)
                            || last.type == DIVIDER
                            || last.type == SIGN)) {
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
            switch (element.type) {
                case BRACKET:
                    if (helper.brackets.brOpens(element.symbol))
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

    public void simpleCheck(List<Element> stack) {
        int returns = 0;
        if (!ints.empty())
            ints.clear();
        for (int i = 0; i < stack.size(); ++i) {
            Element e = stack.get(i);
            switch (e.type) {
                case SIGN:
                    returns -= 1;
                    break;
                case LAMBDA:
                    if (i > 0 && stack.get(i - 1).type == BRACKET && helper.brackets.brOpens(stack.get(i - 1).symbol)) {
                        returns += 2;
                    } else {
                        returns += 1;
                    }
                    break;
                case FUNCTION:
                    int args = helper.getFunc(e.symbol).args;
                    returns -= args;
                    if ((args > 1 || args == -1)
                            && (i == 0
                            || stack.get(i - 1).type != BRACKET
                            || !helper.brackets.brOpens(stack.get(i - 1).symbol))) {
                        throw new RuntimeException(String.format(PARSER_ERRORS[4], "'" + e.symbol + "'"));
                    }
                case VAR:
                case NUMBER:
                case CONSTANT:
                    returns++;
                    break;
                case BRACKET:
                    if (!helper.brackets.brOpens(e.symbol)) {
                        ints.push(returns);
                        returns = 0;
                    } else {
                        if (i < stack.size() - 1) {
                            Element e1 = stack.get(i + 1);
                            if (e1.type == FUNCTION || e1.type == LAMBDA) {
                                int needArgs;
                                if (e1.type == FUNCTION) {
                                    needArgs = helper.
                                            getFunc(e1.symbol).args;
                                } else {
                                    needArgs = -1;
                                }
                                if (needArgs != -1) {
                                    if (returns < needArgs)
                                        throw new RuntimeException(String.format(PARSER_ERRORS[1], PARSER_ERRORS[2], "'" + e1.symbol + "'"));
                                    else if (returns > needArgs)
                                        throw new RuntimeException(String.format(PARSER_ERRORS[1], PARSER_ERRORS[3], "'" + e1.symbol + "'"));
                                } else {
                                    e.symbol += returns;
                                }
                                returns = ints.pop() + needArgs;
                                break;
                            }
                        }
                        if (returns < 1)
                            throw new RuntimeException(String.format(PARSER_ERRORS[1], PARSER_ERRORS[2], PARSER_ERRORS[5]));
                        else if (returns > 1)
                            throw new RuntimeException(String.format(PARSER_ERRORS[1], PARSER_ERRORS[3], PARSER_ERRORS[5]));
                        returns = ints.pop() + 1;
                    }
            }
        }
        if (returns < 1)
            throw new RuntimeException(PARSER_ERRORS[2]);
        else if (returns > 1)
            throw new RuntimeException(PARSER_ERRORS[3]);
    }

    private int calculateVars(List<Element> list) {
        int level = 0;
        varNames.clear();
        sb.setLength(0);
        for (int i = 0; i < list.size(); ++i) {
            Element element = list.get(i);
            if (element.type == BRACKET && helper.brackets.brLambda(element.symbol)) {
                if (helper.brackets.brOpens(element.symbol)) {
                    ++level;
                    if (i == 0 || list.get(i - 1).type != LAMBDA_PARAM) {
                        list.add(i, new Element("", LAMBDA_PARAM));
                        ++i;
                    }
                } else {
                    --level;
                }
            } else if ((element.type == VAR || element.type == LAMBDA) && level == 0) {
                String name = element.symbol;
                if (!varNames.contains(name)) {
                    varNames.add(name);
                }
            } else if (element.type == LAMBDA_PARAM) {
                list.remove(i);
                if(list.size() <= i)
                    throw new RuntimeException(String.format(PARSER_ERRORS[6], element.symbol));
                while (list.get(i).type != LAMBDA_PARAM) {
                    Element e = list.get(i);
                    if (e.type == VAR || e.type == LAMBDA) {
                        sb.append(e.symbol).append(",");
                        if (!varNames.contains(e.symbol) && level == 0) {
                            varNames.add(e.symbol);
                        }
                    } else if (e.type != DIVIDER) {
                        throw new RuntimeException(String.format(PARSER_ERRORS[6], e.symbol));
                    }
                    list.remove(i);
                }
                list.get(i).symbol = sb.toString();
                sb.setLength(0);
            }
        }
        return varNames.size();
    }

    public void findEndOf(StringToken e) {
        if (helper.type == null) {
            e.text = "";
            return;
        }
        sb.setLength(0);
        String line = e.text;
        e.text = "";
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(line.length() - i - 1);
            if (c != ' ' && c != '\t' && type(c) == FUNCTION) {
                sb.append(c);
            } else {
                break;
            }
        }
        String name = sb.reverse().toString();
        sb.setLength(0);
        if (name.isEmpty())
            return;
        if (helper.isConst(name) || helper.getFunc(name) != null) {
            Set<String> set;
            if (helper.isConst(name))
                set = helper.type.consts.keySet();
            else
                set = helper.type.funcs.keySet();
            Iterator<String> it = set.stream()
                    .sorted()
                    .iterator();
            String first = null;
            while (it.hasNext()) {
                String s = it.next();
                if (s.length() == 1 && helper.getSign(s) != null)
                    continue;
                if (first == null)
                    first = s;
                if (s.equals(name)) {
                    if (it.hasNext())
                        first = it.next();
                    break;
                }
            }
            if (first != null) {
                e.text = first;
                e.replace = name.length();
            }
            return;
        }
        String t = helper.type.consts.keySet()
                .stream()
                .sorted()
                .filter((s) -> s.startsWith(name))
                .findFirst()
                .orElse("");
        if (!t.isEmpty()) {
            e.text = t.substring(name.length());
            return;
        }
        t = helper.type.funcs.keySet()
                .stream()
                .sorted()
                .filter(s -> s.startsWith(name))
                .findFirst()
                .orElse("");
        if (t.isEmpty())
            return;
        e.text = t.substring(name.length());
    }

    public static class StringToken {
        public String text;
        public int replace;
    }

    private boolean openBracket(Element e) {
        return e.type == BRACKET && helper.brackets.brOpens(e.symbol);
    }

    public Stack<Element> getStack() {
        return stack;
    }
}
