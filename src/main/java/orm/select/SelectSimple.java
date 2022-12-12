package orm.select;

import orm.analysis.LambdaToSql;
import orm.delegates.Action1;
import orm.delegates.Func1;
import orm.delegates.SinglePredicate;

import java.util.ArrayList;
import java.util.List;

public class SelectSimple<T> {
    private List<Func1<T>> wheres = new ArrayList<>();

    public SelectSimple<T> where(Func1<T> where) {
        wheres.add(where);
        return this;
    }

    public SelectSimple<T> orderBy(SinglePredicate<T> lambda) {
        return this;
    }

    public String toSql() {
        LambdaToSql<T> lambdaAnalysis = new LambdaToSql<T>();
        var sb = new StringBuilder();
        for (Func1<T> where : wheres) {
            String sql = lambdaAnalysis.toSql(where);
            if (sb.length() != 0) {
                sb.append(" AND " + sql + " ");
            } else {
                sb.append(sql);
            }
        }
        return sb.toString();
    }
}

