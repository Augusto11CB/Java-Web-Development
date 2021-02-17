# Persistence without JPA
## SQL Initialization Scripts
Spring attempts to execute two scripts by default when the application starts:

-   schema.sql - Create or update the schema.
-   data.sql - Initialize or modify the data in your tables.

The default directory for these files is  `src/main/resources`

### Properties
Still controlled by the same property:
```
spring.datasource.initialization-mode=[always|embedded|never]
```

**Make sure to disable hibernate initialization if you use these scripts to avoid conflicts:**

```
spring.jpa.hibernate.ddl-auto=none
```

### Platform-specific initialization
May provide additional initialization scripts using the naming pattern:

-   schema-${platform}.sql
-   data-${platform}.sql

By setting the platform property, you can control which scripts get loaded:

```
spring.datasource.platform=foo 
```

The above property will cause Spring to try and execute  `schema-foo.sql`  and  `data-foo.sql`.

### Initialization script in `data.sql`
use the `INSERT IGNORE` syntax to ignore attempts to insert values that already exist. For example:
```sql
insert ignore into candy
   set id = 1,
       name = 'Gummy Badgers',
       price = '3.50';
```

## Data Object Design - SQL-based persistence layers
SQL-based persistence layers often use Data Objects to map to their database tables. Data Objects usually have a **one-to-one** mapping of table fields to object attributes, though projections are frequently used outside ORM as well.

The below images demonstrate how associations are often mapped into secondary id fields of Data Objects rather than populating the associated objects.

![](resources/sql-based-persistence-layers-representation-association-data-object.png)

## Data Access Objects

### DAO vs. Repository
The Data Access Object (DAO) pattern is an alternative to the Repository pattern. A DAO is a programmatic interface to a table or collection of related tables. Whereas a Repository presents a collection of Entities that you modify to update the database, a DAO will often provide methods that expose various actions, along with standard CRUD operations.

![](resources/repository-vs-dao.png)

### Data Object Projection Scope
Data Object scope can vary depending on the needs of the application. It is more common to project directly into DTOs from a DAO, but remember that  `@JSONView`  still works with Data Objects, so it is often still worth adhering to a similar set of boundaries to what you would observe with Entities.

### DAO pattern in Hibernate
While the Repository pattern is common with Hibernate, because **most modifications can be done simply by editing Entities**, there is nothing that prevents us from using the DAO pattern in either a Hibernate or SQL-based persistence model.

## JDBCTemplate
`JdbcTemplate`  functions like the EntityManager: it provides connections, executes queries, and manages transactions.
`NamedParameterJdbcTemplate`  allows you to use named parameters in your query string.

```
-- with standard JdbcTemplate
select * from person where id = ? and age >= ?

-- with NamedParameterJdbcTemplate
select * from person where id = :id and age >= :minAge
```

### queryForObject
To query for a single object, you can use the  `jdbcTemplate.queryForObject`  method. This example takes three parameters:

1.  Query String
2.  A map of parameter names to parameter values
3.  A RowMapper that returns the instance of the object for which we’re querying

```java
@Autowired
NamedParameterJdbcTemplate jdbcTemplate;

private static final String SELECT_PERSON_BY_ID = 
   "SELECT * FROM person " + 
   "WHERE id = :id";

public PersonData getPersonById(Long id){
   return jdbcTemplate.queryForObject(
           SELECT_PERSON_BY_ID,
           new MapSqlParameterSource().addValue("id", id),
           new BeanPropertyRowMapper<>(PersonData.class));
}
```

### update
The  `jdbcTemplate.update`  method allows you to execute SQL statements that modify the database, such as  `INSERT`,  `UPDATE`, or  `DELETE`.

Instead of taking a RowMapper, it takes an optional KeyHolder object that can contain the id of the new row.

```java
private static final String INSERT_PERSON = 
   "INSERT INTO person (name, age, favorite_composer) " + 
   "VALUES(:name, :age, :favoriteComposer)";

public Long addPerson(PersonData personData) {
   KeyHolder key = new GeneratedKeyHolder();
   jdbcTemplate.update(
           INSERT_PERSON,
           new BeanPropertySqlParameterSource(personData),
           key);
   return key.getKey().longValue();
}
```

### SimpleJdbcInsert
The  `SimpleJdbcInsert`  class allows us to do inserts without writing a query, which can make it easier to maintain our code if the composition of our tables change.

```java
// does the same as the previous addPerson method
public Long addPersonInsert(PersonData personData) {
   SimpleJdbcInsert sji = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
           .withTableName("person")
           .usingGeneratedKeyColumns("id");
   return sji.executeAndReturnKey(new BeanPropertySqlParameterSource(personData)).longValue();
}
```

