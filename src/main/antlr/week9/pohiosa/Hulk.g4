grammar Hulk;
@header { package week9.pohiosa; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : programm EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
programm
    : 'implementeeri mind!'
    ;