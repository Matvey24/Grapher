package calculator2.calculator;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.calculator.helpers.Helper;
import calculator2.calculator.util.AbstractType;

import java.util.ArrayList;
import java.util.Stack;

public class Director<T> {
    private final Calculator<T> calculator;
    private final Parser<T> parser;
    private final Helper<T> helper;

    private Expression<T> tree;
    private ArrayList<Variable<T>> vars;

    public Director() {
        helper = new Helper<>();
        calculator = new Calculator<>(helper);
        parser = new Parser<>(helper);
    }
    public void setType(AbstractType<T> type){
        helper.setType(type);
    }
    public int parse(String str) {
        return parser.parse(str);
    }

    public void update(Stack<Element> stack) {
        parser.simpleCheck(stack);
        calculator.clear();
        while (!stack.empty()) {
            calculator.next(stack.pop());
        }
        calculator.next(null);
        tree = calculator.getExpression();
        vars = calculator.getVars();
    }

    public Stack<Element> getStack() {
        return parser.getStack();
    }

    public ArrayList<Variable<T>> getVars() {
        return vars;
    }

    public Expression<T> getTree() {
        return tree;
    }

    public Helper<T> getHelper() {
        return helper;
    }
}
