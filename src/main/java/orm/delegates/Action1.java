package orm.delegates;

import java.io.Serializable;

@FunctionalInterface
public interface Action1<T> extends Serializable {
    void apply(T t);
}
