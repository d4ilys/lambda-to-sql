# lambda-to-sql

将lambda解析为sql where语句，后期升级为orm

~~~C#
List<String> list = new ArrayList<>();
list.add("1");
list.add("2");
String sql = new Select<Person>()
    .whereAnd(p -> p.getAge() > 5 && p.getName().startsWith("张"))
    .whereAnd(p -> p.getId() != "666" && p.getName().contains("刘"))
    .whereOr(p -> list.contains(p.getId()) && p.getAge() < 15)
    .orderBy(p -> p.getAge())
    .toSql();
~~~

**结果为**

~~~sql
 (age > 5 AND name LIKE '%张' )  AND (id <> 666 AND name LIKE '%刘%' )  OR (id IN ('1','2') AND age < 15) 
~~~

