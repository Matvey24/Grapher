package calculator2.calculator.util.actions.functions;

import calculator2.calculator.executors.actors.Expression;

public interface UnarFunc<T> {
    T execute(Expression<T> a);
}
