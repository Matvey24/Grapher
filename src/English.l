<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<entry key = "main">Grapher
settings:
Discretization
Type
titles3:
Function y(x) or x(y)
Parametric x(t): y(t)
Implicit z(x,y)=0 or z(x,y)
Sensitivity
Inequality
Spectrum
Dimension
Duration:FPS
Begin
Boomerang
Language
states:
Converting functions..
Calculating..
+
loaded
updater_errors4:
We need 2 funcs in parametric
Too many vars in
graphic
We need x and y wars in Implicit
calculator_errors6:
in
graphic
function
grammar of
vars of
calculator
parser_errors3:
Undefined symbol
Too many functions
Too many arguments
help3:
Help
Using Help
Calculator help
text_elements3:
Graphics
Functions
Calculator
buttons5:
Update
Settings
Timer
Back
resizers4:
Resize
Abscissa
Ordinate
Return back
help 5
part 3
Make a graphic
Click "+" and write function in the text field.
Then press ENTER.
part 6
Signs:
+, -, *, / standard math signs
^ - power
% - remainder of division
&lt;, >, = return 1 if the expression
is true else return 0
part 16
Functions:
sqrt, cbrt - quadratic and cubic roots
pow - power
exp - exponent, it works faster than 'e^x'
sign - signum of number (1, 0, -1)
lg, ln, ld, log - logarithms
sigm - sigmoid - sigm(x) = 1 / (1 + e^-x)
sin, cos, tg, ctg, arcsin, arccos, arctg, arcctg, arctgTwo
- trigonometric functions
put 'd' after them to convert radians to degrees.
abs - absolute value
floor - max integer less then given
ceil - min integer bigger then given
min, max - min / max number from two given
if(x, y, z) - returns z if x = 0 else y
ifs(x) - returns 0 if x = 0 else 1
part 18
Constants: (SI)
pi - pi value = 3.14
e - exponent = 2.71
g - gravity = 9.8
G - gravitational constant = 6.67 * 10 ^ -11
h - Plank constant = 6.63 * 10 ^ -34
c - speed of light = 3 * 10 ^ 8
eps - permittivity = 8.85 * 10 ^ -12
NA - Avogadro number = 6.02 * 10 ^ 23
R - Gas constant = 8.31
k - Boltzmann constant = 1.38 * 10 ^ -23
EARTH - mass of Earth = 6 * 10 ^ 24
SUN - mass of sun = 2 * 10 ^ 30
PROTON - mass of proton = 1.67 * 10 ^ -27
ELCT - mass of electron = 9.1 * 10 ^ -31
eCHARGE - elementary charge = 1.6 * 10 ^ -19
au - astronomical unit = 1.5 * 10 ^ 11
pc - parsec = 3.1 * 10 ^ 16
part 7
Examples:
sqrt(4) = 2
pow(2, 4) = 16
ln(e) = 1
log(8, 2) = 3
arcsind(1) = 90
arctgTwo(1, 0) = pi / 2
help 10
part 6
How to use?
Enter an example in calculator field and press Enter
If you want, you also can add your
own functions and constants,
You can write them in 'Functions' area,
separate each one by pressing ENTER.
part 2
Constants:
Write name + "=" + write an example.
part 4
How to write your own functions?
This is the same as writing constant.
You can call vars by these names:
x; y; z; t; x + some letter, like x\u9f99.
part 4
Timer:
Write a graphic using time var 'tm',
than click 'Timer' and click 'Run' in opened frame.
You can change some settings in this frame.
part 9
Resize:
Open the app fullscreen and click
this button is to resize graphics view.
'Right' click on the button will change the action:
Abscissa/Ordinate - mouse wheel
rotation/touchPad gestures
will change only abscissa's/ordinate's size.
Return back - left click will change
ordinate's size to abscissa's size.
part 14
Recursion:
You can use recursion, but be careful:
long recursion can produce stack over flow errors.
When you call func like that
"func= ... func(x-1) ... x ..."
x will be changed to x-1 after every
calling func(->(x-1)&lt;-)
it is bad, but it is true.
So don't use x after the same functions
to avoid the same situations, for example:
fact = if(x &lt; 2, 1, fact(x-1) * x)
everytime returns 1 when x is integer
fact = if(x &lt; 2, 1, x * fact(x-1))
is normal factorial when x is integer
part 5
Extra:
'Right' click on '-' button in expression in graphics.
Extra info window will be opened.
You can choose between Function,
Parametric and Implicit there.
part 3
Parametric:
Write expression for x, then put ':' and
then write expression for y, click ENTER
part 9
Implicit:
Inequality:
Write an expression using x, y and grapher
will show color, where this expression is non-zero
Spectrum:
The same as inequality, but grapher will show color
which depends on expression value
from red to purple (rainbow).
Use sensitivity to control rainbow thickness.
part 6
Examples:
num = sqrt(5) - num is a constant
lb = ld(x) - the same as ld(x) = logarithm of x with basis 2
myfunc = sin(x) + cos(y) - use it like myfunc(x,y)
(parameter) 2sin(x):cos(x) - draws ellipse
(Implicit Spectrum) cos(x) + sin(x) - pretty picture
</entry>
</properties>