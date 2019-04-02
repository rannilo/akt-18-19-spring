package week6.stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class EvalMachine {

    private Deque<Integer> stack;
    private int sign;
    private int sum;
    private Map<String, Integer> env;

    public EvalMachine(Map<String, Integer> env) {
        stack = new ArrayDeque<>();
        sign = 1;
        stack.push(1); // <-- Mida võiks magasini peal jälgida??
        sum = 0;
        this.env = env;
    }

    // Siin on vaja muuta/lisada neli rida, et see töötaks ka sulgudega:
    public int compute(String token) {
        switch (token) {
            case "(":
                break;
            case ")":
                break;
            case "-":
                sign *= -1;
                break;
            case "+":
                break;
            default:
                sum += sign * getValue(token);
                sign = 1;
        }

        return sum;
    }

    private int getValue(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return env.get(token);
        }
    }

}
