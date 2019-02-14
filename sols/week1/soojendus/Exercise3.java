package week1.soojendus;

import java.util.*;


public class Exercise3 {

    /**
     * Defineeri meetod eval, mis väärtustab etteantud avaldise.
     * @param str on plussidega eraldatud arvude jada, näiteks "5 + 35+  10".
     * @return arvude summa, antud näide puhul 50.
     */
    public static int eval(String str) {
        int sum = 0;
        int current = 0;
        for (char c : str.toCharArray()) {
            if (c == '+') {
                sum += current;
                current = 0;
            }
            if (Character.isDigit(c)) current = current * 10 + Character.getNumericValue(c);
        }
        return sum+current;
    }

    /**
     * Tuletame lihtsalt meelde Java List ja Map andmestruktuurid!
     * Selle ülesanne puhul võiks tegelikult tüüpide ja main meetodi põhjal aru saada, mida tegema peaks...
     *
     * @param list sõnedest, kus on vaheldumisi nimi ja arv (sõne kujul). Võib eeldada, et pikkus on paarisarv.
     * @return listile vastav map nimedest arvudesse.
     */
    public static Map<String, Integer> createMap(List<String> list) {
        Map<String, Integer> env = new HashMap<>();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = iterator.next();
            env.put(key, Integer.valueOf(val));
        }
        return env;
    }

    public static void main(String[] args) {
        System.out.println(eval("2+2"));
        Map<String, Integer> ageMap = createMap(Arrays.asList("Carmen", "17", "Jürgen", "44", "Tarmo", "10", "Mari", "83"));
        System.out.println(ageMap.get("Carmen")); // vastus: 17
        System.out.println(ageMap.get("Tarmo"));  // vastu: 10
    }
}
