grammar Kala;
@header { package week9.pohiosa; }

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init
    : list EOF
    ;

list : '(' elements? ')';

elements : element (',' element)*;


element
    : Muutuja   # Muutuja
    | Null      # Null
    | list      # ListElement
    ;

Null : 'null';

Muutuja : [a-z]+;

WS : [ \t] -> skip;
