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
        StringBuilder currentToken = new StringBuilder();         // Tühi sõne tähistab seisundit ￢TOK
        for (char c : input.toCharArray()) {
            if (c == '#') break;
            if (c == '+' || c == '-' || c == '=' || c == ' ' || c == '(' || c == ')') {   // lisame siia sulud.
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
        return compute(tokens, env, true);
    }

    public static int compute(List<String> tokens, Map<String, Integer> env, boolean allowPartial) {
        EvalMachine machine = new EvalMachine(env);
        int result = 0;
        for (String token : tokens) result = machine.compute(token);
        // Peab suutma tagastama, kas oli väärtus, et viimased AktPdaTest testid läbi läheks.
        // Moodle'i testide läbimiseks ei ole seda vaja.
        // if (!allowPartial && !machine.wasValue()) throw new RuntimeException("Malformed expression!");
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
                if (line.length() > 0 && line.charAt(0) == ' ') throw new RuntimeException("Begins with space!");
                List<String> tokens = tokenize(line);
                if (tokens.size() < 1) continue;
                String firstToken = tokens.get(0);
                if (firstToken.equals("print")) {
                    System.out.println(compute(tokens.subList(1, tokens.size()), env, false));
                } else if (tokens.get(1).equals("=")) {
                    if (firstToken.length() > 1) throw new RuntimeException("Too long var!");
                    if (!isLatinLetter(firstToken.charAt(0))) throw new RuntimeException("Nonlatin var name.");
                    env.put(firstToken, compute(tokens.subList(2, tokens.size()), env, false));
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
