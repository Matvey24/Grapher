package calculator2.calculator.executors;

public class Variable<T> implements Expression<T>{
    private String name;
    private T value;
    public void setName(String name){
        this.name = name;
    }
    public void setValue(T value){
        this.value = value;
    }
    @Override
    public String getName(){
        return name;
    }

    @Override
    public T calculate() {
        return value;
    }

    @Override
    public String toString() {
        return getName();
    }
}
