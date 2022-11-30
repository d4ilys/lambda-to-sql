package orm;

import orm.select.Select;

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
        String sql = new Select<Person>()
                .whereAnd(p -> p.getAge() > 5 && p.getName().startsWith("张"))
                .whereAnd(p -> p.getId() != "666" && p.getName().contains("刘"))
                .whereOr(p -> list.contains(p.getId()) && p.getAge() < 15)
                .orderBy(p -> p.getAge())
                .toSql();
        System.out.println(sql);
    }
}

