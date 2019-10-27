package calculator2.calculator.executors;

public interface Expression<T> {
    T calculate();
    String getName();
}
