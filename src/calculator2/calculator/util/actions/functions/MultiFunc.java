package calculator2.calculator.util.actions.functions;

import calculator2.calculator.executors.Expression;

public interface MultiFunc<T> {
    T execute(Expression<T>[] a);
}
