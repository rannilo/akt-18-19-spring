grammar Letex;
@header { package week9.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init
    : avaldis EOF
    ;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
avaldis
    : Arv                                                                          # ArvuLiteraal
    | Muutuja                                                                      # MuutujaNimi
    | '(' avaldis ')'                                                              # SuluAvaldis
    | avaldis '-' avaldis                                                          # Lahutamine
    | 'let' Muutuja '=' avaldis ( ';' Muutuja '=' avaldis )* 'in' body=avaldis     # MuutujaSidumine
    | 'sum' Muutuja '=' lo+=avaldis 'to' hi+=avaldis
      ( ';' Muutuja '=' lo+=avaldis 'to' hi+=avaldis )* 'in' body=avaldis          # Summeerimine
    ;

Muutuja : [a-zA-Z]+;
Arv : [0-9]+;

WS : [ \n\r\t] -> skip;
