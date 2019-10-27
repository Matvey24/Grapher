package calculator2.values.util.actions;

public class Constant<T> {
    public final String name;
    public final T value;
    public Constant(String name, T val){
        this.name = name;
        this.value = val;
    }
}
