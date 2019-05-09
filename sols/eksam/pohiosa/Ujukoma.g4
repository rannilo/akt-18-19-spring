grammar Ujukoma;
@header { package eksam.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : avaldis EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
avaldis
    : intavaldis
    | dblavaldis
    ;

intavaldis
    : intavaldis op='*' intavaldis         #ArithI
    | intavaldis op=('+'|'-') intavaldis   #ArithI
    | '(' intavaldis ')' #ParenI
    | IntLit             #IntLit
    | IntVar             #IntVar
    ;

dblavaldis
    : dblavaldis op=('*'|'/') dblavaldis  #ArithD
    | dblavaldis op=('+'|'-') dblavaldis  #ArithD
    | '(' dblavaldis ')' #ParenD
    | DblLit             #DblLit
    | DblVar             #DblVar
    ;

IntLit : '0'|[1-9][0-9]*;
DblLit : IntLit?'.'[0-9]+;
IntVar : [i-z];
DblVar : [a-h];

// Siin soovitame tühjust ignoreerida:
WS : [ \r\n\t] -> skip;
