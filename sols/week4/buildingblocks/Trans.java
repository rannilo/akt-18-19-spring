package week4.buildingblocks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trans {

    private final int source;
    private final String label;
    private final int target;

    public Trans(int source, String label, int target) {
        this.source = source;
        this.label = label;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public String getLabel() {
        return label;
    }

    public int getTarget() {
        return target;
    }

    public static Map<Integer, Map<String, Integer>> makeTransitionMap(Set<Trans> transitions) {
        Map<Integer, Map<String, Integer>> map = new HashMap<>();
        // Siin on peamine mure, et mapi sees on omakorda vaja neid mapikesed luua!
        for (Trans trans : transitions) {
            int from = trans.getSource();
            Map<String, Integer> minimap = map.get(from);
            if (minimap == null) {  // IntelliJ väidab, et saab paremini... proovige!
                minimap = new HashMap<>();
                map.put(from, minimap);
            }
            minimap.put(trans.getLabel(), trans.getTarget());
        }
        return map;
    }

    public static void main(String[] args) {
        Set<Trans> table = new HashSet<>();
        table.add(new Trans(1, "a", 2));
        table.add(new Trans(1, "b", 3));
        table.add(new Trans(2, "c", 3));
        System.out.println(makeTransitionMap(table)); // {1={a=2, b=3}, 2={c=3}}
    }

    @Override
    public String toString() {
        return String.format("(%d,%s,%d)", source, label, target);
    }


    // IntelliJ autogenereeritud equals & Hashcode.
    // (Ainult vajalik Relation jaoks.)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trans trans = (Trans) o;
        if (source != trans.source) return false;
        if (target != trans.target) return false;
        return label != null ? label.equals(trans.label) : trans.label == null;
    }

    @Override
    public int hashCode() {
        int result = source;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + target;
        return result;
    }

}
