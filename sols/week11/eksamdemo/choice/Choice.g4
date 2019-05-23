grammar Choice;
@header { package week11.eksamdemo.choice; }

expr
    : expr op='+' expr  #Add
    | expr op='|' expr  #Choice
    | Int               #int
    | '(' expr ')'      #parens
    ;

Int: ('0'..'9')+;

WS: [ \t\r\n]+ -> skip;
