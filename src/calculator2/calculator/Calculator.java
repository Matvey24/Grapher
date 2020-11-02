package calculator2.calculator;

import calculator2.calculator.executors.*;
import calculator2.calculator.helpers.Helper;
import calculator2.calculator.util.actions.Func;
import calculator2.calculator.util.actions.Sign;

import java.util.ArrayList;
import java.util.Stack;

class Calculator<T> {
    private final Helper<T> helper;
    private final Stack<Expression<T>> values;
    private final Stack<Element> other;
    private final ArrayList<Variable<T>> vars;
    private int nextArgs;

    Calculator(Helper<T> helper) {
        this.helper = helper;
        values = new Stack<>();
        other = new Stack<>();
        vars = new ArrayList<>();
    }

    void next(Element e) {
        if (e == null) {
            count(0);
            return;
        }
        Value<T> value;
        switch (e.type) {
            case FUNCTION:
                other.push(e);
                break;
            case SIGN:
                nextSign(e);
                break;
            case CONSTANT:
                values.push(helper.getConst(e.symbol));
                break;
            case NUMBER:
                value = new Value<>();
                value.setValue(helper.toValue(e.symbol));
                values.push(value);
                break;
            case BRACKET:
                other.push(e);
                nextBracket(helper.brackets.brOpens(e.symbol));
                break;
            case DIVIDER:
                count(0);
                break;
            case VAR:
                for (Variable<T> var : vars) {
                    if (var.getName().equals(e.symbol)) {
                        values.add(var);
                        return;
                    }
                }
                Variable<T> var = new Variable<>();
                var.setName(e.symbol);
                values.push(var);
                vars.add(var);
                break;
        }
    }

    private void nextSign(Element e) {
        Sign<T> sign = helper.getSign(e.symbol);
        int prio = 0;
        if (!other.empty()) {
            Element e1 = other.peek();
            if (e1.type == Element.ElementType.SIGN) {
                prio = helper.getSign(e1.symbol).priority;
            } else if (e1.type == Element.ElementType.FUNCTION) {
                prio = helper.getFunc(e1.symbol).priority;
            }
        }
        if (prio >= sign.priority) {
            count(sign.priority);
        }
        other.push(e);
    }

    private void nextBracket(boolean opens) {
        if (!opens) {
            for (int i = other.size() - 2; i >= 0; --i) {
                Element e = other.elementAt(i);
                if (e.type == Element.ElementType.BRACKET) {
                    if (i - 1 >= 0) {
                        if (other.elementAt(i - 1).type == Element.ElementType.SIGN) {
                            Sign<T> s = helper.getSign(other.elementAt(i - 1).symbol);
                            count(s.priority + 1);
                        } else if (other.elementAt(i - 1).type == Element.ElementType.FUNCTION) {
                            Func<T> f = helper.getFunc(other.elementAt(i - 1).symbol);
                            count(f.priority + 1);
                        } else if (other.elementAt(i - 1).type == Element.ElementType.BRACKET) {
                            count(0);
                        }
                    } else {
                        count(0);
                    }
                    return;
                }
            }
        }
    }

    private void count(int priority) {
        if (other.empty())
            return;
        Element e = other.pop();
        if (e.type == Element.ElementType.SIGN) {
            Sign<T> sign = helper.getSign(e.symbol);
            if (sign.priority >= priority) {
                Expression<T> e2 = values.pop();
                Expression<T> e1 = values.pop();
                BinaryActor<T> actor = new BinaryActor<>();
                actor.setValues(sign.function, e1, e2, String.valueOf(sign.name));
                values.push(actor);
                count(priority);
            } else {
                other.push(e);
            }
        } else if (e.type == Element.ElementType.BRACKET) {
            boolean opens = helper.brackets.brOpens(e.symbol);
            if (!opens) {
                count(-1);
                count(priority);
            } else {
                if (priority != -1) {
                    other.push(e);
                } else if (e.symbol.length() > 1) {
                    nextArgs = Integer.parseInt(e.symbol.substring(1));
                }
            }
        } else if (e.type == Element.ElementType.FUNCTION) {
            Func<T> f = helper.getFunc(e.symbol);
            if (f.priority >= priority) {
                if (f.args == 1) {
                    Expression<T> e1 = values.pop();

                    UnaryActor<T> actor = new UnaryActor<>();
                    actor.setValues(f.unarFunc, e1, f.name);
                    values.push(actor);
                } else if (f.args == 2) {
                    Expression<T> e2 = values.pop();
                    Expression<T> e1 = values.pop();

                    BinaryActor<T> actor = new BinaryActor<>();
                    actor.setValues(f.binarFunc, e1, e2, f.name);
                    values.push(actor);
                } else {
                    int args = f.args;
                    if (args == -1) {
                        if (nextArgs == -1) {
                            throw new RuntimeException("NextArgs doesn't exist.");
                        }
                        args = nextArgs;
                        nextArgs = -1;
                    }
                    @SuppressWarnings("unchecked")
                    Expression<T>[] arr = new Expression[args];
                    for (int i = args - 1; i >= 0; --i)
                        arr[i] = values.pop();
                    MultiActor<T> actor = new MultiActor<>();
                    actor.setValues(f.multiFunc, f.name, arr);
                    values.push(actor);
                }
                count(priority);
            } else {
                other.push(e);
            }
        }

    }

    Expression<T> getExpression() {
        return values.peek();
    }

    ArrayList<Variable<T>> getVars() {
        return vars;
    }

    void clear() {
        values.clear();
        other.clear();
        vars.clear();
        nextArgs = -1;
    }
}
