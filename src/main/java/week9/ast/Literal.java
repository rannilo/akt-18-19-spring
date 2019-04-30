package week9.ast;

import java.util.Arrays;
import java.util.List;

/**
 * Erinevate literaaliliikide ülemklass.
 */
public abstract class Literal<T> extends Expression {

    protected final T value;

    public Literal(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public List<Object> getChildren() {
        return Arrays.asList(this.value);
    }
}
