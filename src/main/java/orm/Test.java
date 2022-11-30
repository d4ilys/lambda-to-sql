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
                .where(w -> w.where(p -> p.getAge() > 15)
                        .and(p -> p.getName().startsWith("张"))
                        .and(p -> p.getId() != "666")
                        .or(p -> list.contains(p.getId()))
                        .and(p -> p.getAge() < 15))
                .where(w -> w.where(p -> p.getName() != "tom")
                        .and(p -> p.getName().contains("daily")))
                .orderBy(p -> p.getAge())
                .toSql();
        System.out.println(sql);
    }
}

