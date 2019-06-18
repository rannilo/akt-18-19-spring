grammar Ujukoma;
@header { package eksam.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : avaldis EOF;

avaldis
    :   taisavaldis
    |   ujuavaldis
    ;

taisavaldis
    : taisavaldis ('*' | '%') taisavaldis #TaisBinOp
    | taisavaldis ('+' | '-') taisavaldis #TaisBinOp
    | TAISMUUTUJA #TaisMuutuja
    | TAISLITERAAL #TaisLiteraal
    | '(' taisavaldis ')' #TaisSulud
    ;

ujuavaldis
    : ujuavaldis ('*' | '%' | '/' ) ujuavaldis #UjuBinOp
    | ujuavaldis ('+' | '-') ujuavaldis #UjuBinOp
    | UJUMUUTUJA #UjuMuutuja
    | UJULITERAAL #UjuLiteraal
    | '(' ujuavaldis ')' #UjuSulud
    ;

TAISMUUTUJA: [i-z];
UJUMUUTUJA: [a-h];

TAISLITERAAL: [0-9] | [1-9][0-9]+;
UJULITERAAL: TAISLITERAAL? '.' [0-9]+;

// Siin soovitame tühjust ignoreerida:
WS : [ \r\n\t] -> skip;
