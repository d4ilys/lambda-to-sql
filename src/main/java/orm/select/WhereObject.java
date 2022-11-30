package orm.select;

import orm.delegates.SqlPredicate;

public class WhereObject<T> {
    public int type;
    public SqlPredicate<T> lambda;

    public WhereObject(int _type, SqlPredicate<T> _lambda) {
        type = _type;
        lambda = _lambda;
    }
}
