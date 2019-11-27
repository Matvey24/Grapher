package view.elements;

import framesLib.TextPanel;

import java.util.HashMap;

public class TextViewer {
    private static final String[][] help = {
            {
                "Make a graphic",
                "Click \"+\" and write function in the text field.",
                "Then press ENTER."
            },{
                "Signs:",
                "+, -, *, / standard math signs",
                "^ - power",
                "% - remainder of division",
                "<, > - returns 1 if true else 0"
            },{
                "Functions:",
                "sqrt, cbrt - quadratic and cubic roots",
                "pow - power",
                "lg, ln, ld, log - logarithms",
                "sin, cos, tg, ctg, arcsin, arccos, arctg, arcctg, arctgTwo",
                "- trigonometric functions",
                "abs - absolute value",
                "ift(x, y, z) - returns z if x = 0 else y",
                "if(x) - returns 0 if x = 0 else 1"
            },{
                "Constants: (SI)",
                "pi - pi value = 3.14",
                "e - exponent = 2.71",
                "g - gravity = 9.8",
                "G - gravitational constant = 6.67 * 10 ^ -11",
                "h - Planсk constant = 6.62 * 10 ^ -34",
                "c - speed of light = 3 * 10 ^ 8",
                "eps - permittivity = 8.85 * 10 ^ -12",
                "NA - Avogadro number = 6.02 * 10 ^ 23",
                "R - Gas constant = 8.31",
                "k - Boltzmann constant = 1.38 * 10 ^ -23",
                "EARTH - mass of Earth = 6 * 10 ^ 24",
                "SUN - mass of sun = 2 * 10 ^ 30",
                "PROTON - mass of proton = 1.67 * 10 ^ -27",
                "ELCT - mass of electron = 9.1 * 10 ^ -31",
                "eCHARGE - elementary charge = 1.6 * 10 ^ -19",
                "au - astronomical unit = 1.5 * 10 ^ 11",
                "pc - parsec = 3,1 * 10 ^ 16"
            },{
                "Examples:",
                "sqrt(4) = 2",
                "pow(2, 4) = 16",
                "ln(e) = 1",
                "log(8, 2) = 3"
            }
    };
    private static final String[][] calc_help = {
            {
                "How to use?",
                "Enter an example in field and press Enter",
                "If you want, you also can add your",
                "own functions and constants,",
                "You can write them in \'Functions\' area,",
                "separate each one by pressing ENTER."
            },{
                "How to write your own functions?",
                "Write name + \":\" + how many arguments do you",
                "want to see (3 is max) + \"=\" and write an function",
                "using these arguments."
            },{
                "Constants:",
                "Write name + \"=\" and write an example.",
                "When you write a constant,",
                "you can't use your own functions,",
                "but you can use your other constants,",
                "which have written before."
            },{
                "Timer",
                "Write a graphic using time var 'z', that is a good name,",
                "than click 'Timer' and click 'Run' in opened frame."
            },{
                "Extra:",
                "Right click on '-' button in expression in graphics.",
                "Extra info window will opened.",
                "You can choose between Function and Parameter there."
            },{
                "Parameters:",
                "Write expression for x, than put ':' and ",
                "then write expression for y, click enter"
            },{
                "Examples:",
                "num = sqrt(5) - num is a constant",
                "lb:1= ld(x) - lb(x) = logarithm of x with basis 2",
                "mysin:2=sin(x) + cos(y) - use it like mysin(x,y)",
                "(parameter) 2sin(x):cos(x) - draws ellipse"
            }
    };
    private static final HashMap<String, String[][]> text_panels;
    static{
        text_panels = new HashMap<>();
        text_panels.put("Help", help);
        text_panels.put("Calc_Help", calc_help);
    }
    public static TextPanel openText(String name){
        return new TextPanel(text_panels.get(name));
    }
}
