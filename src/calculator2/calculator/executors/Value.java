package calculator2.calculator.executors;

public class Value<T> implements Expression<T> {
    private T value;
    public void setValue(T value){
        this.value = value;
    }
    @Override
    public T calculate() {
        return value;
    }
    @Override
    public String getName() {
        return value.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
