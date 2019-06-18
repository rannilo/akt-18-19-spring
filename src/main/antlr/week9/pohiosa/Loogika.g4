grammar Loogika;
@header { package week9.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : avaldis EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)

avaldis
    : MUUTUJA
    | LITERAAL
    | loogiline
    | tingimus
    | '(' avaldis ')'
    ;

MUUTUJA
    : [a-zA-Z]+
    ;

LITERAAL
    : '0' | '1'
    ;

tingimus
    : 'KUI' avaldis 'SIIS' avaldis ('MUIDU' avaldis)?
    ;

loogiline
    : loogiline 'NING' loogiline
    | MUUTUJA
    ;