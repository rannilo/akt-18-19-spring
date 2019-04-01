package week6;

public enum TokenType {
    VARIABLE,                   // Token-i data peab olema muutuja nimi (String)
    STRING, INTEGER, DOUBLE,    // Literaalid. Token-i data peab olema vastav väärtus (String, Integer või Double)
    LPAREN, RPAREN,             // Alustav ja lõpetav ümarsulg. Token-i data peab olema null
    PLUS, MINUS, TIMES, DIV,    // Operaatorid. Token-i data peab olema null
    IF, WHILE, VAR,             // Võtmesõnad. Token-i data peab olema null
    EOF;                        // Token-i data peab olema null
}
