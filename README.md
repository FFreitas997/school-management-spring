# School-Management-System-Spring

This school management system offers a robust platform for managing various aspects of a school's operations. It
uses a relational database structure with interconnected entities representing key parts of the school
environment.

____________________________________________________________________________________________________________________

# Spring Framework Documentation

## Spring Framework

It's an open source framework for building enterprise Java applications.

#### Spring core features

1. IOC: Inversion of Control Container 
2. AOPAspect Oriented Programming
3. Data Access Framework
4. Spring MVC Framework

#### Spring Bean

An object that is managed by Spring Framework in Java application, and it's configured by XML or Java code.

#### Spring Component

It indicates that the class should be initialized, configured and managed (dependency injection) by Spring Container. 
Exists these annotations as meta-annotation: @Repository, @Service and @Controller. 
This should be defined to support general architecture principles.

#### Dependency Injection
1. Constructor Injection
2. Setter Method Injection
3. Field Injection
4. Configuration Methods

#### Bean Scoping
1. Singleton
2. Prototype
3. Request
4. Session
5. Application
6. WebSocket

#### @Environment
It can be used to access Environment config fields

#### @Bean
You can give different names to the beans (default is method name) and you can also give profiles

#### ApplicationContext
It's an interface for providing configuration information to an application.
It reads the configuration metadata and uses it to create a fully configured application.

