# Husky challenge implementation!

Here it is my implementation of Husky challenge using Java with SpringBoot.

## Requirements

In order to build and run you will need:

1. A Postgres database
2. A Memcache daemon (memcached on linux)
3. Java 8
4. Maven

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

Create an specifique database on Postgres to use within the application. The default configuration will try to connect to **husky-code-challene**.

Java 8 and Maven should already be installed and available on shell. 

```
git clone https://github.com/edbort/code-challenge.git
cd code-challenge
mvn package -DskipTests
```

**I could send you guys the final JAR file so you don't need to care about the compilation proccess.**

The parameter **-DskipTests** is mandatory at the first time because the table and the mock data will be created only after the first execution. After that, running the coomand **mvn package** will perform the created [Unit Test case](https://github.com/edbort/code-challenge/blob/master/src/test/java/bortolin/edison/husky/code/challenge/test/CacheTest.java) in order to check if the requests is cached the result, as expected. 

The server (an embedded Tomcat) will listen to TCP port 8080 as default, but you can easly change it.

```
java -jar ./target/husky-code-challenge-0.0.1-SNAPSHOT.jar --server.port=8080
```

At the moment, the application does not expect any kind of authentication from MemCache server. You can only set the host and  the port to connect to.

As soon as the application starts, for the first time, a new table will be created in the specified database and a lot of records (mock data) will be created. It can take some time, please be patient. Something arround 400.000 records will be created for tests purposes.

After that, the App will build all caches for all accounts (200 accounts). For each account, a small cache will be created in order to grouping, per day, all account movements for a specific date. Caches for the lasts 3, 15, 30, 60 and 90 days will also be created. 

The message **"Application is Ready!"** will be displayed after the boot proccess is finished.

## The Cache Strategy Implementation

The **DailyBaseCache** is the the last, the smallest doll, and represents all moviments for an specific account, for a specific date (a single day). This cache is built on application's boot and it is seted to never expires. Whe a new transaction is created the DailyBaseCache, based on transaction date, will also be updated. This cache's key is defined using account's id and a date formated with YYYMMDD. Example: **50-20200210** // Account id = 50, Date = 2020-02-10

The other cache is called **CacheLastNDays** and contains the keys for all other **DailyBaseCache**. The application will create caches for the lasts **3**, **15**, **30**, **60** and **90** days on application's boot process, for each account in the system. Every day, at 00:00:00, all these caches will expires and a new ones will be created. As it contains only the keys for the others caches, it does not need to be updated when a new transaction is created. This cache's key is defined using accoun's id and the number os days followed by the letter **d**. Example: **50-3d** // Account id = 50, Period = The lasts 3 days.

If the MemCache server is not available at the application's boot proccess, or if the MemCache server becomes unavailable in some time, it will not crash the system. The App will remain working respond to requests using database queries.

An admin API was created to maintain MemCache server correctly integrated. 

## Using the API

Take a look at postmans files (or Java Classes at controller package) to find out what requests could be made.

[Postman file](https://github.com/edbort/code-challenge/blob/master/Husky%20Challeng%20Test.postman_collection.json)

[Postman Environment](https://github.com/edbort/codechallenge/blob/master/Husky%20Challenge%20Test.postman_environment.json)

## Stress Test

JMeter was configure to use 12 threads simultaneously, as my computer has 12 cores. Each thread should make 5.000 HTTP GET requests to the server.

```
HTTP Request Example: localhost:8080/account/movement/50/3
```
The account id and the desired period was randomly defined.

The JMeter project file could be downloaded [here](https://github.com/edbort/code-challenge/blob/master/stress-test/Husky%20Challenge%20Stress%20Test.jmx)

**After perform the test using a query database method (localhost:8080/account/movement/db/account_id/period) and perform another test using MemCache method (localhost:8080/account/movement/account_id/period), as describe bellow, I realized that the database query method was harmed since I've forgot to create an index using account_id and date fields from account_movement table, forcing the Postgres to perform a full scann. After creating the index, the database query test performs faster than the MemCache. So, I will keep the results only to show that I know how to perform some stress tests.**

### Database Queries

~~Making requests that will respond using database queries takes arround 05 minutes and 12 seconds.~~

Making requests that will respond using database queries (with the appropriate index) only takes 10 seconds. It was faster than memcache probably because of my computer configuration and because every call to memcache needs two requests when the other only needs one request to the database.

![image2](https://github.com/edbort/code-challenge/blob/master/stress-test/db-test-01.png?raw=true)

![image2](https://github.com/edbort/code-challenge/blob/master/stress-test/db-test-02.png?raw=true)

### Memcache Queries

Making requests that will respond using memcache queries, it takes only 18 seconds. ~~It was more than 15 times faster.~~

![image2](https://github.com/edbort/code-challenge/blob/master/stress-test/db-test-03.png?raw=true)


