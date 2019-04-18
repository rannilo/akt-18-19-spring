package week7.kalaparser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class KalaNode {

    public abstract int sum(Map<String, Integer> env);

    public static KalaNode mkNull() {
        return new KalaNull();
    }

    public static KalaNode mkIdent(String name) {
        return new KalaIdent(name);
    }

    public static KalaNode mkList(List<KalaNode> args) {
        return new KalaList(args);
    }

    public static KalaNode mkList(KalaNode... args) {
        return mkList(Arrays.asList(args));
    }


    // Siit edasi on siis lahendus ette antud, et selle peale mitte jälle liiga palju aega kulutada.
    // Nende peidetud "sisemiste" klasside asemel võid kodutöös teha täiesti eraldi avalikud klassid!

    private static class KalaIdent extends KalaNode {

        private final String name;

        public KalaIdent(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return env.get(name);
        }
    }

    private static class KalaList extends KalaNode {

        private final List<KalaNode> args;

        public KalaList(List<KalaNode> args) {
            this.args = args;
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (KalaNode arg : args) joiner.add(arg.toString());
            return joiner.toString();
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return args.stream().mapToInt(n -> n.sum(env)).sum();
        }
    }

    private static class KalaNull extends KalaNode {

        @Override
        public String toString() {
            return "NULL";
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return 0;
        }
    }
}
