package week2;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week2.intro.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextAnalyzerTest {

    @Test
    public void test01_last() throws Exception {
        List<String> input = Arrays.asList("cred", "schred!", "bedding", "ed", "eddard", "edx");
        check(TextAnalyzer.RE1, input, "cred", "schred!", "ed", "edx");
    }

    @Test
    public void test02_odd() throws Exception {
        List<String> input = Arrays.asList("123", "11", "kala", "kalur", "pallur", "palluri", "!");
        check(TextAnalyzer.RE2, input, "123", "kalur", "palluri", "!");
    }

    @Test
    public void test03_beginend() throws Exception {
        char c = (char) new Random().nextInt();
        char d = (char) new Random().nextInt();
        if (c == d) c++;
        List<String> input = Arrays.asList("aba", "ab", c + "ab" + c, c + "ab" + d, "aab", "baa");
        check(TextAnalyzer.RE3, input, "aba", c + "ab" + c);
    }

    @Test
    public void test04_nimed() throws Exception {
        List<String> input = Arrays.asList("John", "Kalle Kulbok", "vesal vojdani", "Mari maasiKas", "J Jubin");
        check(TextAnalyzer.NAME, input, "Kalle Kulbok", "J Jubin");
    }

    @Test
    public void test05_numbrideasy() throws Exception {
        List<String> input = Arrays.asList("42312345", "1234-4444", "1234567", "12-343-33", "123-456", "1234+3534");
        check("^("+ TextAnalyzer.NUMBER + ")$", input, "42312345", "1234-4444", "1234567", "123-456");
    }

    @Test
    public void test06_numbrid() throws Exception {
        List<String> input = Arrays.asList("42312345", "1234-4444", "123", "12-265", "1234567", "12345678",
                "123456789", "1234-567", "123-4567", "123-456", "1234-5678", "12345-6789");
        check("^("+ TextAnalyzer.NUMBER + ")$", input, "42312345", "1234-4444", "1234567", "12345678", "1234-567", "123-4567", "123-456", "1234-5678");
    }


    @Test
    public void test07_getPhoneNumbers() {
        checkPhoneNumber("Mart Laar, tel: 42312345, e-mail:laar@laar.ee", "Mart Laar","42312345");
        checkPhoneNumber("Suvaline teks ja *+*asf,, Jaak Saar veel miskit +afV+++*., 1234-4444.", "Jaak Saar","12344444");
    }

    @Test
    public void test08_anonymize() {
        checkAnon("Mart Laar, tel: 42312345, e-mail:laar@laar.ee", "<nimi>, tel: <telefoninumber>, e-mail:laar@laar.ee");
        checkAnon("Suvaline teks ja *+*asf,, Jaak Saar veel miskit +afV+++*., 1234-4444.", "Suvaline teks ja *+*asf,, <nimi> veel miskit +afV+++*., <telefoninumber>.");
    }

    @Test
    public void test09_misc() {
        checkAnon("Mart Laar   42312345", "<nimi>   <telefoninumber>");
        checkAnon("Ma La   42312345.", "<nimi>   <telefoninumber>.");
        checkPhoneNumber("Ma La   42312345.", "Ma La", "42312345");
        checkPhoneNumber("Ma La   423 2345.", "Ma La", "4232345");
        checkPhoneNumber("Hai Kala, sündinud 80ndatel, saab kätte numbril 423 2345.", "Hai Kala", "4232345");
        checkPhoneNumber("Karu Kalle, iskukoodiga 38205937492, telefoninumber on 3244-8589.", "Karu Kalle", "32448589");
    }

    @Test
    public void test10_demoCase() {
        TextAnalyzer ta = new TextAnalyzer(
                "Mina olen Kalle Kulbok ja mu telefoninumber on 5556 4272.\n"
                        + "Mina olen Peeter Peet ja mu telefoninumber on 5234 567.\n"
                        + "Mari Maasikas siin, mu number on 6723 3434.\n"
                        + "Tere, olen Jaan Jubin numbriga 45631643.\n");

        Map<String, String> phoneBook = ta.getPhoneNumbers();
        assertEquals("5234567", phoneBook.get("Peeter Peet"));
        assertEquals("45631643", phoneBook.get("Jaan Jubin"));

        assertEquals("Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.\n" +
                        "Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.\n" +
                        "<nimi> siin, mu number on <telefoninumber>.\n" +
                        "Tere, olen <nimi> numbriga <telefoninumber>.\n",
                ta.anonymize());
    }


	private void checkPhoneNumber(String input, String nimi, String expected) {
		TextAnalyzer ta = new TextAnalyzer(input);
        String actual = ta.getPhoneNumbers().get(nimi);
		assertEquals(input, expected, actual);
	}

    private void checkAnon(String input, String expected) {
        TextAnalyzer ta = new TextAnalyzer(input);
        String actual = ta.anonymize();
        assertEquals(input, expected, actual);
    }

    private void check(String re, List<String> input, String... expected) {
        List<String> result = Util.grep(re, input);
        assertEquals(Arrays.asList(expected), result);
    }
}
