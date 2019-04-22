grammar Number;
@header { package week8.demos; }

number: INT;
fragment DIGIT : '0'..'9';
INT   :  DIGIT+;