`````java

public class SpringBootApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootApplication.class, args);
        ctx.getBean("javaVersion", String.class);
        ctx.getBean("osName", String.class);
    }

}

@Configuration
public class ApplicationConfig {

    private final Environment environment;

    public ApplicationConfig(Environment environment) {
        this.environment = environment;
    }
    
    @Bean(name = "javaVersion")
    @Profile("dev")
    public String getJavaVersion() {
        return environment.getProperty("java.version");
    }

    @Bean(name = "osName")
    @Profile("dev")
    public String getOsName() {
        return environment.getProperty("os.name");
    }

}

``````

#### @Value
It can be used to inject values from properties file

#### @PostConstruct
It can be used to execute a method after bean initialization

#### @ResponseStatus
Mark a method or exception class with the status code and the reason that should be returned

#### Best Practice In Spring
1. Use Constructor Injection
2. Use @Value for injecting values
3. Split Configuration
4. Spring Initializer

#### Spring Boot
It's an approach to building Spring-based applications with less config.
It means that it has a set of starters to start working with it and has autoconfiguration available.

#### Why Spring Boot?
1. Standalone apps
2. Embed Server: Tomcat or jetty
3. Starters
4. Auto Configuration
5. Production Ready Features
6. No XML configuration

#### @PropertySource
It can be used to load properties file
````` @PropertySource("classpath:custom.properties") `````

#### Spring Profile

Allowing us to map our beans to different profiles, for example, dev, test, and prod.
The property needed to activate the environment is: spring.profiles.active
To mark the beans with the profile related to: @Profile("test") this can be set in class level or even method level

#### Testing Repository without creating controllers and services using Java Faker

````java
@Bean
public CommandLineRunner commandLineRunner(AuthorRepository repo){
    return args -> {
        log.info("Executing command line runner");
        for (int i = 0; i < 50; i++) {
            Faker faker = new Faker();
            Author author = Author.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .age(faker.number().numberBetween(19, 65))
                    .createdAt(LocalDateTime.now())
                    .createdBy("John Doe")
                    .build();
            repo.save(author);
        }
        List<Author> authors = repo.findAllByFirstNameStartsWithIgnoreCase("f");
        authors.forEach(System.out::println);
    };
}
````

#### Spring Rest
Stands for Representational State Transfer.
It's an architectural style for designing networked applications.
The REST is about treating network resources as an object that can be accessed using standard HTTP

1. Client-Server Architecture -> should both act independently
2. Stateless -> each request from client to server must contain all the information needed to understand the request
3. Cacheable -> server responses must define themselves as cacheable or not
4. Uniform Interface -> Identification of resources, manipulation of resources through representations, Self-descriptive messages, Hypermedia As The Engine Of Application State (HATEOS)

#### Handling Exceptions
1. @ExceptionHandler: Controller Exception Handler
2. @ExceptionHandler with @ControllerAdvice: Global Exception Handler
3. ResponseStatusException: We can create an instance of it providing an HttpStatus and optionally a reason and a cause
4. HandlerExceptionResolver: define HandlerExceptionResolver. This will resolve any exception thrown by the application.

## Spring Security

Spring Security is a powerful and highly customizable authentication and access-control framework. 
It is the de-facto standard for securing Spring-based applications.

#### There are many ways to achieve authentication in Spring, such as:

- Basic Authentication: Basic authentication is a simple authentication method that involves sending a user’s credentials (username and password) in plain text with each request
- Form-based Authentication: Form-based authentication is a more secure authentication method that uses a login form to collect user credentials. The user enters their username and password into the form, which is then sent to the server for verification
- OAuth 2.0: is an open standard for authentication and authorization that allows users to grant third-party applications access to their resources without giving away their credentials
- JWT: Token-based authentication is a popular authentication method that involves generating a token (usually a JSON web token or JWT) that is sent to the client after successful authentication. The client includes the token with each later request to access protected resources. This method is stateless, scalable, and secure
- LDAP


## Spring Data

Spring Data is a high-level SpringSource project whose purpose is to unify and ease the access to different kinds of persistence stores, both relational database systems and NoSQL data stores.

#### Jpa Specification
It's a powerful API for building queries with JPA. It allows you to build up a query using a fluent API and then use it to query your database.

````java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {}

public class AuthorSpecification {
    public static Specification<Author> hasAge(int age) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (age < 0) return null;
            return builder.equal(root.get("age"), age);
        };
    }

    public static Specification<Author> firstnameLike(String firstname) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (firstname == null) return null;
            return builder.like(root.get("firstName"), "%" + firstname + "%");
        };
    }
}

// Usage
public CommandLineRunner commandLineRunner(AuthorRepository repo) {
    return args -> {
        // Execute Specification Author
        Specification<Author> spec = Specification
                .where(AuthorSpecification.hasAge(34))
                .and(AuthorSpecification.firstnameLike("John"));
        repo.findAll(spec).forEach(System.out::println);
    };
}
````

#### Json Management
- @JsonManagedReference: Manages the forward part of the reference and
the fields marked by this annotation are the ones that get Serialised.

- @JsonBackReference Manages the reverse part of the reference, and
the fields/collections marked with this annotation are not serialized.

#### @NamedQuery
It can be used to define a query in the entity class

````java
@Entity
@NamedQuery(
        name = "Author.findByNameQuery",
        query = "select a from Author a where a.age >= :age"
)
@NamedQuery(
        name = "Author.updateByNameQuery",
        query = "update Author a set a.age = :age"
)
public class Author {}

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Transactional
    List<Author> findByNameQuery(@Param("age") int age);

    @Modifying
    @Transactional
    void updateByNameQuery(@Param("age") int age);
}
````

#### Embedded Key
It can be used to define a composite key

````java
// Composite Key
@Embeddable
public class OrderId implements Serializable {

    private String username;
    private LocalDateTime orderDate;
}

@Entity
public class Order {

    @EmbeddedId
    private OrderId id;
    @Embedded
    private Address address;
    private String orderName;
    private String orderDescription;
}

// Order Additional fields
@Embeddable
public class Address {

    private String street;
    private String houseNumber;
    private String zipCode;
}

````

#### Spring data JPA Inheritance
- Single Table Strategy: All the classes in the hierarchy are mapped to a single table in the database
````java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double amount;
    private LocalDateTime paymentDate;
}   

@DiscriminatorValue("FILE")
@Entity
public class File extends Payment{

    private String type;
}
````
- Table Per Class Strategy: Each class in the hierarchy is mapped to a separate table in the database
````java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double amount;
    private LocalDateTime paymentDate;
}   

@Entity
public class File extends Payment{

    private String type;
}
````
- Joined Strategy: Each class in the hierarchy is mapped to a separate table in the database, and the tables are linked using foreign key relationships
````java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double amount;
    private LocalDateTime paymentDate;
}   

@Entity
@PrimaryKeyJoinColumn(name = "file_id")
public class File extends Payment{

    private String type;
}
````
- Mapped Superclass Strategy: The superclass fields are mapped to the subclass tables, but the superclass itself is not mapped to a table

````java
@MappedSuperclass
public class UserBaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
}

@Entity
public class User extends UserBaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;
}
````
#### Table Generator Strategy
It can be used to generate the primary key

````java
@GeneratedValue(strategy = GenerationType.TABLE, generator = "author_id_gen")
@TableGenerator(name = "author_id_gen", table = "id_generator", pkColumnName = "id_name", valueColumnName = "id_value", allocationSize = 1)
````

#### Sequence Generator Strategy
It can be used to generate the primary key

````java
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq")
     @SequenceGenerator(name = "author_seq", sequenceName = "author_seq", allocationSize = 1)
````

____________________________________________________________________________________________________________________
# Java

## Java Functional Programming

Functional programming is a programming paradigm
that treats computation as the evaluation of mathematical functions and avoids changing-state and mutable data.

#### Functional Interface
A functional interface is an interface that contains only one abstract method.
They can have only one functionality to exhibit.
A functional interface can have any number of default, static methods but can contain only one abstract method.

1. Consumer: Represents an operation that accepts a single input argument and returns no result.
2. Supplier: Represents a supplier of results.
3. Predicate: Represents a predicate (boolean-valued function) of one argument.
4. Function: Represents a function that accepts one argument and produces a result.
5. BiFunction: Represents a function that accepts two arguments and produces a result.
6. UnaryOperator: Represents an operation on a single operand that produces a result of the same type as its operand.
7. BinaryOperator: Represents an operation upon two operands of the same type, producing a result of the same type as the operands.
8. BiConsumer: Represents an operation that accepts two input arguments and returns no result.
9. Comparator: Represents an operation that accepts two input arguments and returns no result.
10. Runnable: Represents a task that can be executed.
11. Callable: Represents a task that returns a result.
12. Stream: Represents a sequence of elements supporting sequential and parallel aggregate operations.
13. Optional: A container object which may or may not contain a non-null value.
14. CompletableFuture: A Future that may be explicitly completed (setting its value and status), and may be used as a CompletionStage, supporting dependent functions and actions that trigger upon its completion.


____________________________________________________________________________________________________________________

# Docker

Docker is a platform for developing, shipping, and running applications in containers.
It enables you to separate your applications from your infrastructure, so you can deliver software quickly.

#### Docker Container
A container is a standard unit of software that packages up code and all its dependencies,
so the application runs quickly and reliably from one computing environment to another.

#### Docker Image
An image is a lightweight, stand-alone, executable package that includes everything needed to run a piece of software,
including the code, a runtime, libraries, environment variables, and config files.

#### Docker Container vs Virtual Machine
- Containers are lightweight because they don't need the extra load of a hypervisor, but run directly within the host machine's kernel.
- Containers can share a single kernel, and the only information that needs to be in a container image is the executable and its package dependencies, which reduces the image size.
- Containers start up very quickly and use fewer resources than virtual machines.

#### Docker Commands

1. docker -v
2. docker images
3. docker pull {image-name}:{image-version}
4. docker run --name {set-container-name} {image-name} | example: docker run --name francisco-postgres -e POSTGRES_PASSWORD=password postgres
5. docker run -v [host-path]:[container-path] | example: docker run -v /user/some/path/data:/var/lib/postgres/data
6. docker run -v [container-path] | example: docker run -v /var/lib/postgres/data
7. docker run -v name:[container-path] | example: docker run -v name:/var/lib/postgres/data
8. docker ps | list running containers
9. docker ps -a
10. docker start {container-name}
11. docker stop {container-name}
12. docker logs {container-name}
13. docker logs -f {container-name} | blocking logs
14. docker rm {container-name} | delete container
15. docker rmi {image-name}
16. docker exec -it {container-name} {specific-command} | example: docker exec -it practical_cartwright psql -U postgres
17. docker exit

After restart or stop the container, the data will be lost to persist the data; we can use volumes

#### Docker Volumes
A Docker volume is a directory stored outside the container's filesystem.
It is part of the host filesystem and can be shared among containers.

example: docker run --name postgres-db -e POSTGRES_PASSWORD=password -v C:\Users\franc\docker\volumes\postgres:/var/lib/postgres/data postgres

#### Working Before Containers
1. Different installation process for each OS
2. Hard to reproduce the same issues in different environments
3. Many steps if something goes wrong

#### Expose Docker Container to the Host
1. docker run -p 8080:8080 {image-name}
   
Full example:
 ````
 docker run --name postgres-xico -p 5430:5432 -e POSTGRES_USER=ffreitas -e POSTGRES_PASSWORD=29101997 -e POSTGRES_DB=demo_db -v C:\Users\franc\docker\volumes\postgres\data:/var/lib/postgres/data postgres
 ````


#### Docker file
A Dockerfile is a text document
that contains all the commands a user could call on the command line to assemble an image.

Structure Example:
````dockerfile
FROM java:21 {base_image_name}
RUN {command_1} && {command_2}
COPY {source} {destination}
WORKDIR {path}
ENV {key}={value}
EXPOSE {port}
CMD {command}
````

Dockerfile for the application:
````dockerfile
FROM openjdk:21-jdk

LABEL authors="Francisco"
LABEL description="This is a Dockerfile for a Spring Boot application"
LABEL version="1.0"

WORKDIR /app

COPY target/School-Management-System-Spring-1.0.0.jar /app

EXPOSE 8080

CMD ["java", "-jar", "School-Management-System-Spring-1.0.0.jar"]
````
then we can build the image using the command:
````docker
docker build -t school-management-system-spring .
````

#### Docker Network
Docker network is a type of network that allows communication between different containers.

Create Network:
````docker
docker network create --driver bridge {network-name}
````
List Networks:
````docker
docker network ls
````

Connect Container to Network:
````docker
docker network connect {network-name} {container-name}
````
````docker
docker run --name {container-name} --network {network-name} {image-name}
````

#### Docker Compose
Docker Compose is a tool for defining and running multi-container Docker applications.
With Compose, you use a YAML file to configure your application's services.
Then, with a single command, you create and start all the services from your configuration.

Example of a docker-compose file:
````yaml
# It's required that the image must be created before running the docker-compose
# spring-school-management:1.0.0 is app image name
# openjdk:21-jdk is the base image name Java 21

services:
  database:
    image: postgres:latest
    container_name: postgres-school-management
    environment:
      POSTGRES_USER: ffreitas
      POSTGRES_PASSWORD: 29101997
      POSTGRES_DB: school_management_database
    ports:
      - "5430:5432"
    volumes:
      - postgres:/var/lib/postgres/data
    networks:
      - school-management-network

  spring-application:
    image: spring-school-management:latest
    container_name: spring-school-management
    ports:
      - "8080:8080"
    networks:
      - school-management-network
    volumes:
      - spring-application:/var/lib/spring-application/data

volumes:
    postgres:
    spring-application:

networks:
    school-management-network:
        driver: bridge
````

#### Docker compose commands
1. docker-compose up
2. docker-compose up -d
3. docker-compose down

Overview Steps:
1. Create a Dockerfile for the application
2. Docker Build
3. Docker Compose run
````docker
docker build -t {image_name} .
````
````dockerfile
FROM openjdk:21-jdk

LABEL authors="FFreitas"
LABEL description="This is a Dockerfile for a Spring Boot application"
LABEL version="1.0.0"

WORKDIR /app

COPY target/School-Management-System-Spring-1.0.0.jar /app

EXPOSE 8080

CMD ["java", "-jar", "School-Management-System-Spring-1.0.0.jar"]
````

# Spring AOP

Aspect-Oriented Programming (AOP) complements Object-Oriented Programming (OOP) by providing another way of thinking about program structure.

#### AOP Concepts
1. Aspect: A module that encapsulates behaviors affecting multiple classes into reusable modules.
2. Join Point: A point during the execution of a program, such as the execution of a method or the handling of an exception.
3. Advice: Action taken by an aspect at a particular join point.
4. Pointcut: A predicate that matches join points.

#### Most Common Use Case of Advice
1. Logging
2. Security
3. Transaction Management
4. Exception Handling
5. Performance Monitoring
6. Caching

#### Types of Advice
1. Before Advice: Run before the method execution
2. After Returning Advice: Run after the method returns a result
3. After Throwing Advice: Run after the method throws an exception
4. After Advice: Run regardless of the method outcome
5. Around Advice: Run around the method execution 
