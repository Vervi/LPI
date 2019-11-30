# CISC 7120 Project

The following defines a simple language, in which a program consists of assignments and each variable is assumed to be of the integer type. For the sake of simplicity, only operators that give integer values are included. Write an interpreter for the language in a language of your choice. Your interpreter should be able to do the following for a given program: (1) detect syntax errors; (2) report uninitialized variables; and (3) perform the assignments if there is no error and print out the values of all the variables after all the assignments are done.

<p>
Program:
<ul>	Assignment* </ul>
 </p> 
 <p>
Assignment:
<ul>
    Identifier = Exp;
    </ul></p>
  <p>  
Exp:<ul>
	Exp + Term | Exp - Term | Term
  </ul></p>
  <p>
Term:<ul>
	Term * Fact  | Fact
  </ul></p>
  <p>
Fact: <ul>
	( Exp ) | - Fact | + Fact | Literal | Identifier
  </ul></p>
  <p>
Identifier: <ul>
     	Letter [Letter | Digit]*
      </ul></p>
      <p>
Letter: <ul>
	a|...|z|A|...|Z|_
  </ul></p>
  <p>
Literal:<ul>
	0 | NonZeroDigit Digit*
  </ul></p>
  <p>
NonZeroDigit:<ul>
	1|...|9
  </ul></p>
  <p>
  Digit: <ul>
	0|1|...|9
  </ul></p>

<br>
Sample inputs and outputs
</br>
<p>
Input 1:<br> </br>
x = 001;<br> </br>
Output 1:<br> </br>
error<br> </br>
</p>

<p>
Input 2:<br> </br>
x_2 = 0;<br> </br>
Output 2:<br> </br>
x_2 = 0<br> </br>
</p>

<p>
Input 3:<br> </br>
x = 0<br> </br>
y = x;<br> </br>
z = ---(x+y);<br> </br>
Output 3:<br> </br>
error<br> </br>
</p>

<p>
Input 4:<br> </br>
x = 1;<br> </br>
y = 2;<br> </br>
z = ---(x+y)*(x+-y);<br> </br>
Output 4:<br> </br>
x = 1<br> </br>
y = 2<br> </br>
z = 3<br> </br>
</p>
