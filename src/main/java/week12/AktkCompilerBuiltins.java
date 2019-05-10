package week12;

import java.util.Scanner;

public class AktkCompilerBuiltins {
    private static Scanner systemInScanner = null;

    // sisend-väljund
    public static int print(int value) {
        System.out.println(value);
        return 0; // mugavuse jaoks tagastame alati int
    }

    public static int readInt() {
        if (systemInScanner == null) {
            systemInScanner = new Scanner(System.in);
        }

        return Integer.parseInt(systemInScanner.nextLine());
    }

    // loogikatehted
    public static int not(int x) {
        return ~x;
    }

    public static int and(int a, int b) {
        return a & b;
    }

    public static int or(int a, int b) {
        return a | b;
    }

    // täisarvude tehted
    /**
     * Astendamine
     */
    public static int power(int x, int n) {
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
    public static int gcd(int a, int b) {
        while (b > 0) {
            int c = a % b;
            a = b;
            b = c;
        }
        return a;
    }
}
