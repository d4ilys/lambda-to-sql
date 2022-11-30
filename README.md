# lambda-to-sql

将lambda解析为sql where语句，后期升级为orm

~~~C#
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
~~~

**结果为**

~~~sql
(age > '15'AND name LIKE '%张'  AND id <> '666' OR id IN ('1','2') AND age < '15' ) AND (name <> 'tom'AND name LIKE '%daily%'  ) 
~~~

