package week2;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer {

    // Sõned, mis lõppevad tähtedega "ed" või mõni täht veel.
    // (Ülesanne lehel on pikemalt seletatud!)
    public static final String RE1 = "ed.?$";

    // Paaritu pikkusega sõned.
    public static final String RE2 = "^.(..)*$";

    // Sõned, mille esimene ja viimane täht on sama!
    public static final String RE3 = "^(.).*\\1$";

    // Sõned, mis ülesanne nimede tingimustele vastavad.
	public static final String NAME = "(\\p{Upper}\\p{Lower}*\\s*\\p{Upper}\\p{Lower}*)";

    // Sõned, mis ülesanne numbri tingimustele vastavad.
	public static final String NUMBER = "\\b(\\d{3,4}[\\s-]\\d{3,4}|\\d{4,8})\\b";
    //Lihtsam variant:	public static final String NUMBER = "(\\d+[-\\s]?\\d+)";

    private final String text;

	public TextAnalyzer(String text) {
		this.text = text;
	}

	public Map<String, String> getPhoneNumbers() {
		Pattern pattern = Pattern.compile(NAME + ".*?" + NUMBER + ".*?\\.");
		Matcher matcher = pattern.matcher(text);
		Map<String, String> phonebook = new HashMap<>();
		while(matcher.find()){
			String cleanNumber = matcher.group(2).replaceAll("[-\\s]", "");
			phonebook.put(matcher.group(1), cleanNumber);
		}
		return phonebook;
	}

	public String anonymize() {
		return text.replaceAll(NAME, "<nimi>").replaceAll(NUMBER, "<telefoninumber>");
	}

    // Alternatiivne lahendus: Üldiselt eelistame lihtsamaid lahendusi, aga järgmine lahendus läbib sõne ainult
    // ühe korra ja sellega tahame näidata matcheri üldisem skeem sõnede asendamisel.
    public String anonymizeSingleRun() {
        String regex = String.format("(?<name>%s)|(?<number>%s)", NAME, NUMBER);
        Matcher matcher = Pattern.compile(regex).matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            if (matcher.group("name") != null) matcher.appendReplacement(sb, "<nimi>");
            if (matcher.group("number") != null) matcher.appendReplacement(sb, "<telefoninumber>");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    public static void main(String[] args) {

        String input =
                "Mina olen Kalle Kulbok ja mu telefoninumber on 5556 4272.\n" +
                "Mina olen Peeter Peet ja mu telefoninumber on 5234 567.\n" +
                "Mari Maasikas siin, mu number on 6723 3434.\n" +
                "Tere, olen Jaan Jubin numbriga 45631643.";

        TextAnalyzer ta = new TextAnalyzer(input);
        Map<String, String> phoneBook = ta.getPhoneNumbers();
        System.out.println(phoneBook.get("Peeter Peet")); // peab väljastama 5234567
        System.out.println(phoneBook.get("Jaan Jubin"));  // peab väljastama 45631643

        System.out.println(ta.anonymize());

		/* peab väljastama:
        	Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
        	Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
        	<nimi> siin, mu number on <telefoninumber>.
        	Tere, olen <nimi> numbriga <telefoninumber>.
        */
    }
}
