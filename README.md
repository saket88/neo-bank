# neo-bank
[![CircleCI](https://circleci.com/gh/saket88/neo-bank.svg?style=svg)](https://circleci.com/gh/saket88/neo-bank)

Repository for modern neo online bank


This is a simple neo bank which provides the facility to transfer money.(EUR for now)


1. We are using sparkjava as the micro framework as it is super fast.

2. Intitally Dagger2 was considered for DI but it has some issues post jdk 8. So Guice was fallback choice for this

3. Mockito and Junit are used as testing framework.

4. TDD is used as a development practise.

5. JPA is used as ORM.

6. This is a modular architecture having broad categories of application,infrastructure,entity and e2e test

7. There is no end to end testing framework used for the end to end testing. 
I found POSTMAN collections a good documentation for that. It has been enclosed in the module neo-e2e-test


Steps to run

1. mvn clean install
2. java -jar neo-application/target/neo-application-1.0-SNAPSHOT.jar 

Please use the POSTMAN collections in the neo-e2e-test for a basic transfer flow.
