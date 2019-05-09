package week10;

import java.util.Scanner;

public class AktkInterpreterBuiltins {
    private static Scanner systemInScanner = null;

    // sisend-väljund
    public static void print(String value) {
        System.out.println(value);
    }

    public static void print(Integer value) {
        System.out.println(value);
    }

    /**
     * Töötab umbes nagu Pythoni input
     */
    public static String input(String prompt) {
        System.out.print(prompt);

        if (systemInScanner == null) {
            systemInScanner = new Scanner(System.in);
        }

        return systemInScanner.nextLine();
    }

    public static String input() {
        return input("");
    }

    public static Integer readInt() {
        return stringToInteger(input());
    }

    // loogikatehted
    public static Integer not(Integer x) {
        return ~x;
    }

    public static Integer and(Integer a, Integer b) {
        return a & b;
    }

    public static Integer or(Integer a, Integer b) {
        return a | b;
    }

    // stringi tehted
    public static String upper(String s) {
        return s.toUpperCase();
    }

    public static String lower(String s) {
        return s.toLowerCase();
    }

    // teisendused
    public static String integerToString(Integer x) {
        return String.valueOf(x);
    }

    public static Integer stringToInteger(String x) {
        return Integer.parseInt(x);
    }

    // täisarvude tehted
    /**
     * Astendamine
     */
    public static Integer power(Integer x, Integer n) {
        if (n < 0) {
            throw new IllegalArgumentException("Ainult mittenegatiivne astendaja on lubatud");
        }

        int result = 1;
        while (n > 0) {
            result *= x;
            n--;
        }
        return result;
    }

    /**
     * Suurim ühistegur
     */
    public static Integer gcd(Integer a, Integer b) {
        while (b > 0) {
            int c = a % b;
            a = b;
            b = c;
        }
        return a;
    }
}
