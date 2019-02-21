package week2.regexapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exercise2 {

    // Kasutame siin ASCII klassid, aga all lisame Unicode flag.
    private static final String varName = "[\\p{Alpha}_][\\p{Alnum}_]*";

    // Anname siis rühmitusele nimi "fname":
    // http://stackoverflow.com/questions/415580/regex-named-groups-in-java
    private static final String regex =
            "def\\s+(?<fnname>"+ varName +")\\s*\\(\\s*"+ varName +"\\s*,\\s*"+ varName +"\\s*\\)\\s*:";

    public static List<String> extractFunctions(String text){
        List<String> functionNames = new ArrayList<>();
        Pattern p = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS); // <-- saab eesti tähtedega kergemini
        Matcher m = p.matcher(text);
        while(m.find()){
            functionNames.add(m.group("fnname"));
        }
        return functionNames;
    }
}