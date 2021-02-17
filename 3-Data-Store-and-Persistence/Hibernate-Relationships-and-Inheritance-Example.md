# Hibernate Relationship and Inheritance - Example

- `Plant` is a parent class. It contains the attributes `name, price and id`
- `Flower` is a subclass of `Plant`. It has the attribute `color`
- `Shrub` is a sub class of `Plant`. It has the attributes `height and width`
-  table `Plant` to store all the shared data, the flowers and shrubs should have separate tables for their unique fields
- `Delivery` is a class that has oneToMany relationship with `Plants`. It includes a list of all the `Flowers and Shrubs`
- store the association in the ‘plant’ table in a column called `delivery_id`.

```java
// Uses InheritanceType.JOINED to store shared fields in 'plant' and unique fields
// in subclass tables
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Plant {
   @Id
   @GeneratedValue
   private Long id;

   @Nationalized // should use @Nationalized instead of @Type=nstring
   private String name;
   @Column(precision=12, scale=4)
   private BigDecimal price; // BigDecimal is the standard Java class for currency math

   @ManyToOne //many plants can belong to one delivery
   @JoinColumn(name = "delivery_id")  //map the join column in the plant table
   private Delivery delivery;

   /* getters and setters*/
}
```
```java
@Entity
public class Flower extends Plant {
   private String color;

   /* getters and setters*/
}
```
```java
@Entity
public class Shrub extends Plant {
   private int heightCm; //any reasonable unit of measurement is fine
   private int widthCm;

   /* getters and setters*/
}
```
```java
@Entity
public class Delivery {
   @Id
  @GeneratedValue
   private Long id;

   @Nationalized
   private String name;
   @Column(name = "address_full", length = 500)
   private String address;
   private LocalDateTime deliveryTime;
   @Type(type = "yes_no")
   private Boolean completed;

   //make sure to specify mappedBy. Lazy fetch optional,
   //  but often a good idea for collection attributes
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "delivery")
   private List<Plant> plants;

   /* getters and setters */
}
```