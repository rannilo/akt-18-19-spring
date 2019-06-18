package week4.mealy;

import java.util.*;


/**
 * Siin on vaja defineerida järgmised masinad. Kõige paremini saab nendest aru, kui hoolikalt
 * teste läbi vaadata. Seekord on kõik testid avalikud. Me võime aga kosmeetilisi muudatusi nendele teha,
 * seega ära päris ainult nende pealt progremmeeri.
 */
public class MachineDefs {


    /**
     * Tulemusena tahaks saada masin, mis realiseerib meie HtmlStrip ülesanne.
     * Siin on alustatud, aga väike viga on sees... Lisaks võiks masina täiendada, et
     * mõlemat tüüpi jutumärkidega arvestaks.
     */
    public static MealyMachine getHtmlMachine() {
        List<TableEntry> table = Arrays.asList(
                new TableEntry(0, '<', 1, ""),
                new TableEntry(0, '_', 0, "_"),

                new TableEntry(1, '>', 0, ""),
                new TableEntry(1, '\'', 2, ""),
                new TableEntry(1, '\"', 3, ""),
                new TableEntry(1, '_', 1, ""),

                new TableEntry(2, '\'', 1, ""),
                new TableEntry(2, '_', 2, ""),

                new TableEntry(3, '\"', 1, ""),
                new TableEntry(3, '_', 3, "")

        );
        return new MealyMachine(table);
    }


    /**
     * Masin, mis asendab iga teine 'x' tähega 'o', näiteks 'xxxx' → 'xoxo'.
     */
    public static MealyMachine getXoxoMachine() {
        List<TableEntry> table = Arrays.asList(
                new TableEntry(0, 'x', 1, "x"),
                new TableEntry(0, '_', 0, "_"),
                new TableEntry(1, 'x', 2, "o"),
                new TableEntry(1, '_', 1, "_"),
                new TableEntry(2, 'x', 1, "_"),
                new TableEntry(2, '_', 2, "_")
        );
        return new MealyMachine(table);
    }

    /**
     * Siin on vaja plus-avaldist juppideks jagada, toppides etteantud eraldaja juppide vahele.
     * Kui eraldaja on näiteks '|', siis sisendi "10+kala++5" korral tahaks saada "10|+|kala|+|+|5".
     * Ma ei taha saada "10|+|kala|+||+|5"
     */
    public static MealyMachine getTokenizer(char delim) {
        // Samuti kahe seisundiga masin: https://courses.cs.ut.ee/2017/AKT/spring/Main/PlusMiinus
        // Sealset viimast seisundit ei ole vaja ja väljundid on siin muidugi lihtsamad.
        List<TableEntry> table = Arrays.asList(
                new TableEntry(0, '+', 1, delim+"+"),
                new TableEntry(0, '_', 0, "_"),
                new TableEntry(1, '+', 1, delim+"+"),
                new TableEntry(1, '_', 0, delim+"_")
        );
        return new MealyMachine(table);
    }

    /**
     * Sisendiks on sulgavaldis, kus on peidus üks dollarmärk. Tuleb tagastada dollarmärgi sügavus ehk
     * kui palju rohkem on dollarmärgini jõudes sulge avatud, kui seda on kinni pandud. Kuna lõpliku arv
     * seisunditega on seda võimatu lahendada, võib eeldada, et maksimaalne sügavus on 10.
     */
    public static MealyMachine getDepthMachine() {
        List<TableEntry> table = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            table.add(new TableEntry(i, '(', i+1, ""));
            table.add(new TableEntry(i, ')', i-1, ""));
            table.add(new TableEntry(i, '$', i, Integer.toString(i)));
            table.add(new TableEntry(i, '_', i, ""));
        }
        return new MealyMachine(table);
    }

    /**
     * See on see eelmise nädala kodutöö ülesanne, aga lõpuks saab seda normaalselt kirja panna.  :)
     * See ei ole liiga tüütu, kui kasutada kirjavahemärkide lisamiseks abifunktsioon,
     * eeldades muidugi, et automaat on kõigepealt optimeeritud.
     */
    public static MealyMachine getFormatter() {
        List<TableEntry> table = new ArrayList<>();
        Set<Character> punc = new HashSet<>(Arrays.asList(',', '.', ':', ';', '!', '?', ')'));
        table.add(new TableEntry(0, '(', 0, "("));
        table.add(new TableEntry(1, '(', 0, " ("));
        table.add(new TableEntry(2, '(', 0, " ("));

        for(Character c: punc){
            table.add(new TableEntry(0, c, 1, "_"));
            table.add(new TableEntry(2, c, 1, "_"));
            table.add(new TableEntry(1, c, 1, "_"));
        }
        table.add(new TableEntry(0, '\n', 0, "\n"));
        table.add(new TableEntry(1, '\n', 0, "\n"));
        table.add(new TableEntry(2, '\n', 0, "\n"));

        table.add(new TableEntry(2, ' ', 1, ""));
        table.add(new TableEntry(1, ' ', 1, ""));
        table.add(new TableEntry(0, ' ', 0, ""));

        table.add(new TableEntry(1, '_', 2, " _"));
        table.add(new TableEntry(2, '_', 2, "_"));
        table.add(new TableEntry(0, '_', 2, "_"));
        return new MealyMachine(table);
    }


    private static void addPuncts(List<TableEntry> table, int pre) {
        List<Character> puncts = Arrays.asList(',', ';', ':', '!', '?', '.', ')');
        for (char c : puncts) table.add(new TableEntry(pre, c, 1, "_"));
        table.add(new TableEntry(pre, '\n', 0, "\n"));
    }

    /**
     * Aitab naljast, nüüd lõpuks saab natuke mõelda... Sisenditeks on binaararvud, mis lõppevad dollariga.
     * Tuleks öelda, mis on antud binaararvu väärtus kümnendsüsteemis, aga kuna meie automaadid on lõplikud,
     * siis saame ainult loendada teatud arvuni. Kui piir on ületatud, siis tuleb uuesti nullist lugema hakkata.
     * Teiste sõnadaga, me fikseerime arvu n ja masin arvutab, mis on binaararvu jääk jagamisel n-iga.
     */
    public static MealyMachine getBinaryMachine(int n) {
        // See on tegelikult lihtne, kui Algebra loengutest on meeles, kuidas jäägiklassid käituvad...
        throw new UnsupportedOperationException();
    }
}
