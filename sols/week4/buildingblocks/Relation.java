package week4.buildingblocks;

import java.util.HashSet;
import java.util.Set;

public class Relation {

    public static Set<Trans> compose(Set<Trans> rel1, Set<Trans> rel2) {
        Set<Trans> result = new HashSet<>();
        for (Trans t1 : rel1) {
            for (Trans t2 : rel2) {
                if (t1.getTarget() == t2.getSource()) {
                    String label = t1.getLabel() + t2.getLabel();
                    result.add(new Trans(t1.getSource(), label, t2.getTarget()));
                }
            }
        }
        return result;
    }

    public static Set<Trans> transitiveClosure(Set<Trans> rel) {
        Set<Trans> result = new HashSet<>(rel);
        Set<Trans> newTrans = compose(result, rel);
        // Peab lisama sinna hulka kõik uued üleminekud, kuni asi enam ei muutu.
        // Ühesõnaga seda compose on vaja korrata tsüklis:
        while (result.addAll(newTrans)) { // addAll tagastab true, kui hulk muutus!
            newTrans = compose(result, rel);
        }
        return result;
    }

    public static void main(String[] args) {
        Set<Trans> rel = new HashSet<>();
        rel.add(new Trans(0, "a", 1));
        rel.add(new Trans(1, "b", 2));
        rel.add(new Trans(1, "c", 3));
        System.out.println(transitiveClosure(rel)); // [(0,a,1), (0,ab,2), (0,ac,3), (1,b,2), (1,c,3)]
    }
}
