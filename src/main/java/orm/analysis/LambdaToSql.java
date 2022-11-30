package orm.analysis;


import co.streamx.fluent.extree.expression.LambdaExpression;
import orm.delegates.SqlPredicate;

public class LambdaToSql<T>  {
    public String toSql(SqlPredicate<T> predicate) {
        LambdaExpression<SqlPredicate<T>> lambdaExpression = LambdaExpression.parse(predicate);
        ToSqlVisitor toSqlVisitor = new ToSqlVisitor();
        lambdaExpression.accept(toSqlVisitor);
        return toSqlVisitor.GetWhere();
    }
}
