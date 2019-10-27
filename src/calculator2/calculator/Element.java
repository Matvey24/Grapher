package calculator2.calculator;

public class Element {
    String symbol;
    ElementType type;

    Element(String symbol, ElementType type) {
        this.symbol = symbol;
        this.type = type;
    }

    @Override
    public String toString() {
        return symbol + ' ' + type;
    }
    public enum ElementType{
        NUMBER, FUNCTION, VAR, SIGN, DIVIDER, BRACKET, CONSTANT
    }
}
