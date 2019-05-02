grammar Hulk;
@header { package week9.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : programm EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
programm
    : lause (Reavahetus lause)*
    ;

lause
    : Hulk op=':=' avaldis ('|' tingimus)?
    | Hulk op=('<-' | '->') elementideList ('|' tingimus)?
    ;

tingimus
    : avaldis 'subset' avaldis                              #Subset
    | Element 'in' avaldis                                  #Incl
    ;

avaldis
    : avaldis ('+' | '&' | '-') avaldis                     #Tehe
    | Hulk                                                  #Muutuja
    | '{' elementideList? '}'                               #Literaal
    | '(' avaldis ')'                                       #Sulud
    ;

elementideList
    : Element (',' Element)*
    ;

Hulk : [A-Z];
Element : [a-z];

Reavahetus : ('\r')?'\n';
WS : [ \t] -> skip;
//</sol>