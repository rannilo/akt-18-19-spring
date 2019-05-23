package week11.loviosa;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaUtils;
import week9.pohiosa.hulk.hulkAst.*;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkAvaldis;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkLiteraal;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkMuutuja;
import week9.pohiosa.hulk.hulkAst.avaldis.HulkTehe;

import java.util.*;
import java.util.stream.Collectors;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.*;
import static week9.pohiosa.hulk.hulkAst.HulkNode.*;

public class HulkAnalyzer {

    public static boolean isValidHulkNode(HulkNode node) {
        HulkVisitor<Boolean> visitor = new HulkVisitor<Boolean>() {
            private final Set<Character> definedVars = new HashSet<>();

            @Override
            public Boolean visit(HulkLiteraal literaal) {
                return true;
            }

            @Override
            public Boolean visit(HulkMuutuja muutuja) {
                return definedVars.contains(muutuja.getNimi());
            }

            @Override
            public Boolean visit(HulkTehe tehe) {
                return visit(tehe.getVasak()) && visit(tehe.getParem());
            }

            @Override
            public Boolean visit(HulkLause lause) {
                boolean validAvaldis = visit(lause.getAvaldis());
                boolean validTingimus = true;
                HulkTingimus tingimus = lause.getTingimus();
                if (tingimus != null) {
                    validTingimus = visit(tingimus);
                } else {
                    definedVars.add(lause.getNimi());
                }
                return validAvaldis && validTingimus;
            }

            @Override
            public Boolean visit(HulkProgramm programm) {
                return programm.getLaused().stream().allMatch(this::visit);
            }

            @Override
            public Boolean visit(HulkTingimus tingimus) {
                return visit(tingimus.getAlamHulk()) && visit(tingimus.getYlemHulk());
            }
        };

        return visitor.visit(node);
    }

    public static HulkNode processEmptyLiterals(HulkNode node) {
        HulkVisitor<HulkNode> visitor = new HulkVisitor<HulkNode>() {
            @Override
            public HulkNode visit(HulkLiteraal literaal) {
                return literaal;
            }

            @Override
            public HulkNode visit(HulkMuutuja muutuja) {
                return muutuja;
            }

            @Override
            public HulkNode visit(HulkTehe tehe) {
                Character op = tehe.getOp();
                HulkAvaldis vasak = (HulkAvaldis) visit(tehe.getVasak());
                HulkAvaldis parem = (HulkAvaldis) visit(tehe.getParem());

                if (op == '-' && parem.equals(HulkNode.lit())) {
                    return vasak;
                } else if (op == '-' && vasak.equals(HulkNode.lit())) {
                    return HulkNode.lit();
                } else if (op == '&' && (vasak.equals(HulkNode.lit()) || parem.equals(HulkNode.lit()))) {
                    return HulkNode.lit();
                } else if (op == '+' && (vasak.equals(HulkNode.lit()) || parem.equals(HulkNode.lit()))) {
                    if (vasak.equals(HulkNode.lit())) {
                        return parem;
                    } else {
                        return vasak;
                    }
                }
                return new HulkTehe(vasak, parem, op);
            }

            @Override
            public HulkNode visit(HulkLause lause) {
                HulkTingimus hulkTingimus = lause.getTingimus() != null ? (HulkTingimus) visit(lause.getTingimus()) : null;
                HulkAvaldis hulkAvaldis = (HulkAvaldis) visit(lause.getAvaldis());

                if (hulkTingimus != null && hulkTingimus.getAlamHulk().equals(HulkNode.lit())) {
                    return new HulkLause(lause.getNimi(), hulkAvaldis, null);
                }
                return new HulkLause(lause.getNimi(), hulkAvaldis, hulkTingimus);
            }

            @Override
            public HulkNode visit(HulkProgramm programm) {
                return new HulkProgramm(programm.getLaused().stream()
                        .map(x -> (HulkLause) visit(x))
                        .collect(Collectors.toList()));
            }

            @Override
            public HulkNode visit(HulkTingimus tingimus) {
                return new HulkTingimus(((HulkAvaldis) visit(tingimus.getAlamHulk())),
                        ((HulkAvaldis) visit(tingimus.getYlemHulk())));
            }
        };

        return visitor.visit(node);
    }

