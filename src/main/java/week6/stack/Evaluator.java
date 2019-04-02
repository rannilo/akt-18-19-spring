package week6.stack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {

    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        String currentToken = null;
        for (char c : input.toCharArray()) {
            if (c == '#') break;
            if (c == '+' || c == '-' || c == '=' || c == '(' || c == ')' || c == ' ') {
                if (currentToken != null) {
                    tokens.add(currentToken);
                    currentToken = null;
                }
                if (c != ' ') tokens.add(Character.toString(c));
            } else {
                if (currentToken != null) currentToken += c;
                else currentToken = Character.toString(c);
            }
        }
        if (currentToken != null) tokens.add(currentToken);
        return tokens;
    }

    public static int compute(List<String> tokens, Map<String, Integer> env) {
        EvalMachine machine = new EvalMachine(env);
        int result = 0;
        for (String token : tokens) result = machine.compute(token);
        return result;
    }

    public static Node createAst(List<String> tokens) {
        AstMachine machine = new AstMachine();
        Node result = null;
        for (String token : tokens) result = machine.process(token);
        return result;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> env = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            for (String line; (line = br.readLine()) != null; ) {
                List<String> tokens = tokenize(line);
                if (tokens.size() < 1) continue;
                if (tokens.get(0).equals("print")) System.out.println(compute(tokens.subList(1, tokens.size()), env));
                if (tokens.get(1).equals("=")) env.put(tokens.get(0), compute(tokens.subList(2, tokens.size()), env));
            }
        }
    }
}
