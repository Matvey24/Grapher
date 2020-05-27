package calculator2.values;

import calculator2.values.util.AbstractType;
import calculator2.values.util.actions.Sign;
import calculator2.values.util.actions.functions.BinarFunc;
import calculator2.values.util.actions.functions.UnarFunc;

import static java.lang.Math.PI;

public class Number extends AbstractType<Double> {
    private static final BinarFunc<Double> pow = (a, b)->{
        double B = b.calculate();
        if(B == 0)
            return 1d;
        return Math.pow(a.calculate(), B);
    };
    private static final BinarFunc<Double> mul = (a, b)->{
        double A = a.calculate();
        return (A == 0)?0:A * b.calculate();
    };
    private final double ln2 = Math.log(2);
    public Number(){
        addSign('<', (a, b)->((a.calculate() < b.calculate())?1d:0), 1);
        addSign('>', (a, b)->((a.calculate() > b.calculate())?1d:0), 1);
        addSign('=', (a,b)-> ((a.calculate().equals(b.calculate()))?1d:0d), 1);

        addSign('+', (a,b)-> a.calculate() + b.calculate(), 2);
        Sign<Double> minus = addSign('-', (a, b)-> a.calculate() - b.calculate(), 2);
        addSign('*', mul, 3);
        addSign('/', (a, b)-> a.calculate() / b.calculate(), 3);
        addSign('%', (a, b)-> a.calculate() % b.calculate(), 3);
        addSign('^', pow, 4);

        Sign<Double> mulp = addSign('!', mul, 5);

        functions();
        constants();

        unarySign(minus, a -> -a);
        setMissingSign(mulp);
    }
    @SuppressWarnings("RedundantCast")
    private void functions(){
        addFunction("sqrt", Math::sqrt, 10);
        addFunction("cbrt", Math::cbrt, 10);
        addFunction("pow", pow, 10);
        addFunction("exp", Math::exp, 10);
        addFunction("sign", (UnarFunc<Double>)Math::signum, 10);
        addFunction("ln", Math::log, 10);
        addFunction("ld", (a)->Math.log(a) / ln2, 10);
        addFunction("lg", Math::log10, 10);
        addFunction("log",(a,b)-> Math.log(a.calculate()) / Math.log(b.calculate()), 10);
        addFunction("sigm", a -> 1 / (1 + Math.exp(-a)),10);

        addFunction("sin", Math::sin, 10);
        addFunction("cos", Math::cos, 10);
        addFunction("tg", Math::tan, 10);
        addFunction("ctg", a -> 1 / Math.tan(a), 10);
        addFunction("sind", a -> Math.sin(PI / 180 * a), 10);
        addFunction("cosd", a -> Math.cos(PI / 180 * a), 10);
        addFunction("tgd", a -> Math.tan(PI / 180 * a), 10);
        addFunction("ctgd", a -> 1 / Math.tan(PI / 180 * a), 10);
        addFunction("arctgTwo", (a, b)-> Math.atan2(a.calculate(), b.calculate()), 10);
        addFunction("arcsin", Math::asin, 10);
        addFunction("arccos", Math::acos, 10);
        addFunction("arctg", Math::atan, 10);
        addFunction("arcctg", a->Math.atan(1/a), 10);
        addFunction("arcsind", a->Math.asin(a)/PI*180, 10);
        addFunction("arccosd", a->Math.acos(a)/PI*180, 10);
        addFunction("arctgd", a->Math.atan(a)/PI*180, 10);
        addFunction("arcctgd", a->Math.atan(1/a)/PI*180, 10);
        addFunction("arctgTwod", (a,b)->Math.atan2(a.calculate(), b.calculate())/PI*180, 10);


        addFunction("abs", (UnarFunc<Double>) Math::abs, 10);
        addFunction("floor", Math::floor, 10);
        addFunction("ceil", Math::ceil, 10);
        addFunction("round", a->(double)Math.round(a), 10);

        addFunction("min", (a, b) -> Math.min(a.calculate(),b.calculate()), 10);
        addFunction("max", (a, b) -> Math.max(a.calculate(),b.calculate()), 10);

        addFunction("if", (a)->((a[0].calculate() == 0)?a[2].calculate():a[1].calculate()),3,  10);
        addFunction("ifs", a->(a == 0)?0d:1d, 10);
    }
    private void constants(){
        addConst("pi", PI);
        addConst("e", Math.E);

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

        addConst("tm", .0);
    }
    @Override
    public Double toValue(String text) {
        return Double.parseDouble(text);
    }
}
