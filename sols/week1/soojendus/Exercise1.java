package week1.soojendus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Exercise1 {


    // Lugeda failist kõik read ja liita neid kokku.
    // Faili nimi antakse parameetrina.
    // NB! Seda tuleks from scratch lahendada.
    public static void main(String[] args) {

        int sum = 0;

        File file = new File(args[0]);

        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextInt()) {
//                String line = scanner.nextLine();
                sum += scanner.nextInt();
            }
            System.out.println(sum);

        } catch (FileNotFoundException e) {
            System.err.println("Sorry, " + file + " does not exist!");
        }

    }


}
