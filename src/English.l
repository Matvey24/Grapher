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
Feels time
Set color</entry>
<entry key="actions">Back
Update
Done
Apply</entry>
<entry key = "colors">Ray spectrum
Infrared imager</entry>
<entry key = "states">Converting
Calculating..
+
loaded
Saved to</entry>
<entry key = "updater_errors">We need 2 funcs in parametric and translation.
We need t or x and y vars in graphic
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
brackets
in
Bad location of
Only vars and lambdas can be in $..$: </entry>
<entry key="version_controller">Can't load log!</entry>
<entry key = "buttons">Begin
Settings
Timer
Save project
Load project</entry>
<entry key = "helpers">Help
Using Help
Calculator help
Extra info
Version log</entry>
<entry key = "resizers">Resize
Abscissa
Ordinate</entry>
<entry key = "text_elements">Graphics
Functions
Calculator</entry>
<entry key = "main_settings">Language
Lines &lt;->(pixels)
Graphics max count</entry>
<entry key = "help">help 11
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
part 8
Lambdas:
Instead of parameter in function write an
an expression for any variable in '{..}', like '{x^2}',
if you use external vars or lambdas here,
write them in '$..$' before brackets, like '$x${x^y}'.
Then in function use 'f' + 'some letter', like function,
for example 'fa(x)', you can send this function in other,
like 'func(fa, 4)' - without brackets.
part 8
Examples:
f(x)=if(x&lt;1,1,x*f(x-1)) - factorial
2sin(t):cos(t) - draws ellipse
cos(t)*sin(tm*t):sin(t)*sin(tm*t) - polar r(a)=sin(tm*a)
cos(x) + cos(y) - pretty picture
in functions:
deriv = (fa(x+d)-fa(x))/d
d = 0.000001 - derivate of function, use like deriv({t^2}, x)
part 9
Settings:
Changing language in settings.
Save/load grapher project here.
Lines &lt;-> is minimum distance
between lines of net on view,
Put '~' before number and
grapher won't show coordinates.
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
part 30
Functions:
sqrt, cbrt - quadratic and cubic roots
pow - power
exp - exponent (it works faster than 'e^x')
sign - signum of number (1, 0, -1)
lg, ln, ld, log - logarithms
sigm - sigmoid - sigm(x) = 1 / (1 + e^-x)
hypot - hypotenuse - sqrt(x^2 + y^2)
sin, cos, tg, ctg,
arcsin, arccos, arctg, arcctg, arctgTwo,
sh, ch, th, cth - trigonometric functions
put 'd' after them to convert radians to degrees.
abs - absolute value
floor - max integer less then given
ceil - min integer bigger then given
round - rounding according to mathematical rules
random() - random value from 0 to 1
min, max - min / max number from few given
arr(idx, a...) - returns element from given array 'a'
returns length of array, if idx = -1
if(x, y, z) - returns z if x = 0 else y
ifs(x) - returns 0 if x = 0 else 1
arrays: the map of arrays of values
set(x, y, val) - sets value in array 'x' on 'y' position,
returns this value
get(x, y) - returns value in array 'x' on 'y' position
for(start, end, func) - executes 'func' - lambda, gives
func the integer index from start to end,
returns sum of returns of func.
assert(x) - makes error with parameter x
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
help 6
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
part 8
Feels time:
If "feels time" is turned off,
timer won't update this graphic,
grapher would work faster with hard animations.
if you want to update this graphic rarely, use
'update_graphic(idx)' function and
grapher will update this graphic on timer iteration
after call of this function.
part 26
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
Use function 'finish' in 'goto = finish' to
finish current iteration,
'finish' returns number of calculating constants.
Use parameter 'gotoLen' to change max count of using 'goto'
per one update or timer cycle.
view_movable - if 0, you can't move view by clicking mouse
buttons (for better view), but you can by moving wheel.
Use functions getMouseX(), getMouseY(), isMousePressed()
to know about mouse. Coords don't update while mouse
is not pressed.
part 5
Lambda in calculator:
Send lambda expression in calculator to get more
than 1 answer. Lambda from -1 has to return count
of answers. Use 'arr' as main func, like '{arr(x, 4, 5)}'.
returns '4.0, 5.0'.
part 21
Hot keys:
Ctrl + S/L - save/load project in/from last used file
Ctrl + Shift + S/L - just save/load project
When you are editing code in functions area,
you can use the following combinations:
Ctrl + Enter - like 'Update' button
Ctrl + Shift + (Up/Down) - move line up or down
Shift + Del - delete line
Ctrl + (Вверх/Вниз) - narrow down/expand area
Tab - in main fields will help (not help)
to finish writing function or constant name.
Ctrl + P - use this combination careful,
It paints graphic on screen by mouse.
How does it work? - paints all lines from
graphic, but it can't paint Implicits and letters (numbers).
Drawing line: moving mouse to the line start,
pressing, moving to the end and releasing.
Ensure, that your app can understand this mechanics.
Graphic left up will be on mouse position
at start of this function, size of graphic will be
the same as in grapher.
part 34
Saves:
All saves are in xml format in java properties.
File contains few parameters:
calculator - saved field "Calculator"
functions - field "Functions"
resize_idx - way to resize - number in range 0-3
timer_info - four numbers, like in timer settings:
duration:fps and interval on new line
view_params - four numbers, each on its own line:
lookX, lookY, scale x, scale y
look x, y - coordinates of center of view
scale x, y - number of pixels of display,
which graphic unit contains
graphs_count - count of graphics -
any natural number or 0
graphic+number - field of graphic
graphic_info+the same number - information of graphic:
1) name of graphic - for example f for f(x),
purport of name is to define color of graphic
there can be also hex value of color,
like 'f ffff0000' - 'name aarrggbb'
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
main_settings - information of the app:
first line - lines_space
second line - language, for example, 'Русский'.
</entry>
</properties>