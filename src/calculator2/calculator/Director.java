package calculator2.calculator;

import calculator2.calculator.executors.Expression;
import calculator2.calculator.executors.Variable;
import calculator2.calculator.helpers.Helper;
import calculator2.values.util.AbstractType;

import java.util.ArrayList;
import java.util.Stack;

public class Director<T> {
    private Calculator<T> calculator;
    private Parser<T> parser;
    private Helper<T> helper;

    private Expression<T> tree;
    private ArrayList<Variable<T>> vars;
    public Director(AbstractType<T> type){
        helper = new Helper<>(type);
        calculator = new Calculator<>(helper);
        parser = new Parser<>(helper);
    }
    public void renewType(AbstractType<T> type){
        helper.renewType(type);
    }
    public void update(String str){
        Stack<Element> stack = parser.parse(str);
        calculator.clear();
        while (!stack.empty())
            calculator.next(stack.pop());
        calculator.next(null);
        tree = calculator.getExpression();
        vars = calculator.getVars();
    }
    public T calculate(){
        return tree.calculate();
    }
    public ArrayList<Variable<T>> getVars(){
        return vars;
    }

    public Expression<T> getTree() {
        return tree;
    }

    public Helper<T> getHelper() {
        return helper;
    }
}
