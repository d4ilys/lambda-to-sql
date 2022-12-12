# lambda-to-sql

将lambda解析为sql where语句，后期升级为orm

> 复杂的查询条件

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

> 简单的查询条件

**因为Java的ExpressionTree是第三方实现 or 的解析存在问题，只能解析简单的表达式，如果查询条件复杂只能是上面的写法**

~~~JAVA
String sqlSimple = new SelectSimple<Person>()
    .where(p -> p.getName().startsWith("张") && p.getAge() < 10)
    .where(p -> list.contains(p.getId()))
    .toSql();
~~~

**结果为**

~~~c#
name LIKE '%张' AND age < '10' AND id IN ('1','2') 
~~~

