# Husky challenge implementation!

Here it is my implementation of Husky challenge, using Java with SpringBoot.

## Requirement

In order to build and run you will need:

1. A Postgres database.
2. A Memcache daemon (memcached on linux).
3. Java 8.
4. Maven.

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

I could send you guys the final JAR file so you don't need to care about the compilation proccess.

The server (an embedded tomcat) will listen to TCP port 8080 as default, but you can easly change that.

```
java -jar ./target/husky-code-challenge-0.0.1-SNAPSHOT.jar --server.port=8080
```

At the moment, the application does not expected any kind of authentication from MemCache server. You can only set the host and port to connect to.

As soon as the application started, for the first time, a new table will be created in the specified database and a lot of records (mock data) will be created. It can take some time, please be patient. Something arround 400.000 will be created for tests purposes.

After that, the App will build all caches for all accounts (200 at the moment). For each account, a small cache will be created in order to grouping, per day, all account movements for a specific date. Caches for the lasts 3, 15, 30, 60 and 90 days (including today) will also be created. 

The message "Application is Ready!" will be displayed after the boot proccess. 

## Testing

Take a look at postman file (or Java Classes at controller package) to find out what could be done.

[Postman file](https://github.com/edbort/code-challenge/blob/master/Husky%20Challeng%20Test.postman_collection.json)

## The implementation

The DailyBaseCache is the is the last, the smallest doll, and represents all moviments for an specific account, for a specific date (single day).

to be continued


