package week3;

public class FormatMachineDemo {

    public static String cleanUp(String s) {
        StringBuilder sb = new StringBuilder();
        FormatMachine machine = new FormatMachine();
        for (char c : s.toCharArray()) sb.append(machine.process(c));
        return sb.toString();
    }

    public static void main(String[] args) {
        String input =
                "This     text (all of it   )has occasional lapses .. .in\n" +
                "  punctuation( sometimes,pretty bad ; sometimes ,not so).\n" +
                "\n" +
                "( Ha ! )Is this  :fun ! ? !  Or   what  ?";
        System.out.println(cleanUp(input));
    }
}
