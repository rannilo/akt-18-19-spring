grammar Ujukoma;
@header { package eksam.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : avaldis EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
avaldis
    : 'implementeeri mind!'
    ;

// Siin soovitame tühjust ignoreerida:
WS : [ \r\n\t] -> skip;
