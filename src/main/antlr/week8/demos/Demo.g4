grammar Demo;
@header { package week8.demos; }

init: test1 | test2;

test1
    : (Kolm|Neli)*
    ;
test2: (kolm|neli)*;

Kolm: 'a' 'a' 'a';
Neli: 'a' 'a' 'a' 'a';

neli: 'b' 'b' 'b' 'b';
kolm: 'b' 'b' 'b';


WS: [ \t\r\n]+ -> skip;
