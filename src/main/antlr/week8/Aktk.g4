grammar Aktk;
@header { package week8; }

programm
    :   lauseteJada
    ;

lauseteJada
    :   lause (';' lause)*
    ;

lause
    :   ifLause
    |   whileLause
    |   omistamine
    |   muutujaDeklaratsioon
    |   avaldis
    |	funktsiooniDefinitsioon
    |	tagastuslause
    |   '{' lauseteJada '}'
    ;

ifLause
    :   'if' avaldis 'then' lause 'else' lause
    ;

whileLause
    :   'while' avaldis 'do' lause
    ;

tagastuslause
	:	'return' avaldis
	;

omistamine
    :   Nimi '=' avaldis
    ;

muutujaDeklaratsioon
    :   'var' Nimi (':' Nimi)? ('=' avaldis)?
    ;

funktsiooniDefinitsioon
    : 	'fun' funktsiooninimi=Nimi
			'(' (parameetriNimi+=Nimi ':' parameetriTyyp+=Nimi (',' parameetriNimi+=Nimi ':' parameetriTyyp+=Nimi)*)? ')' ('->' tagastustyyp=Nimi)?
			'{' lauseteJada '}'
	;

avaldis
    :   avaldis5
    ;

avaldis5
    :   avaldis4
            ('>'|'<'|'>='|'<='|'=='|'!=') avaldis4      # Vordlemine
    |   avaldis4                                        # TriviaalneAvaldis5
    ;

avaldis4
    :   avaldis4 ('+'|'-') avaldis3                     # LiitmineLahutamine
    |   avaldis3                                        # TriviaalneAvaldis4
    ;

avaldis3
    :   avaldis3 ('*'|'/'|'%') avaldis2                 # KorrutamineJagamine
    |   avaldis2                                        # TriviaalneAvaldis3
    ;

avaldis2
    :   '-' avaldis2                                    # UnaarneMiinus
    |   avaldis1                                        # TriviaalneAvaldis2
    ;

avaldis1
    :   Nimi '(' (avaldis (',' avaldis)*)? ')'   # FunktsiooniValjakutse
    |   avaldis0                                        # TriviaalneAvaldis1
    ;

avaldis0
    :   Nimi            # MuutujaNimi      // Lisasin nende märgendite nimede
    |   Arv             # Arvuliteraal     // lõppu R, et need ei langeks kokku
    |   Sone            # Soneliteraal     // lekseri reeglite nimedega.
    |   '(' avaldis ')' # Suluavaldis
    ;

Nimi
    :   [a-zA-Z][a-zA-Z0-9_]*
    ;

Arv
    :   ('0'|[1-9][0-9]*)
    ;

Sone
    :   '"' ~["\n\r]* '"' // Tildega saab väljendada eitust.
    ;                     // Siin ~["\n\r] tähistab suvalist tähte
                          // mis pole jutumärk ega reavahetuse sümbol.

Kommentaar
    :   '/*' .*? '*/' -> skip
    ;

Whitespace
    :   [ \t\r\n]+ -> skip
    ;
