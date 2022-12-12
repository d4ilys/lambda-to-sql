package orm;

import orm.select.Select;
import orm.select.SelectSimple;
import orm.select.Where;

import java.util.ArrayList;
import java.util.List;


public class Test {
    public static void main(String[] args) throws Exception {
        ExpressionTest();
    }

    static void ExpressionTest() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        //复杂的查询条件
        String sql = new Select<Person>()
                .where(w -> w.where(p -> p.getAge() > 15)
                        .and(p -> p.getName().startsWith("张"))
                        .and(p -> p.getId() != "666")
                        .or(p -> p.getAge() < 15 && p.sex == "男" && p.type == "正常人" && p.getAge() > 5))
                .where(w -> w.where(p -> p.getName() != "tom")
                        .and(p -> p.getName().contains("daily")))
                .orderBy(p -> p.getAge())
                .toSql();

        //简单的查询条件
        //TODO 因为Java的ExpressionTree是第三方实现 or 的解析存在问题，只能解析简单的表达式
        String sqlSimple = new SelectSimple<Person>()
                .where(p -> p.getName().startsWith("张") && p.getAge() < 10)
                .where(p -> list.contains(p.getId()))
                .toSql();
        System.out.println(sqlSimple);
    }
}

