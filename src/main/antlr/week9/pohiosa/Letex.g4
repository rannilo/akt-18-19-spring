grammar Letex;
@header { package week9.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init
    : avaldis EOF
    ;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
avaldis
    : left=avaldis '-' right=avaldis #Lahutamine
    | 'let' muutujad+=MUUTUJA '=' avaldised+=avaldis (';' muutujad+=MUUTUJA '=' avaldised+=avaldis)* 'in' keha=avaldis #Sidumine
    | 'sum' (MUUTUJA '=' lo+=avaldis 'to' hi+=avaldis) (';' MUUTUJA '=' lo+=avaldis 'to' hi+=avaldis)* 'in' keha=avaldis #Summeerimine
    | MUUTUJA #Muutuja
    | TAISARV #Taisarv
    | '(' avaldis ')' #Sulud
    ;

MUUTUJA : [a-zA-Z]+;
TAISARV : [0-9]+;

WS : [ \n\r\t] -> skip;