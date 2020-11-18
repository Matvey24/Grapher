package calculator2.calculator.executors;

import calculator2.calculator.executors.actors.Expression;

public class Variable<T> implements Expression<T> {
    private String name;
    private T value;
    public Variable(){}
    public Variable(String name, T value){
        this.name = name;
        this.value = value;
    }
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
    public void free() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable<?> variable = (Variable<?>) o;
        return name.equals(variable.name);
    }
    @Override
    public String toString() {
        return getName();
    }
}
