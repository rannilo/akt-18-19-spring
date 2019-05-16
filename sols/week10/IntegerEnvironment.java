package week10;

import java.util.*;

public class IntegerEnvironment {
    private Deque<Map<String, Integer>> envs = new ArrayDeque<>();

    public IntegerEnvironment() {
        enterBlock();
    }

    public void declare(String variable) {
        envs.peekFirst().put(variable, null);
    }

    public void assign(String variable, Integer value) {
        Map<String, Integer> env = findEnv(variable);
        if (env != null)
            env.put(variable, value);
    }

    public Integer get(String variable) {
        Map<String, Integer> env = findEnv(variable);
        return env != null ? env.get(variable) : null;
    }

    public void enterBlock() {
        envs.addFirst(new HashMap<>());
    }

    public void exitBlock() {
        envs.removeFirst();
    }

    private Map<String, Integer> findEnv(String variable) {
        for (Map<String, Integer> env : envs) {
            if (env.containsKey(variable))
                return env;
        }
        return null;
    }
}
