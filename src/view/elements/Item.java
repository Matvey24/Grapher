package view.elements;

import java.util.Objects;

public class Item {
    public String name;

    public Item(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}