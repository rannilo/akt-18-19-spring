package week1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AKTKi {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new RuntimeException("Programmile tuleb anda 1 argument");
        }

        File file = new File(args[0]);
        Scanner sc = new Scanner(file, "UTF-8");

        Map<String, Integer> variables = new HashMap<>();

        while (sc.hasNextLine()) {
            String line = removeComment(sc.nextLine());

            if (line.startsWith("print ")) {
                String expression = line.substring(5);
                System.out.println(evaluateExpression(expression, variables));
            }

            else if (line.contains("=")) {
                String[] parts = line.split("=");
                if (parts.length != 2) {
                    throw new RuntimeException("Vigane omistuslause");
                }
                String varName = parts[0].trim();
                if (varName.length() != 1) {
                    throw new RuntimeException("Muutujanimed peavad olema 1-tähelised");
                }
                if (!isLatinLetter(varName.charAt(0))) {
                    throw new RuntimeException("Muutujanimed pidi olema ladina tähestikust.");
                }
                String expression = parts[1];
                variables.put(varName, evaluateExpression(expression, variables));
            }

            else if (!line.trim().isEmpty()) {
                throw new RuntimeException("Arusaamatu rida: " + line);
            }
        }

        sc.close();
    }


    private static String removeComment(String s) {
        if (s.contains("#")) return s.substring(0, s.indexOf('#'));
        else return s;
    }

    private static int evaluateExpression(String e, Map<String, Integer> variables) {
        // Teisendame näiteks "a +b-c" kujule "a + b + -c", et saaks arve lihtsalt kokku liita!
        // (Me siin eeldame, et esialgses avaldises on ainult märgita täisarvud.)
        e = e.replace("+", " + ").replace("-", " + -");
        String[] summands = e.split("\\+");

        int sum = 0;
        for (String summand : summands) {
            sum += evaluateSummand(summand.trim(), variables);
        }

        return sum;
    }

    /*
     * See funktsioon võtab argumendiks märgita või miinusega täisarvu või muutujanime
     */
    private static int evaluateSummand(String s, Map<String, Integer> variables) {
        int sign = 1;
        if (s.startsWith("-")) {
            sign = -1;
            s = s.substring(1).trim();
        }
        // proovime täisarvuna tõlgendada, muidu peaks olema muutuja:
        try {
            return Integer.parseInt(s) * sign;
        } catch (NumberFormatException e) {
            if (variables.containsKey(s)) return variables.get(s) * sign;
            else throw new RuntimeException("Muutuja " + s + " pole defineeritud");
        }
    }

    // https://stackoverflow.com/questions/9154905/checking-if-character-is-a-part-of-latin-alphabet
    private static boolean isLatinLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

}
