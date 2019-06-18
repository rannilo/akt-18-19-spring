package week10;

import java.util.*;

public class IntegerEnvironment {

    Stack<Map<String, Object>> data;

    IntegerEnvironment(){
        data = new Stack<>();
        data.add(new HashMap<>());
    }

    public void declare(String variable) {
        data.peek().put(variable, null);
    }

    public void assign(String variable, Integer value) {
        data.peek().put(variable, value);
    }

    public Integer get(String variable) {
        for (int i = data.size()-1; i>= 0; i--){
            Integer value = (Integer)data.get(i).getOrDefault(variable, null);
            if(value != null) return value;
        }
        return null;
    }

    public void enterBlock() {
        data.push(new HashMap<>());
    }

    public void exitBlock() {
        data.pop();
    }
}
