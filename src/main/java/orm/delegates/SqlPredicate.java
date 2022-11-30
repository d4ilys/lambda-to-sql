package orm.delegates;

import java.io.Serializable;

@FunctionalInterface
public interface SqlPredicate<T> extends Serializable {
    Boolean apply(T t);
}
