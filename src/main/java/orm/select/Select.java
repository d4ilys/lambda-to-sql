package orm.select;

import orm.delegates.Action1;
import orm.delegates.SinglePredicate;
import orm.delegates.Func1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Select<T> {
    private List<Action1<Where<T>>> wheres = new ArrayList<>();

    public Select<T> where(Action1<Where<T>> where) {
        wheres.add(where);
        return this;
    }

    public Select<T> orderBy(SinglePredicate<T> lambda) {
        return this;
    }

    public String toSql() {
        StringBuilder sb = new StringBuilder();
        for (Action1<Where<T>> where : wheres) {
            Where w = new Where();
            where.apply(w);
            var r = w.end();
            if (sb.length() != 0) {
                sb.append(" AND (" + r + ") ");
            } else {
                sb.append("(" + r + ")");
            }
        }
        return sb.toString();
    }
}

