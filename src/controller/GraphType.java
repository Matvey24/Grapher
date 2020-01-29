package controller;

public enum GraphType {
    FUNCTION, PARAMETER, IMPLICIT;
    public static final String[] titles = {"Function y(x) or x(y)", "Parametric x(t); y(t)", "Implicit z(x,y)=0 or z(x,y)"};
}
