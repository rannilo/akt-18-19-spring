package week2.regexapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {

    public static void main(String[] args) {
        String input =
                "1, 2, Polizei, 3, 4, Grenadier...\n" +
                "Jenny, don't change your number, 867 5309.\n" +
                "666 the number of the Beast. Hell and fire was spawned to be released.\n" +
                "87, 87 var har du tagit vägen nu?\n" +
                "Olen su poole teel. Aa-ha-ha-haaaaaaa-haa-haaaaaa. 581C.";

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        List<Integer> numbers = new ArrayList<>();

        while (matcher.find()) {
            String match = matcher.group();
            numbers.add(Integer.valueOf(match));
        }

        System.out.print("Tekstis esinevad arvud: ");
        System.out.println(numbers);


        matcher = Pattern.compile("(\\d+) (\\d+)").matcher(input);
        if (matcher.find()) {
            System.out.println("Jenny number: " + matcher.group(0)); // = matcher.group()
            System.out.println("Esimene osa: " + matcher.group(1));
            System.out.println("Teine osa: " + matcher.group(2));
        }
    }
}
