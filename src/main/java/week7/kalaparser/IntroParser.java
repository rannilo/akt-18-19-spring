package week7.kalaparser;

import java.io.IOException;

/**
 * Kirjutame ise parseri, et saaks parseri abifunktsioonidest aru!
 * Testide läbimiseks ei pea ASTi tagastama, seega on parse meetodi tüüp esialgu void.
 */

//S->A ; S->B. võtad A ja B first hulga ja võrdled hetke tähega. kui see ei sobi, võtad järgmise jne.
public class IntroParser {

    String sisend;
    private int pc;
    private static char TERMINATOR = '\\';

    public IntroParser(String sisend){
        this.sisend = sisend + TERMINATOR;
        this.pc = 0;
    }

    public static void parse(String input) {
        IntroParser parser = new IntroParser(input);
        parser.s();
        if (!parser.isFinished()) throw new RuntimeException("Not all characters consumed");
    }

    public void s(){
        int i = peek() == 'a' ? 0 : 1;
        switch(i){
            case 0:
                match('a');
                s();
                match('b');
            case 1:
                epsilon();
        }
    }

    private char peek() {
        return sisend.charAt(pc);
    }

    private boolean isFinished(){
        return peek() == TERMINATOR;
    }

    private void match(char karakter) {
        char järgmine = sisend.charAt(pc);
        if(järgmine != karakter){
            throw new RuntimeException();
        } else{
            pc++;
        }
    }

    private void epsilon(){

    }

    private static int a(){
        try{
            throw new IOException(" ");
        }
        catch (RuntimeException ex){
            throw new RuntimeException();
        }
        finally{
            return 1;
        }
    }

    public static void main(String[] args) {
        System.out.println(a());
    }

}
