package calculator2.values;

import calculator2.values.util.AbstractType;
import calculator2.values.util.actions.Sign;

public class Number extends AbstractType<Double> {
    public Number(){
        addSign('+', (a,b)-> (double)a + b, 1);
        Sign<Double> minus = addSign('-', (a, b)-> a - b, 1);
        Sign<Double> mul = addSign('*', (a, b)-> a * b, 2);
        addSign('/', (a, b)-> a / b, 2);
        addSign('%', (a, b)-> a % b, 2);
        addSign('^', Math::pow, 3);

        functions();
        constants();

        unarySign(minus, a -> -a, 2);
        setMissingSign(mul);
    }
    private void functions(){
        addFunction("sqrt", Math::sqrt, 5);
        addFunction("cbrt", Math::cbrt, 5);
        addFunction("pow", Math::pow, 5);
        addFunction("ln", Math::log, 5);
        addFunction("ld", (a)->Math.log(a) / Math.log(2), 5);
        addFunction("lg", Math::log10, 5);
        addFunction("log",(a,b)-> Math.log(a) / Math.log(b), 5);

        addFunction("sin", Math::sin, 5);
        addFunction("cos", Math::cos, 5);
        addFunction("tg", Math::tan, 5);
        addFunction("ctg", a -> 1 / Math.tan(a), 5);
        addFunction("arctgTwo", Math::atan2, 5);
        addFunction("arcsin", Math::asin, 5);
        addFunction("arccos", Math::acos, 5);
        addFunction("arctg", Math::atan, 5);
        addFunction("arcctg", a->Math.atan(1/a), 5);

        addFunction("abs", a->Math.abs(a), 5);
    }
    private void constants(){
        addConst("pi", Math.PI);
        addConst("e", Math.E);

        addConst("g", 9.8);
        addConst("G", 6.674083e-11);
        addConst("h", 6.6260700408e-34);
        addConst("c", 299792458.0);

        addConst("eps", 8.85418781762039e-12);

        addConst("NA", 6.022140857e23);
        addConst("R", 8.31445984);
        addConst("k", 1.380648528e-23);

        addConst("EARTH", 5.97e24);
        addConst("SUN", 1.9891e30);
        addConst("PROTON", 1.672621777e-27);
        addConst("ELCT", 9.10938291e-31);

        addConst("eCHARGE", 1.602176634e-19);

        addConst("au", 1.495978707e11);
        addConst("pc", 3.09e16);
    }
    @Override
    public Double toValue(String text) {
        return Double.parseDouble(text);
    }
}
