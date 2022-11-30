package orm.delegates;

import java.io.Serializable;

@FunctionalInterface
public interface SinglePredicate<T> extends Serializable {
    void apply(T t);
}
