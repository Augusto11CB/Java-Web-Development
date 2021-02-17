# Java Persistence API - JPA
## Definitions
-   **Persistence Context:**  Describes the relationship between all the Entity instances in our program and their representations in the underlying database.
-   **Instance:**  A specific copy of an Entity in program memory.

## Persistence Context Entity States
**Transient:**  not associated with the persistence context. Often has not yet had an ID assigned.  
**Managed:**  persistent. Managed by the current persistence context. Changes to the entity will be reflected in the backing database.  
**Detached:**  previously managed. Occurs to all managed entities when persistence context ends.  
**Removed:**  scheduled to be removed from the database. Java object still exists and has ID.

### Additional Resources
[Hibernate Documentation on Persistence Context](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#pc)

## Entity Manager
Entity managers have the role of creating and deleting persistent entities, but also they can find entities in the database or execute other queries.

### Injecting EntityManager - `@PersistenceContext`
```java
@PersistenceContext
EntityManager entityManager;

public void persistExample(Person p) {
   entityManager.persist(p); //write p to the database
   p.setFavoriteComposer("Johann Strauss II"); //will update database
}
```

### Changing Persistence States
**Persist:**  Takes an Entity not yet managed. The **Entity becomes managed** and will be saved to the database.  
**Find:**  Looks up an id in the database and returns a managed Entity.  
**Merge:**  Updates an Entity that is in the detached state. Returns an instance of that Entity that is now managed. If Entity was not found in the database to update, persists Entity as a new row.  
**Remove:**  Detaches an entity and deletes it from the database.

#### Persist

```java
public void persistExample(Person p) {
   entityManager.persist(p); //write p to the database
   p.setFavoriteComposer("Johann Strauss II"); //will update database
}
```

#### Find
```java
public void findExample(Long id) {
   Person p = entityManager.find(Person.class, id); //retrieve an instance by its key
   p.setFavoriteComposer("Sir Malcolm Arnold"); // will update database, because now it is in the state MANAGED
}
```
"If we wanted to change the values of this entity in the database, we could simply change the values of the returned objects and **the changes would be persisted when the _persistence context closes_**

`entityManager.getReference`: Instead returning the entity itself, `getReference` returns a proxy objects that does not load the values of any attributes until they are directly referenced. This is useful if a copy is needed and whats to associate with another object, but do not want change its values.
```java
public void getReferenceExample(Long personId, Long outfitId) {
   Person p = entityManager.find(Person.class, personId);
   Outfit outfitReference = entityManager.getReference(Outfit.class, outfitId);
   p.getOutfits().add(outfitReference);
}
```

#### Merge
```java
public void mergeExample(Person detachedPerson){
   detachedPerson.setFavoriteComposer("Rimsky Korsakov");
   
   Person managedPerson = entityManager.merge(detachedPerson);
   
   detachedPerson.setFavoriteComposer("Antonio Salieri"); //will have no effect on database
   managedPerson.setFavoriteComposer("C.P.E. Bach"); //will overwrite Korsakov
}
```

#### Merge vs. Persistence
If a entity that does not exist yet were used as a parameter for the `merge()` method, Hibernate will create a new entity and return the **managed** copy. However it is not recommended to use `merge` for this purpose  because every merge call has to look up the existing object first.

#### delete
```java
public void deleteExample(Long id) {
   Person p = entityManager.find(Person.class, id); //retrieve an instance by its key
   entityManager.remove(p); //will delete row from database
}
```

## Lazy Loading
Setting a fetch strategy can prevent your Entities from loading associated values until those values are referenced.

### FetchType.EAGER
Always retrieve the associated values as part of the Entity retrieval. This means the initial query for the entity retrieves this data.

### FetchType.LAZY 
Wait to retrieve associated values until they are referenced. Lazy-loaded attributes are Hibernate proxy objects whose specific values are retrieved from the database only if they’re accessed. The initial query for the entity will NOT retrieve this data.

### Default FetchTypes
#### `FetchType.LAZY`
- @OneToMany
- @ManyToMany

#### FetchType.EAGER
- @ManyToOne
- @OneToOne

### Example
```java
@Entity
public class Person {

   @Id
   @GeneratedValue
   Long id;

   @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
   List<Outfit> outfits;

   /* rest of class */
}
```

## Cascading
JPA allows you to propagate the state transition from a parent entity to a child. For this purpose, the JPA `javax.persistence.CascadeType` defines various cascade types:

Valid CascadeTypes correspond to the different persistence operations, such as `Persist`, `Merge`, and `Remove`.

CascadeType.ALL includes all **Persistence States**

```
@Entity
public class Person {

   @Id
   @GeneratedValue
   Long id;

   @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
   List<Outfit> outfits;

   /* rest of class */
}
```

## Queries JPQL
JPQL allows to write queries for the EntityManager that return objects directly. Their syntax is very similar to SQL, but we reference Entities instead of tables.

### SQL
```
SELECT * 
FROM   person p /* Table name */ 
WHERE  p.favoritecomposer LIKE '%Sibelius%' /* Column */
```

### JPQL
```
SELECT p 
FROM   person p /* Entity */ 
WHERE  p.favoritecomposer LIKE '%Sibelius%' /* Attribute */
```

### Creating a JPQL Query
To create a query, inject an entityManager into a class and then use the  `createQuery`  method. This method returns different types of Query objects depending on your parameters. `TypedQuery` is recommended for clarity

```java
private static final String FIND_PERSON_BY_COMPOSER =
       "select p from Person p " +
       "where p.favoriteComposer like :favoriteComposer";

public Person findPersonByFavoriteComposer(String favoriteComposer){
   TypedQuery<Person> query = entityManager.createQuery(FIND_PERSON_BY_COMPOSER, Person.class);
   query.setParameter("favoriteComposer", favoriteComposer);
   return query.getSingleResult();
}
```

### Referencing Associated Entities
In SQL, often tables are joined together to search for results by related Entities. In JPQL, it is possible reference the value of associated Entities by accessing them directly as Entity attributes in the query.

```java
private static final String FIND_HUMANOID_BY_OUTFIT =
       "select h from Humanoid h " +
       "where :outfit member of h.outfits";

List<Humanoid> findHumanoidByOutfit(Outfit o){
   TypedQuery<Humanoid> query = entityManager.createQuery(FIND_HUMANOID_BY_OUTFIT, Humanoid.class);
   query.setParameter("outfit", o);
   return query.getResultList();
}
```
### Additional Resources
-   [Hibernate Documentation on queries](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#hql)

## Named Queries - `@NamedQueries` and `@NamedQuery`
- Named queries can be useful for organizing queries by class.
- They also allow to **compiler-check** the queries for validity at build time.
- **Any named queries that reference invalid entities will throw exceptions**
- To use them, **declare them at the top of the Entity class** to which they refer.
- Their names are global across the whole persistence unit, so they should all have unique names.

```java
@NamedQueries({
 @NamedQuery(
  name = "Outfit.findByHat", 
  query = "select o from Outfit o where o.hat = :hat"),
 @NamedQuery(
  name = "Outfit.findBySock", 
  query = "select o from Outfit o where o.sock = :sock")
})
```

## Criteria Builder
Helps build queries dynamically using Java code.

### JPQL Version
```
SELECT h FROM Humanoid h
WHERE :outfit MEMBER OF h.outfits
```

### Criteria Builder Version
```java
List<Humanoid> findHumanoidByOutfitCriteria(Outfit o) {
   CriteriaBuilder cb = entityManager.getCriteriaBuilder();
   CriteriaQuery<Humanoid> criteria = cb.createQuery(Humanoid.class);
   Root<Humanoid> root = criteria.from(Humanoid.class);

   criteria.select(root).where(cb.isMember(o, root.get("outfits")));
   return entityManager.createQuery(criteria).getResultList();
}
```

## Projections
Projections allow to return **non-Entity** data from queries.

### Projecting into a Value Type
```java
private static final String LIST_FAVORITE_COMPOSERS = "select distinct p.favoriteComposer from Person p";

List<String> listFavoriteComposers() {
   TypedQuery<String> query = entityManager.createQuery(LIST_FAVORITE_COMPOSERS, String.class);
   return query.getResultList();
}
```

### Projecting into a non-Entity Object

```java
public class PersonComposerDTO {
   private String name;
   private String composer;

   public PersonComposerDTO(String name, String composer) {
       this.name = name;
       this.composer = composer;
   }
   /* getters and setters */
}
```

```java
private static final String GET_PERSON_AND_COMPOSER =
       "select new aug.bueno.entity.dto.PersonComposerDTO(p.name, p.favoriteComposer) " +
       "from Person p " +
       "where p.id = :id";

PersonComposerDTO getPersonComposer(Long id) {
   TypedQuery<PersonComposerDTO> query = entityManager.createQuery(GET_PERSON_AND_COMPOSER, PersonComposerDTO.class);
   query.setParameter("id", id);
   return query.getSingleResult();
}
```

### When should you use projections?
- When it is only needed to return a single value type;
- When an object is needed that contains data from two unrelated Entities.

### Additional Resources
[Hibernate Projections documentation](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#hql-api-scroll)

## Spring Data JPA - Query Methods
[Spring Doc - Defining Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details)

## Transactions and Flushing

[AugustoCalado - Spring Transaction Management](https://github.com/AugustoCalado/Spring-Framework-Studies/blob/master/Spring%20Transaction%20Management.md)

### Definitions
-   **Flushing:**  The process of synchronizing the state of the persistence context with the underlying database.
-   **Transaction:**  A set of operations that either succeed or fail as a group. By telling the program where to start and end transactions, it is possible to describe groups of operations that must complete in their entirety or  else do nothing
-   **Level 1 Cache:**  The Persistence Context functions as a  **Level 1 Cache**, because it does not write changes to the database until  **Flushing**  occurs.

### Transactions
If multiple persistence operations were executed, a failure on one could leave the Database in an inconsistent state. By wrapping multiple operations in a Transaction, no changes will be applied unless all operations succeed.  

**A good practice is to start one Transaction for each request that interacts with the database.** The simplest way to do this in Spring is through the **@Transactional** annotation. 

> - You can annotate methods to begin a transaction when the method starts and close it when you leave. 
> - You can also annotate classes to treat all their methods as @Transactional. 

#### Transaction-Per-Request
This annotation is best done at the Service layer, so a new transaction is started whenever the Controller classes request operations that may involve the database.

**Advantages**
- Allows Persistence Context function as cache;
- Minimize number of transactions creaated;
- Avoids locking resources across requests.

### Extra Persistence Context To Flush Changes
- Query overlaps with queued entity actions: When a query requests information that the persistence context plans to change.
	- Ex: If there a query that finds a person based on their hat, and they have a pending modification to the outfit table, this modification is likely to be flushed before the query `findByHat` executes.

-  Native SQL Queries: Any native SQL query that has not registred which entities it affects. Since the persistence context doesn't know if the query requires entities that have pending updates or not, it flushs every thing to be safe.

### Additional Resources
[Hibernate Documentation on Flushing](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#flushing)