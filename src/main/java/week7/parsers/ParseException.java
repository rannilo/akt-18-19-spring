package week7.parsers;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ParseException extends RuntimeException {

    private final int offset;
    private final Set<Character> expected;

    public int getOffset() {
        return offset;
    }

    public Set<Character> getExpected() {
        return expected;
    }

    public ParseException(char c, int pos, Character... expected) {
        super(String.format("Unexpected character '%s' at offset %d, but expected: %s",
                c, pos, Arrays.toString(expected)));
        this.offset = pos;
        this.expected = new LinkedHashSet<>(Arrays.asList(expected));
    }
}
