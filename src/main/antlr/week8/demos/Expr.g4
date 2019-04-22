grammar Expr;
@header { package week8.demos; }

init: expr;

expr: expr '*' expr
    | expr '+' expr
    | Ident
    | Int
    ;

Ident: ('a'..'z'|'A'..'Z')+ ;
Int: ('0'..'9')+;

WS: [ \t\r\n]+ -> skip;
