package week3.machines;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://courses.cs.ut.ee/2019/AKT/spring/Main/PlusMiinus
public class AktkiMachines {

    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();         // Tühi sõne tähistab seisundit ￢TOK
        for (char c : input.toCharArray()) {
            if (c == '#') break;
            if (c == '+' || c == '-' || c == '=' || c == ' ') {   // lisame siia '=' ja tühikud.
                if (currentToken.length() != 0) {
                    tokens.add(currentToken.toString());          // push
                    currentToken = new StringBuilder();
                }
                if (c != ' ') tokens.add(Character.toString(c));  // echo (v.a. tühikud)
            } else {
                currentToken.append(c);                           // acc
            }
        }
        // Ja lõpuks EOF puhul ka push seisundis TOK:
        if (currentToken.length() != 0) tokens.add(currentToken.toString());
        return tokens;
    }

    public static int compute(List<String> tokens, Map<String, Integer> env) {
        int sign = 1;
        int sum = 0;
        boolean wasValue = false; // was the previous token a value
        for (String token : tokens) {
            switch (token) {
                case "-":
                    sign *= -1;
                    // intentional fallthrough
                case "+":
                    wasValue = false;
                    break;
                default:
                    if (wasValue) throw new RuntimeException("Expected operator!");
                    sum += sign * getValue(token, env);
                    sign = 1;
                    wasValue = true;
            }
        }
        if (!wasValue) throw new RuntimeException("Malformed expression!");
        return sum;
    }

    private static int getValue(String token, Map<String, Integer> env) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return env.get(token);
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> env = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            for (String line; (line = br.readLine()) != null; ) {
                if (line.length() > 0 && line.charAt(0) == ' ') throw new RuntimeException("Begins with space!");
                List<String> tokens = tokenize(line);
                if (tokens.size() < 1) continue;
                String firstToken = tokens.get(0);
                if (firstToken.equals("print")) {
                    System.out.println(compute(tokens.subList(1, tokens.size()), env));
                } else if (tokens.get(1).equals("=")) {
                    if (firstToken.length() > 1) throw new RuntimeException("Too long var!");
                    if (!isLatinLetter(firstToken.charAt(0))) throw new RuntimeException("Nonlatin var name.");
                    env.put(firstToken, compute(tokens.subList(2, tokens.size()), env));
                } else {
                    throw new RuntimeException("Unexpected operation!");
                }
            }
        }
    }

    private static boolean isLatinLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

}
