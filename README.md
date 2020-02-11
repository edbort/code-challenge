# Husky challenge implementation!

Here it is my implementation of Husky challenge, using Java with SpringBoot.

## Requiriment

In order to build and run you need:

1 - A Postgres database.
2 - A Memcache daemon (memcached on linux).
3 - Java 8.
4 - Maven.

## Build and Run

First of all, consider changing some settings in application.properties file.

```
spring.jpa.database=POSTGRESQL
spring.datasource.platform=postgres
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update
spring.database.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/husky-code-challene
spring.datasource.username=postgres
spring.datasource.password=postgres
memcache.host=localhost
memcache.port=11211
```

A postgres database should be create before running the application. I also included a MySQL driver, despite my will.

Java 8 and Maven should already be installed and available on shell. 

```
git clone https://github.com/edbort/code-challenge.git
cd code-challenge
mvn package
```

I can send you guys the final jar file so you don't need to care about the compilling proccess.

The server (tomcat) will listen to TCP port 8080 as default but you can easly change it.

```
java -jar ./target/husky-code-challenge-0.0.1-SNAPSHOT.jar --server.port=8080
```

At the moment, the application does not expected any kind of authentication from MemCache server. You can only set the host and port to connect to.

As soon as the application started, for the first time, a new table will be created in the specified database and a lot of records (mock data) will be created. It can take some time, please be patient. Something arround 400.000 will be created for tests purposes.

After that, the App will build all caches for all 200 accounts (id from 1 to 200).

The message "Application is Ready!" will be displayed after the boot proccess. 
