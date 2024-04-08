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

#### @Value
It can be used to inject values from properties file

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
