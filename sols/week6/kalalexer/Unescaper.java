package week6.kalalexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Unescaper {

    /**
     * Töötleb sisendsõne selliselt, et escape'itud reavahetused on asendatud päris reavahetustega.
     */
    private static String unescape(String str) {
        StringBuilder output = new StringBuilder();
        boolean escape = false;
        for (char c : str.toCharArray()) {
                if (escape) {
                    if (c == 'n') output.append('\n');
                    else throw new IllegalArgumentException("Unknown escape literal");
                    escape = false;
                }
                else if (c == '\\') escape = true;
                else output.append(c);
            }s
        return output.toString();
    }


    /**
     * Testime, kas meetod teeb seda, mida vaja failis input3.txt oleva kirjutisega.
     */
    public static void main(String[] args) throws IOException {

        // Esialgne sõne:
        String mystring = "foo\nbar\nbaz";
        System.out.print("Original: ");
        System.out.println(Arrays.toString(mystring.toCharArray()));
        System.out.println();

        // sisend failist
        String input = new String(Files.readAllBytes(Paths.get("input3.txt"))).trim();
        System.out.print("Input: ");
        System.out.println(Arrays.toString(input.toCharArray()));
        System.out.println();

        // funktsiooni väljund
        String unescaped = unescape(input.substring(1, input.length() - 1));
        System.out.print("Unescaped: ");
        System.out.println(Arrays.toString(unescaped.toCharArray()));
        System.out.println();

        // funktsiooni väljund peaks olema see, mis oodatud
        System.out.println(!unescaped.equals(mystring) ? "Ei tööta!" : "Suurepärane töö!");
    }
}
