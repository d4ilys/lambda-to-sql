package orm.select;

import orm.analysis.LambdaToSql;
import orm.delegates.Func1;

import java.util.ArrayList;
import java.util.List;

public class Where<T> {
    private List<WhereObject<T>> whereCollect = new ArrayList<>();
    private static final int and = 1;
    private static final int or = 0;

    public Where<T> where(Func1<T> lambda) {
        whereCollect.add(new WhereObject<T>(and, lambda));
        return this;
    }
    public Where<T> and(Func1<T> lambda) {
        whereCollect.add(new WhereObject<T>(and, lambda));
        return this;
    }

    public Where<T> or(Func1<T> lambda) {
        whereCollect.add(new WhereObject<T>(or, lambda));
        return this;
    }
    public String end() {
        LambdaToSql<T> lambdaAnalysis = new LambdaToSql<T>();
        StringBuilder sb = new StringBuilder();
        // 遍历
        for (WhereObject<T> where : whereCollect) {
            int type = where.type;
            String sql = lambdaAnalysis.toSql(where.lambda);
            if (type == or) {
                sb.append("OR " + sql + " ");
            } else if (type == and) {
                if (sb.length() != 0){
                    sb.append("AND " + sql + " ");
                }else{
                    sb.append(sql);
                }

            }

        }
        return sb.toString();
    }
}
