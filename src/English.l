<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<entry key = "name">Grapher</entry>
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
Boomerang
Feels time</entry>
<entry key = "colors">Ray spectrum
Infrared imager</entry>
<entry key = "states">Converting
Calculating..
+
loaded
Saved to</entry>
<entry key = "updater_errors">We need 2 funcs in parametric and translation.
graphic
We need t or x and y vars in
Too long 'goto' recursion: len</entry>
<entry key = "calculator_errors">in
graphic
function
grammar of
vars of
calculator
Too many vars
constant</entry>
<entry key = "parser_errors">Undefined symbol
Too little arguments
Too many arguments
in brackets
in
Bad location of</entry>
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
Ordinate</entry>
<entry key = "text_elements">Graphics
Functions
Calculator</entry>
<entry key = "main_settings">Language
Lines &lt;->(pixels)
Graphics max count</entry>
<entry key = "help">help 10
part 3
Make a graphic
Click "+" and write function in the text field.
Then press ENTER.
part 6
Timer:
Write a graphic using time var 'tm',
than click 'Timer' and click 'Begin' in the shown panel.
You can change some settings in this panel.
You can also do right click on button 'Timer'
to stop or start it.
part 7
Resize:
'Left' click will change
ordinate's scale to abscissa's scale.
'Right' click on the button will change the action:
Abscissa/Ordinate - mouse wheel rotation
/ touchPad gestures
will change only abscissa's/ordinate's scale.
part 3
Extra:
'Right' click on '-' button in expression in graphics.
Extra info panel will be shown.
part 4
Parametric:
Using var t
write an expression for x, then put ':' and
then write an expression for y, click ENTER
part 17
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
from blue to red like in infrared imager, or
from red to purple like in rainbow.
Use sensitivity to control rainbow thickness.
z(x,y)
part 6
Translation:
Translation is moving dot,
Write expressions for x and y, in relation to
original coordinates of dot, divide them by ":",
this is like parametric but use x and y
instead of t.
part 3
Recursion:
You can use recursion, but be careful:
long recursion can produce stack over flow errors.
part 5
Examples:
f(x)=if(x&lt;1,1,x*f(x-1)) - factorial
2sin(t):cos(t) - draws ellipse
cos(t)*sin(tm*t):sin(t)*sin(tm*t) - polar r(a)=sin(tm*a)
cos(x) + cos(y) - pretty picture
part 7
Settings:
Changing language in settings.
Save/load grapher project here.
Lines &lt;-> is minimum distance
between lines of net on view.
Graphics max count - number of visible
functions of graphics.
help 7
part 8
How to use?
Enter an example in calculator field and press Enter.
Do 'right' click on calculator field to compress
Grapher frame.
If you want, you also can add your
own functions and constants,
You can write them in 'Functions' area,
separate each one by pressing ENTER.
part 5
Your constants:
On functions area write
name + "=" + write an example.
You can override internal functions and constants
Constants are calculating in order they are written.
part 5
How to write your own functions?
Write the name + "=" + expression.
You can call vars by these names:
x; y; z; t; x + some letter, like x龙.
Lines which doesn't contain '=' are commentaries.
part 6
Signs:
+, -, *, / standard math signs
^ - power
% - remainder of division
&lt;, >, = return 1 if the expression
is true else return 0
part 19
Functions:
sqrt, cbrt - quadratic and cubic roots
pow - power
exp - exponent (it works faster than 'e^x')
sign - signum of number (1, 0, -1)
lg, ln, ld, log - logarithms
sigm - sigmoid - sigm(x) = 1 / (1 + e^-x)
hypot - hypotenuse - sqrt(x^2 + y^2)
sin, cos, tg, ctg, arcsin, arccos, arctg, arcctg, arctgTwo
- trigonometric functions,
put 'd' after them to convert radians to degrees.
abs - absolute value
floor - max integer less then given
ceil - min integer bigger then given
round - rounding according to mathematical rules
min, max - min / max number from few given
arr(idx, a...) - returns element from given array 'a'
if(x, y, z) - returns z if x = 0 else y
ifs(x) - returns 0 if x = 0 else 1
part 17
Constants: (SI)
pi - pi value = 3.14
e - exponent = 2.71
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
sqrt(4) == 2
pow(2, 4) == 16
ln(e) == 1
log(8, 2) == 3
arcsind(1) == 90
arctgTwo(1, 0) == pi / 2
num = sqrt(5) - num is a constant
lb = ld(x) - the same as ld(x) = logarithm of x with basis 2
myfunc = sin(x) + cos(y) - use it like myfunc(x,y)
help 4
part 11
Warning:
Grapher can answer the incomprehensible
when for it questions are incorrect, for example,
if you write f(x)=rx, r=2
Grapher would answer
Too many arguments!
r is a function, so if there something is
following continuously, grapher think
it is an argument (sinx), and r doesn't
have arguments. xr all is a variable,
xra (ra=2) is working good.
part 4
Feels time:
If "feels time" is turned off,
timer won't update this graphic,
grapher would work faster with hard animations.
part 18
Parameters:
If you create constant with a name "lookX" or "lookY",
Grapher will move camera to this position,
dot (lookX; lookY) will come to the middle of screen.
Create constants "scaleX" or "scaleY"
to change approximation, scaleX/scaleY mean,
than there are scaleX/scaleY pixels in a unit in screen.
You can also use this constants in your formulas
without defining.
If you are using recursive constants, for save value
to next timer iteration, before definition
constants would be 0.
Use parameter 'goto' to change next
calculating constant (constants numbers start with 0)
setting goto means next constant will be on 'goto' position,
goto calculates on 'goto' position.
Use parameter 'gotoLen' to change max count of using 'goto'
per one update or timer cycle.
part 32
Saves:
All saves are in xml format in java properties.
File contains few parameters:
calculator - saved field "Calculator"
functions - field "Functions"
resize_idx - way to resize - number in range 0-3
timer_info - four numbers, like in timer settings:
duration:fps and interval on new line
view_params - four numbers, each on its own line:
offset x, offset y, scale x, scale y
offset x, y - coordinates of left bottom
corner of image
scale x, y - number of pixels of display,
which graphic unit contains
graphs_count - count of graphics -
any natural number or 0
graphic+number - field of graphic
graphic_info+the same number - information of graphic:
1) name of graphic - for example f for f(x),
purport of name is to define color of graphic
2) discretization (natural number > 2)
3) feels time (true/false)
4) type of graphic
(Function/Parametric/Implicit/Translation)
further depends on type of graphic
if it is a Function, nothing,
if it is a Parametric, interval,
Implicit, sensitivity and on next line
type of rainbow(0-rainbow,1-infrared imager)
Translation, count of lines in cell on graphic
if you want, you can add language:
language - name of language, for example, >Русский&lt;.
</entry>
</properties>