    public static void run(HulkNode node, Map<Character, Set<Character>> env) {
        HulkVisitor<Set<Character>> visitor = new HulkVisitor<Set<Character>>() {
            @Override
            public Set<Character> visit(HulkLiteraal literaal) {
                return literaal.getElemendid();
            }

            @Override
            public Set<Character> visit(HulkMuutuja muutuja) {
                Character nimi = muutuja.getNimi();
                if (env.containsKey(nimi))
                    return env.get(muutuja.getNimi());
                else
                    throw new NoSuchElementException(nimi.toString());

            }

            @Override
            public Set<Character> visit(HulkTehe tehe) {
                Set<Character> vasak = visit(tehe.getVasak());
                Set<Character> parem = visit(tehe.getParem());
                Set<Character> result = new HashSet<>(vasak);
                switch (tehe.getOp()) {
                    case '+':
                        result.addAll(parem);
                        break;
                    case '-':
                        result.removeAll(parem);
                        break;
                    case '&':
                        result.retainAll(parem);
                }
                return result;
            }

            @Override
            public Set<Character> visit(HulkLause lause) {
                if (lause.getTingimus() == null || visit(lause.getTingimus()).isEmpty()) {
                    Character nimi = lause.getNimi();
                    Set<Character> elemendid = visit(lause.getAvaldis());
                    env.put(nimi, elemendid);
                }
                return null;
            }

            @Override
            public Set<Character> visit(HulkProgramm programm) {
                for (HulkLause hulkLause : programm.getLaused()) {
                    visit(hulkLause);
                }
                return null;
            }

            @Override
            public Set<Character> visit(HulkTingimus tingimus) {
                // tagastame vahe nende vahe: võtame kõik ylem elemente alamist ära:
                Set<Character> diff = new HashSet<>(visit(tingimus.getAlamHulk()));
                diff.removeAll(visit(tingimus.getYlemHulk()));
                return diff;
            }
        };

        visitor.visit(node);
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

        HulkVisitor<Void> visitor = new HulkVisitor<Void>() {
            @Override
            public Void visit(HulkLiteraal literaal) {
                pw.visit(LOADC, set2int(literaal.getElemendid()));
                return null;
            }

            @Override
            public Void visit(HulkMuutuja muutuja) {
                pw.visit(LOADA, SET_VARIABLES.indexOf(muutuja.getNimi()));
                return null;
            }

            @Override
            public Void visit(HulkTehe tehe) {
                visit(tehe.getVasak());
                visit(tehe.getParem());
                switch (tehe.getOp()) {
                    case '+':
                        pw.visit(OR);
                        break;
                    case '-':
                        // A - B = A & ~B = A & (B ^ 11..1) = A & (B ^ -1)
                        pw.visit(LOADC, -1);
                        pw.visit(XOR);
                        pw.visit(AND);
                        break;
                    case '&':
                        pw.visit(AND);
                        break;
                }
                return null;
            }

            @Override
            public Void visit(HulkLause lause) {
                if (lause.getTingimus() == null)
                    pw.visit(LOADC, 0);
                else
                    visit(lause.getTingimus());
                pw.visit(NOT);
                CMaLabel _endif = new CMaLabel();
                pw.visit(JUMPZ, _endif);
                visit(lause.getAvaldis());
                pw.visit(STOREA, SET_VARIABLES.indexOf(lause.getNimi()));
                pw.visit(POP);
                pw.visit(_endif);
                return null;
            }

            @Override
            public Void visit(HulkProgramm programm) {
                for (HulkLause hulkLause : programm.getLaused()) {
                    visit(hulkLause);
                }
                return null;
            }

            @Override
            public Void visit(HulkTingimus tingimus) {
                // tagastame vahe nende vahe: võtame kõik ylem elemente alamist ära:
                visit(tingimus.getAlamHulk());
                visit(tingimus.getYlemHulk());
                // A - B = A & ~B = A & (B ^ 11..1) = A & (B ^ -1)
                pw.visit(LOADC, -1);
                pw.visit(XOR);
                pw.visit(AND);
                return null;
            }
        };

        visitor.visit(node);

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
