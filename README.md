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


