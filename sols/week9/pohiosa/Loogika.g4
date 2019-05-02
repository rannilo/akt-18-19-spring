grammar Loogika;
@header { package week9.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : avaldis EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
avaldis
    : avaldis 'JA' avaldis                              # Op
    | avaldis 'VOI' avaldis                             # Op
    | avaldis 'NING' avaldis                            # Op
    | 'KUI' avaldis 'SIIS' avaldis ('MUIDU' avaldis)?   # KuiSiis
    | lihtneAvaldis '=' lihtneAvaldis                   # Op
    | lihtneAvaldis                                     # Triviaalne
    ;

lihtneAvaldis
    : Muutuja                                           # Muutuja
    | Arv                                               # Literaal
    | '(' avaldis ')'                                   # Sulud
    ;

Muutuja : [a-zA-Z]+;
Arv : [01];

WS : [ \t] -> skip;
//</sol>