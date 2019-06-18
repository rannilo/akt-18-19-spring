grammar ExamTest;
@header { package week11.eksamdemo.choice; }

expr: (l1|l2|l3)*;

l1: L1;
l2: L2;
l3: L3;

L1: 'ab'+ 'ba'*;
L2: 'bab'+;
L3: 'baba'+;