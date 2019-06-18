grammar Test;
@header { package week8.demos; }

init: expr+ EOF;

expr: '<' expr '>'
    | expr ShiftOp expr
    | Var;

ShiftOp: '<<' | '>>';
Var: [a-z]+;

WS: [ \t]+ -> skip;