### Creating Initial Database

Before using your application to connect to a database server, you should create a database instance on that server to store your information. Then you should create an admin user for that database. You can use the MySQL Workbench to do this, or any other tool that allows you to execute sql against your database.

```
```
CREATE SCHEMA `plant` ; -- Create the plant database
```
create user 'sa'@'localhost' identified by 'sa1234'; -- Create the user
grant all on exercise1.* to 'sa'@'localhost'; -- Gives all privileges to that user on new db
```

Dependecy
```
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```

```
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/plant
spring.datasource.username=sa
spring.datasource.password=sa1234
```