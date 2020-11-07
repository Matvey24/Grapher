package calculator2.calculator;

import calculator2.calculator.executors.*;
import calculator2.calculator.executors.actors.*;
import calculator2.calculator.helpers.Helper;
import calculator2.calculator.util.actions.Func;
import calculator2.calculator.util.actions.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static calculator2.calculator.Element.ElementType.*;

class Calculator<T> {
    private final Helper<T> helper;
    private final Stack<Expression<T>> values;
    private final Stack<Element> other;
    private final Stack<List<FuncVariable<T>>> lambdas;
    private List<FuncVariable<T>> vars;
    private int nextArgs;

    Calculator(Helper<T> helper) {
        this.helper = helper;
        values = new Stack<>();
        other = new Stack<>();
        vars = new ArrayList<>();
        lambdas = new Stack<>();
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
                nextBracket(e);
                break;
            case DIVIDER:
                count(0);
                break;
            case LAMBDA:
                other.push(e);
                FuncVariable<T> var = findVar(e.symbol);
                if (var != null)
                    return;
                @SuppressWarnings("unchecked")
                Expression<T>[] arr = new Expression[0];
                var = new LambdaParameter<>(arr);
                var.setName(e.symbol);
                var.setValue(helper.def());
                vars.add(var);
                break;
            case VAR:
                var = findVar(e.symbol);
                if (var != null) {
                    values.push(var);
                    return;
                }
                var = new FuncVariable<>();
                var.setName(e.symbol);
                var.setValue(helper.def());
                values.push(var);
                vars.add(var);
                break;
        }
    }

    private FuncVariable<T> findVar(String name) {
        for (FuncVariable<T> var : vars) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        return null;
    }

    private void nextSign(Element e) {
        Sign<T> sign = helper.getSign(e.symbol);
        int prio = 0;
        if (!other.empty()) {
            Element e1 = other.peek();
            if (e1.type == SIGN) {
                prio = helper.getSign(e1.symbol).priority;
            } else if (e1.type == FUNCTION) {
                prio = helper.getFunc(e1.symbol).priority;
            } else if (e1.type == LAMBDA) {
                prio = helper.lambdaPrio();
            }
        }
        if (prio >= sign.priority) {
            count(sign.priority);
        }
        other.push(e);
    }

    private void nextBracket(Element el) {
        boolean opens = helper.brackets.brOpens(el.symbol);
        if (opens) {
            if(helper.brackets.brLambda(el.symbol)) {
                lambdas.push(vars);
                vars = new ArrayList<>();
            }
        }else{
            for (int i = other.size() - 2; i >= 0; --i) {
                Element e = other.elementAt(i);
                if (e.type == BRACKET) {
                    if (i - 1 >= 0) {
                        if (other.elementAt(i - 1).type == SIGN) {
                            Sign<T> s = helper.getSign(other.elementAt(i - 1).symbol);
                            count(s.priority + 1);
                        } else if (other.elementAt(i - 1).type == FUNCTION) {
                            Func<T> f = helper.getFunc(other.elementAt(i - 1).symbol);
                            count(f.priority + 1);
                        } else if (other.elementAt(i - 1).type == LAMBDA) {
                            count(helper.lambdaPrio() + 1);
                        } else if (other.elementAt(i - 1).type == BRACKET) {
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
        if (e.type == SIGN) {
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
        } else if (e.type == BRACKET) {
            boolean opens = helper.brackets.brOpens(e.symbol);
            if (!opens) {
                count(-1);
                count(priority);
            } else {
                if (priority != -1) {
                    other.push(e);
                    return;
                } else if (e.symbol.length() > 1) {
                    nextArgs = Integer.parseInt(e.symbol.substring(1));
                }
                if (helper.brackets.brLambda(e.symbol)) {
                    LambdaInitializer<T> init = new LambdaInitializer<>();
                    init.setValues(values.pop(), vars);
                    values.push(init);
                    vars = lambdas.pop();
                }
            }
        } else if (e.type == FUNCTION) {
            Func<T> f = helper.getFunc(e.symbol);
            if (f.priority >= priority) {
                if (f.args == 1) {
                    Expression<T> e1 = values.pop();
                    if (f.oneFunc != null) {
                        OneActor<T> actor = new OneActor<>();
                        actor.setValues(f.oneFunc, e1, f.name);
                        values.push(actor);
                    } else {
                        UnaryActor<T> actor = new UnaryActor<>();
                        actor.setValues(f.unarFunc, e1, f.name);
                        values.push(actor);
                    }
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
        } else if (e.type == LAMBDA) {
            if (helper.lambdaPrio() >= priority) {
                if (nextArgs == -1 || nextArgs == 0) {
                    nextArgs = -1;
                    //this is initializer
                    LambdaParameter<T> param = (LambdaParameter<T>) findVar(e.symbol);

                    LambdaInitializer<T> init = new LambdaInitializer<>();
                    init.setValues(param, null);
                    values.push(init);
                    count(priority);
                    return;
                }
                int args = nextArgs;
                nextArgs = -1;
                @SuppressWarnings("unchecked")
                Expression<T>[] arr = new Expression[args];
                for (int i = args - 1; i >= 0; --i)
                    arr[i] = values.pop();
                LambdaActor<T> actor = new LambdaActor<>();
                LambdaParameter<T> param = (LambdaParameter<T>) findVar(e.symbol);
                actor.setValues(param, e.symbol, arr);
                values.push(actor);
                count(priority);
            } else {
                other.push(e);
            }
        }
    }

    Expression<T> getExpression() {
        return values.peek();
    }

    List<FuncVariable<T>> getVars() {
        return vars;
    }

    void clear() {
        values.clear();
        other.clear();
        vars.clear();
        nextArgs = -1;
    }
}
