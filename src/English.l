<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<entry key = "name">Grapher</entry>
<entry key = "type">Type</entry>
<entry key = "titles">Function
Parametric
Implicit
Translation</entry>
<entry key = "settings">Discretization
Sensitivity
View colors
Dimension
Lines per cell
Save picture
Duration:FPS
Boomerang</entry>
<entry key = "colors">Ray spectrum
Infrared imager</entry>
<entry key = "states">Converting
Calculating..
+
loaded
Saved to</entry>
<entry key = "updater_errors">We need 2 funcs in parametric and translation.
Too many vars in
graphic
We need x and y vars in Implicit</entry>
<entry key = "calculator_errors">in
graphic
function
grammar of
vars of
calculator</entry>
<entry key = "parser_errors">Undefined symbol
Too many functions
Too many arguments</entry>
<entry key = "buttons">Begin
Update
Settings
Timer
Back
Save project
Load project</entry>
<entry key = "helpers">Help
Using Help
Calculator help
Extra info</entry>
<entry key = "resizers">Resize
Abscissa
Ordinate
Rescale back</entry>
<entry key = "text_elements">Graphics
Functions
Calculator</entry>
<entry key = "main_settings">Language
Lines &lt;->(pixels)</entry>
<entry key = "help">help 10
part 3
Make a graphic
Click "+" and write function in the text field.
Then press ENTER.
part 6
Timer:
Write a graphic using time var 'tm',
than click 'Timer' and click 'Begin' in opened frame.
You can change some settings in this frame.
You can also do right click on button 'Timer'
to stop or start it.
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
part 3
Extra:
'Right' click on '-' button in expression in graphics.
Extra info window will be opened.
part 4
Parametric:
Using var t
write an expression for x, then put ':' and
then write an expression for y, click ENTER
part 16
Implicit:
Equality:
Grapher will show, where the expression is true
(sensitivity is maximum difference between
two neighboring pixels close to 0)
f(x,y)=g(x,y)
Inequality:
Write an inequality using x, y and grapher
will show color, where this expression is true.
f(x,y)&lt;g(x,y)
Spectrum:
Write a function like z(x, y) and grapher will show color
which depends on expression value
from red to purple (rainbow).
Use sensitivity to control rainbow thickness.
z(x,y)
part 6
Translation:
Translation is moving dot,
Write an expression for x and y, in relation to
original coordinates of dot, divide them by ":",
this is like parametric but use x and y
instead of t.
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
Examples:
f(x)=if(x&lt;1,1,x*f(x-1)) - factorial
2sin(x):cos(x) - draws ellipse
cos(x)*sin(tmx):sin(x)*sin(tmx) - polar r(a)
cos(x) + cos(y) - pretty picture
part 5
Settings:
Changing language in settings.
Save/load grapher project here.
Lines &lt;-> is minimum distance
between lines of net on view.
help 7
part 6
How to use?
Enter an example in calculator field and press Enter
If you want, you also can add your
own functions and constants,
You can write them in 'Functions' area,
separate each one by pressing ENTER.
part 2
Your constants:
Write name + "=" + write an example.
part 4
How to write your own functions?
This is the same as writing constant.
You can call vars by these names:
x; y; z; t; x + some letter, like x\u9f99.
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
exp - exponent (it works faster than 'e^x')
sign - signum of number (1, 0, -1)
lg, ln, ld, log - logarithms
sigm - sigmoid - sigm(x) = 1 / (1 + e^-x)
sin, cos, tg, ctg, arcsin, arccos, arctg, arcctg, arctgTwo
- trigonometric functions,
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
part 10
Examples:
sqrt(4) = 2
pow(2, 4) = 16
ln(e) = 1
log(8, 2) = 3
arcsind(1) = 90
arctgTwo(1, 0) = pi / 2
num = sqrt(5) - num is a constant
lb = ld(x) - the same as ld(x) = logarithm of x with basis 2
myfunc = sin(x) + cos(y) - use it like myfunc(x,y)
help 1
part 13
Warning:
Grapher can answer the incomprehensible
when for it questions are incorrect, for example,
if you write f(x)=g(x,x) and g=x+y
grapher will show error, because will think anyway
that g is the gravity constant.
The following happens too:
f(x)=rx, r=2
Too many arguments!
r is a function, so if there something is
following continuously, grapher think like here:
r(x), but not r*x, and r doesn't have arguments.
xr all is a variable, xra (ra=2) is working good.
</entry>
</properties>