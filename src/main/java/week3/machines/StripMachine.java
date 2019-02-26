package week3.machines;

import static week3.machines.StripMachine.States.INI;

public class StripMachine {

    enum States { INI, TAG, QTE, DQT }
    private States state = INI;

    public String process(char c) {
        return Character.toString(c);
    }

    private static String cleanUp(String s) {
        StringBuilder sb = new StringBuilder();
        StripMachine machine = new StripMachine();
        for (char c : s.toCharArray()) sb.append(machine.process(c));
        return sb.toString();
    }

    public static void main(String[] args) {
        String input = "<a href='>'>foo</a>";
        System.out.println(cleanUp(input));
    }

}