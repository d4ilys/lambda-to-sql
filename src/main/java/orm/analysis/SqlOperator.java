package orm.analysis;

import co.streamx.fluent.extree.expression.ExpressionType;
import com.bestvike.linq.Linq;

import java.util.ArrayList;
import java.util.List;

public class SqlOperator {
    static List<String> methods = new ArrayList<>();

    static {
        methods.add("contains");
        methods.add("startsWith");
        methods.add("endsWith");
    }

    static String relatorHandler(int type) {
        switch (type) {
            case ExpressionType.LogicalAnd:
                return "AND";
            case ExpressionType.LogicalOr:
                return "OR";
            case ExpressionType.LogicalNot:
                return "NOT";
            case ExpressionType.NotEqual:
                return "<>";
            case ExpressionType.GreaterThan:
                return ">";
            case ExpressionType.GreaterThanOrEqual:
                return ">=";
            case ExpressionType.LessThan:
                return "<";
            case ExpressionType.LessThanOrEqual:
                return "<=";
            case ExpressionType.Equal:
                return "=";
            case ExpressionType.Convert:
                return "";
            default:
                return "";
        }

    }

    /*
     *  0 字段的 get方法，或者字段名称
     *  1 字段类型的方法  p.getName().startsWith('');
     *  2 list类型，一般生成 in
     * */
    static int methodIsHandler(String name, String type) {
        if (Linq.of(methods).any(s -> s == name)) {
            if (type.equals("interface java.util.List"))
                return 2;
            return 1;
        }
        return 0;
    }

    static String InHandler(List<String> list) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= list.size() - 1; i++) {
            if (i < list.size() - 1) {
                sb.append("'" + list.get(i) + "'" + ",");
            } else {
                sb.append("'" + list.get(i) + "'" );
            }
        }
        return sb.toString();
    }
}