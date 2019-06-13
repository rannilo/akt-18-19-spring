grammar Bolog;
@header { package eksam2; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : 'implementeeri mind!'
    ;

// Neid soovitame jätta nii:
NL: '\r'? '\n';
WS: [ \t]+ -> skip;