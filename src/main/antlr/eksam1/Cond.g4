grammar Cond;
@header { package eksam1; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : 'implementeeri mind!'
    ;

// Siin soovitame tühjust ignoreerida:
WS : [ \r\n\t] -> skip;
