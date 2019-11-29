package calculator2.values.util.actions.functions;

import calculator2.calculator.executors.Expression;

public interface MultiFunc<T> {
    T execute(Expression<T>[] a);
}
