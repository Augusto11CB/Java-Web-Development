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

Make sure to disable hibernate initialization if you use these scripts to avoid conflicts:

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