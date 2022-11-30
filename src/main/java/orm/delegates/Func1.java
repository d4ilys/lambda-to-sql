package orm.delegates;

import java.io.Serializable;

@FunctionalInterface
public interface Func1<T> extends Serializable {
    Boolean apply(T t);
}

