package orm.select;

import orm.analysis.LambdaToSql;
import orm.delegates.SinglePredicate;
import orm.delegates.SqlPredicate;

import java.util.ArrayList;
import java.util.List;

public class Select<T> {
    private List<WhereObject<T>> whereCollect = new ArrayList<>();
    private static final int and = 1;
    private static final int or = 0;

    public Select<T> whereAnd(SqlPredicate<T> lambda) {
        whereCollect.add(new WhereObject<T>(and, lambda));
        return this;
    }

    public Select<T> whereOr(SqlPredicate<T> lambda) {
        whereCollect.add(new WhereObject<T>(or, lambda));
        return this;
    }

    public Select<T> orderBy(SinglePredicate<T> lambda) {
        return this;
    }

    public String toSql() {
        LambdaToSql<T> lambdaAnalysis = new LambdaToSql<T>();
        StringBuilder sb = new StringBuilder();
        // 遍历
        for (WhereObject<T> where : whereCollect) {
            int type = where.type;
            String sql = lambdaAnalysis.toSql(where.lambda);
            if (type == or) {
                sb.append(" OR (" + sql + ") ");
            } else if (type == and) {
                if (sb.length() != 0){
                    sb.append(" AND (" + sql + ") ");
                }else{
                    sb.append(" (" + sql + ") ");
                }

            }

        }
        return sb.toString();
    }
}