### Querying for Complex Objects
#### OneToOne with RowMapper
```java
public class PersonWithOneOutfit {
   private String name;
   private int age;
   private String favoriteComposer;
   private OutfitData outfit;
  /* getters and setters */
}
```

We can populate an object representing a OneToOne relationship between tables with a single query by combining multiple ``RowMapper` ``s in the same statement. The query returns all the values necessary to build both objects, and then we use an anonymous RowMapper to make the results into two different objects.

```java
private static final String SELECT_PERSON_WITH_ONE_OUTFIT =
       "SELECT * FROM person p " +
               "JOIN outfit o " +
               "ON p.id = o.person_id " +
               "WHERE p.id = :personId AND o.id = :outfitId";


private static final BeanPropertyRowMapper<PersonWithOneOutfit> personWithOneOutfitRowMapper = new BeanPropertyRowMapper<>(PersonWithOneOutfit.class);
private static final BeanPropertyRowMapper<OutfitData> outfitRowMapper = new BeanPropertyRowMapper<>(OutfitData.class);

public PersonWithOneOutfit addOutfitForPerson(Long personId, OutfitData outfitData) {
   outfitData.setPersonId(personId);
   Long outfitId = addOutfit(personId, outfitData);

   return jdbcTemplate.queryForObject(SELECT_PERSON_WITH_ONE_OUTFIT,
           new MapSqlParameterSource()
                   .addValue("personId", personId)
                   .addValue("outfitId", outfitId),
           //anonymous row mapper lambda
           (resultSet, i) -> {
               PersonWithOneOutfit person = personWithOneOutfitRowMapper.mapRow(resultSet, i);
               person.setOutfit(outfitRowMapper.mapRow(resultSet, i));
               return person;
           });
}
```

#### OneToMany with ResultSetExtractor
```java
public class PersonWithAllOutfits {
   private String name;
   private int age;
   private String favoriteComposer;
   private List<OutfitData> outfits;
}
```

If you want to construct an object with more complex relationships, you have two main choices:

1.  Query twice and assemble the objects
2.  Query once and loop through every row to build objects from the result set

**jdbcTemplate.query**
To retrieve a list of items representing the second part of your OneToMany association, you can use jdbcTemplate.query. Here is a query returning all the outfits for a specific person. By using this in conjunction with a query to retrieve a single PersonData, you can then populate PersonWithAllOutfits.

```java
private static final String SELECT_OUTFIT_BY_PERSON_ID = "SELECT * FROM outfit o WHERE o.person_id = :personId";

public List<OutfitData> getOutfitsByPerson(Long personId){
   return jdbcTemplate.query(SELECT_OUTFIT_BY_PERSON_ID,
           new MapSqlParameterSource().addValue("personId", personId),
           new BeanPropertyRowMapper<>(OutfitData.class));
}
```

**ResultSetExtractor**
If we wrote a single query that retrieved all the data for both  `PersonData`  and our  `List<OutfitData>`, we can use a  `ResultSetExtractor`  to build the objects for us from the data.

```java
private static final String SELECT_PERSON_WITH_ALL_OUTFITS =
       "SELECT * FROM person p " +
       "JOIN outfit o " +
       "ON p.id = o.person_id " +
       "WHERE p.id = :personId";

private static final BeanPropertyRowMapper<PersonWithAllOutfits> personWithAllOutfitsRowMapper = new BeanPropertyRowMapper<>(PersonWithAllOutfits.class);
public PersonWithAllOutfits addOutFitForPersonReturnAllFancy(Long personId, OutfitData outfitData) {
   //add the outfit to db
   outfitData.setPersonId(personId);
   addOutfit(personId, outfitData);

   //iterate over the result set to construct the outfit list
   return jdbcTemplate.query(
           SELECT_PERSON_WITH_ALL_OUTFITS,
           new MapSqlParameterSource().addValue("personId", personId),
           //anonymous ResultSetExtractor lambda
           resultSet -> {
               PersonWithAllOutfits person = null;
               List<OutfitData> outfits = new ArrayList<>();
               int row = 0;
               while(resultSet.next()) {
                   if(person == null){
                       person = personWithAllOutfitsRowMapper.mapRow(resultSet, row);
                   }
                   outfits.add(outfitRowMapper.mapRow(resultSet, row++));
               }
               if(person != null) {
                   person.setOutfits(outfits);
               }
               return person;
           });
}
```

## Transactions in JDBC
### @Transactional
Many operations in DAOs involve combining multiple queries into a single request, so transaction management is just as important as before! Fortunately,  `JdbcTemplate`  provides a Spring-managed connection, so we can simply use  `@Transactional`  to set our transaction boundaries by method just like we did when using Hibernate.