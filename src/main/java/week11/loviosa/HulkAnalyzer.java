package week11.loviosa;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaUtils;
import week9.pohiosa.hulk.hulkAst.HulkNode;
import week9.pohiosa.hulk.hulkAst.HulkProgramm;

import java.util.*;

import static week9.pohiosa.hulk.hulkAst.HulkNode.*;

public class HulkAnalyzer {

    public static boolean isValidHulkNode(HulkNode node) {
        throw new UnsupportedOperationException();
    }

    public static HulkNode processEmptyLiterals(HulkNode node) {
        throw new UnsupportedOperationException();
    }

    public static void run(HulkNode node, Map<Character, Set<Character>> env) {
        throw new UnsupportedOperationException();
    }

    private static final List<Character> SET_VARIABLES = Arrays.asList('X', 'A', 'B', 'C', 'D', 'G', 'H', 'V'); // kasuta SET_VARIABLES.indexOf

    // kasuta hulga literaali teisendamiseks arvuks (bitset)
    private static int set2int(Set<Character> set) {
        List<Character> ELEM_VARIABLES = Arrays.asList('x', 'y', 'z', 'a', 'b', 'c', 'u', 'v');
        int result = 0;
        if (set != null) {
            for (int i = 0; i < ELEM_VARIABLES.size(); i++) {
                result |= CMaUtils.bool2int(set.contains(ELEM_VARIABLES.get(i))) << i;
            }
        }
        return result;
    }

    public static CMaProgram compile(HulkNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }

    public static void main(String[] args) {
        HulkProgramm s = prog(new ArrayList<>());
        s.lisaLause(lause('A', lit('a'), null));  // A := {a}
        s.lisaLause(lause('B', lit('b'), null));  // B := {b}
        s.lisaLause(lause('D', tehe(var('A'), var('B'), '+'), null));  // D := (A+B)
        System.out.println(s);
        System.out.println();

        Map<Character, Set<Character>> env = new HashMap<>();
        run(s, env);
        System.out.println(env);  // {A=[a], B=[b], D=[a, b]}
    }

}
