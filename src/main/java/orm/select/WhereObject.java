package orm.select;

import orm.delegates.Func1;

public class WhereObject<T> {
    public int type;
    public Func1<T> lambda;

    public WhereObject(int _type, Func1<T> _lambda) {
        type = _type;
        lambda = _lambda;
    }
}
