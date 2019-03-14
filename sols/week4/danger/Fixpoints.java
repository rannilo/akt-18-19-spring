package week4.danger;

import week4.buildingblocks.Trans;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

import static com.google.common.collect.Sets.union;
import static week4.buildingblocks.Relation.compose;


/**
 * Siin on paar rida koodi, mis implementeerivad õpikus kirjeldatud mõttekäiku.
 * Selleks peab lugema peatükk 1.5.1, muidu on väga raske siit midagi aru saada.
 *
 */
public class Fixpoints {

    public static <T> T fixpoint(UnaryOperator<T> f, T x) {
        T next = f.apply(x);
        if (next.equals(x)) return x;
        else return fixpoint(f, next);
    }

    public static <T> Set<T> closure(UnaryOperator<Set<T>> f, Set<T> m) {
        return fixpoint(x -> union(x, f.apply(x)), m);
    }

    // Neid kaks funktsiooni saab kombineerida üheks natuke kiiremaks funktsiooniks,
    // kui funktsioon f on distributiivne.
    public static <T> Set<T> closureIterate(UnaryOperator<Set<T>> f, Set<T> m) {
        Set<T> set = new HashSet<>();
        while (set.addAll(m)) m = f.apply(m);
        return set;
    }

    // Kogu selle asja eelis on siis, et saab transitiivse sulundi arvutada ühe reaga.
    public static Set<Trans> transitiveClosure(Set<Trans> rel) {
        return closure(x -> compose(x, rel), rel);
    }

    public static void main(String[] args) {
        Set<Trans> rel0 = new HashSet<>();
        rel0.add(new Trans(0, "a", 1));
        rel0.add(new Trans(1, "b", 2));
        rel0.add(new Trans(1, "c", 3));
        rel0.add(new Trans(3, "d", 4));

        Set<Trans> rel1 = compose(rel0, rel0); System.out.println(rel1);
        Set<Trans> rel2 = compose(rel1, rel0); System.out.println(rel2);
        Set<Trans> rel3 = compose(rel2, rel0); System.out.println(rel3);

        System.out.println(transitiveClosure(rel0));
        //[(0,a,1), (1,b,2), (1,c,3), (3,d,4), (0,ab,2), (0,ac,3), (1,cd,4), (0,acd,4)]
    }

}
