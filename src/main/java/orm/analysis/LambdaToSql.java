package orm.analysis;


import co.streamx.fluent.extree.expression.LambdaExpression;
import orm.delegates.Func1;

public class LambdaToSql<T>  {
    public String toSql(Func1<T> predicate) {
        LambdaExpression<Func1<T>> lambdaExpression = LambdaExpression.parse(predicate);
        ToSqlVisitor toSqlVisitor = new ToSqlVisitor();
        lambdaExpression.accept(toSqlVisitor);
        return toSqlVisitor.GetWhere();
    }
}
