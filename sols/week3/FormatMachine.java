package week3;

public class FormatMachine {

    private enum State {
        INIT, PUNC, MIDW
    }
    private State state = State.INIT;

    private static boolean isPunct(char c) {
        return (c == ',' || c == '.' || c == ':' || c == ';' || c == '!' || c == '?');
    }

    public String process(char c) {
        boolean space = false;
        if (c == '(') {
            space = state != State.INIT;
            state = State.INIT;
        } else if (c == ')' || isPunct(c)) {
            state = State.PUNC;
        } else if (c == '\n') {
            state = State.INIT;
        } else if (c == ' ') {
            if (state == State.MIDW) state = State.PUNC;
            return "";
        } else {
            space = state == State.PUNC;
            state = State.MIDW;
        }
        return (space ? " " : "") + c;
    }
}