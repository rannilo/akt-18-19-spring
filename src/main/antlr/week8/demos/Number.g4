grammar Number;
@header { package week8.demos; }

number: digit;

digit: DIGIT;
DIGIT : '0'..'9';
INT   :  DIGIT+;
