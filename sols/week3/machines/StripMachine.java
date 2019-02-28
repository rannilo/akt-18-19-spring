package week3.machines;

import static week3.machines.StripMachine.States.*;

public class StripMachine {

    enum States { INI, TAG, QTE, DQT }
    private States state = INI;

    public String process(char c) {
        // Väljastamine toimub ainult algseisundis!
        boolean echo = state == INI;
        switch (c) {
            case '<':
                if (state == INI) state = TAG;
                return "";
            case '>':
                if (state == TAG) state = INI;
                break;
            case '\'':
                if (state == QTE) state = TAG;
                else if (state == TAG) state = QTE;
                break;
            case '"':
                if (state == DQT) state = TAG;
                else if (state == TAG) state = DQT;
                break;
        }
        return echo ? Character.toString(c) : "";
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