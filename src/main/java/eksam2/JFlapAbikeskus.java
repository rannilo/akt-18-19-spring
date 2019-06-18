package eksam2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class JFlapAbikeskus {

    // kontroll, et oleks üldse tähestiku sõna!
    private static boolean belongsToAlphabet(String s) {
        Set<Character> alpha = new HashSet<>(Arrays.asList('x', 'y', 'z'));
        return s.chars().allMatch(i -> alpha.contains((char) i));
    }

    // Sõnad üle tähestiku {x,y,z}, mille iga teine täht on 'x'.
    private static boolean yl1(String s) {
        if (!belongsToAlphabet(s)) return false;
        for (int i = 1; i < s.length(); i += 2) {
            if (s.charAt(i) != 'x') return false;
        }
        return true;
    }

    // Sõnad üle tähestiku {x,y,z}, mille iga teine täht ei ole 'x'.
    private static boolean yl2(String s) {
        if (!belongsToAlphabet(s)) return false;
        for (int i = 1; i < s.length(); i += 2) {
            if (s.charAt(i) == 'x') return false;
        }
        return true;
    }

    // Sõnad üle tähestiku {x,y,z}, mille iga teine täht on 'x' või mille iga teine täht ei ole 'x'.
    // (Automaatide yl2 ja yl3 keelte ühend)
    private static boolean yl3(String s) {
        return yl1(s) || yl2(s);
    }


    // Sõnad üle tähestiku {x,y,z}, mille kohta ei kehti see, et iga teine täht on 'x', ega kehti ka see, et
    // iga teine täht ei ole 'x'. (Automaadi yl3 keele täiend!)
    private static boolean yl4(String s) {
        return belongsToAlphabet(s) && !yl3(s);
    }

    public static void main(String[] args) {
        /*System.out.println(yl2(""));     // kas epsilon kuulub yl1 keelde?
        System.out.println(yl2("yyz"));     // kas epsilon kuulub yl1 keelde?
        System.out.println(yl3("xxxy"));     // kas epsilon kuulub yl1 keelde?
        System.out.println(yl4("zxzy")); // zxzy peaks kuuluma yl4 keelde.
        */
        Pattern p1 = Pattern.compile("  |");
        System.out.println(p1.matcher("|").find());
//
    }
}
