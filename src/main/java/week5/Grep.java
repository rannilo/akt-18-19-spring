package week5;

import week4.FiniteAutomaton;
import week5.regex.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Grep {
    /*
     * main meetodit ei ole vaja muuta.
     *
     * See meetod on siin vaid selleks, et anda käesolevale  harjutusele veidi
     * realistlikum kontekst. Aga tegelikult on see vaid mäng -- see programm ei
     * pretendeeri päeva kasulikuima programmi tiitlile. Päris elus kasuta päris grep-i.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1 || args.length > 2) {
            System.err.println(
                    "Programm vajab vähemalt ühte argumenti: regulaaravaldist.\n" +
                            "Teiseks argumendiks võib anda failinime (kui see puudub, siis loetakse tekst standardsisendist).\n" +
                            "Failinime andmisel eeldatakse, et tegemist on UTF-8 kodeeringus tekstifailiga.\n" +
                            "Rohkem argumente programm ei aktsepteeri.\n"
            );
            System.exit(1);
        }

        RegexNode regex = RegexParser.parse(args[0]);
        FiniteAutomaton automaton = optimize(regexToFiniteAutomaton(regex));

        Scanner scanner;
        if (args.length == 2) {
            scanner = new Scanner(new FileInputStream(args[1]), "UTF-8");
        } else {
            scanner = new Scanner(System.in);
        }

        // kuva ekraanile need read, mis vastavad antud regulaaravaldisele/automaadile
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (automaton.accepts(line)) {
                System.out.println(line);
            }
        }

        scanner.close();
    }

    /*
     * See meetod peab loenguslaididel toodud konstruktsiooni põhjal koostama ja tagastama
     * etteantud regulaaravaldisele vastava mittedetermineeritud lõpliku automaadi.
     * Selle meetodi korrektne implementeerimine on antud ülesande juures kõige tähtsam.
     *
     * (Sa võid selle meetodi implementeerimiseks kasutada abimeetodeid ja ka abiklasse,
     * aga ära muuda meetodi signatuuri, sest automaattestid eeldavad just sellise signatuuri
     * olemasolu.)
     *
     * (Selle ülesande juures pole põhjust kasutada vahetulemuste salvestamiseks klassivälju,
     * aga kui sa seda siiski teed, siis kontrolli, et see meetod töötab korrektselt ka siis,
     * kui teda kutsutakse välja mitu korda järjest.)
     */
    public static FiniteAutomaton regexToFiniteAutomaton(RegexNode regex) {
        throw new UnsupportedOperationException();
    }

    /** See meetod peab looma etteantud NFA-le vastava DFA, st. etteantud
     *  automaat tuleb determineerida.
     *  Kui sa seda ei jõua teha, siis jäta see meetod nii, nagu ta on.
     */
    public static FiniteAutomaton optimize(FiniteAutomaton nfa) {
        return nfa;
    }

}
