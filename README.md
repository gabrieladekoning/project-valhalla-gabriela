<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Issues][issues-shield]][issues-url]
![Build][build]
[![LinkedIn][linkedin-shield]][linkedin-url]

>>Note:
>>badges above won't work due to repo being private, badge below works
>>
![build](https://github.com/williamsuane/project-valhalla-gabriela/actions/workflows/maven.yml/badge.svg?style=for-the-badge)


<br />

<h3 align="center">Project Valhalla</h3>

  <p align="center">
Your One-Stop Microservices Authentication and Authorization Manager using Spring and Keycloak for Securely Managing Roles and Permissions.
    <br />
    <a href="https://github.com/williamsuane/project-valhalla-gabriela/issues">Report Bug</a>
    ·
    <a href="https://github.com/williamsuane/project-valhalla-gabriela/issues">Request Feature</a>
  </p>



<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#features">Features</a></li>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#tech-stack">Tech Stack</a></li>
        <li><a href="#authentication-and-token-management-keycloak">Authentication and Token Management: Keycloak</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#setting-up-environment-variables">Setting Up Environment Variables</a></li>
        <li><a href="#keycloak-setup">Keycloak Setup</a></li>
        <li><a href="#swagger">Swagger</a></li>
      </ul>
    </li>
    <li>
      <a href="#usage">Usage</a>
      <ul>
        <li><a href="#initial-user-creation">Initial User Creation</a></li>
        <li><a href="#adding-security-to-a-new-microservice">Adding Security to a New Microservice</a></li>
        <li><a href="#service-ports">Service Ports</a></li>
      </ul>
    </li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>




## About The Project

The purpose of this project is to simplify the management of roles, permissions, authentication, and authorization across microservices. It is designed to be a centralized access control module that can be easily integrated with any service built using Spring technologies.

### Features
- **Role Management**: Allows for the creation and management of different roles that can be assigned to users or a group of users.

- **Permission Management**: Enables defining granular permissions like ‘create’, 'read', ‘update’ and 'delete'.

- **Role-Permission Association**: Offers the ability to associate permissions with roles to create functional profiles like ‘Employee Service Administrator'.

- **User-Role Association**: Allows associating roles with individual users, thereby defining what each user is authorized to do.

- **Business Function Management**: Allows the creation of business functions that can be used for more granular access control.

- **Microservices Architecture**: The project is built with microservices architecture in mind and includes considerations for service discovery, gateways, and stateless authentication.

- **Additional Components**: Incorporates KeyCloak for authentication and token management, Spring Cloud for load balancing and service registry, and Spring Cloud Gateway as the entry point for the services.

- **Documented APIs**: Access the interactive API documentation via Swagger/OpenAPI.

- **Tech Stack**: Utilizes Spring Core modules and Spring Cloud modules for a robust backend.

By utilizing this service, other services can offload the responsibility of managing security and focus on their primary functions, leading to more maintainable and secure microservices architectures.

### Built With

[![Java][Java]][Java-url]
[![Spring Framework][Spring]][Spring-url]

#### Tech Stack
This project leverages various technologies for a scalable, maintainable, and secure architecture:

| Category        | Technologies                         |
|-----------------|--------------------------------------|
| Backend         | Spring Boot, Spring Security, Spring Data JPA |
| Microservices   | Eureka, Spring Cloud Gateway         |
| HTTP Client     | WebClient                            |
| Libraries       | MapStruct, Lombok                    |
| Authentication  | Keycloak                             |

### Authentication and Token Management: Keycloak
This project incorporates Keycloak, an open-source Identity and Access Management solution aimed at modern applications and services. Keycloak plays a critical role in the security architecture, offering a wide range of features:

- Single Sign-On (SSO): Provides a single point of authentication through various social, OpenID Connect, and SAML-based providers.

- Token-based Authentication: Manages tokens and token-based authentication, simplifying secure credential management.

- User Federation: Connects to external user databases or authentication systems to provide a seamless security mechanism.

- Fine-Grained Authorization: Allows for detailed, attribute-based access control policies, which can be managed centrally.

Keycloak integrates seamlessly with the Spring Security framework, aligning closely with the project's goal of providing a comprehensive and centralized security solution for microservices.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

This section provides a quick start guide to get the Access Control Service running on your local machine for development and testing purposes. Follow the steps below to set up your environment and run the project.

### Prerequisites
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)

### Installation
To start, clone the repository:
```bash
git clone https://github.com/williamsuane/project-valhalla-gabriela.git
```

After cloning the repository certain variables will have to be adjusted in order to start the services.

#### Setting Up Environment Variables
In this project, Docker environment variables are being used to securely store sensitive data.

The environment variables that you'll need to configure are contained in an env.template file in the root of the project directory. To use this template file, you should rename it to `.env`. This .env file will be read by Docker Compose.

These are the environment variables which need to be set with your own values:
```
MYSQL_ROOT_PASSWORD= # MySQL root password
MYSQL_USER= # MySQL username
MYSQL_PASSWORD= # MySQL password

KC_DB_USERNAME= # Keycloak DB username
KC_DB_PASSWORD= # Keycloak DB password

KEYCLOAK_ADMIN= # Keycloak admin username
KEYCLOAK_ADMIN_PASSWORD= # Keycloak admin password
```
After setting these variables, you can proceed to start the services using Docker Compose.
```bash
docker-compose up
```

> **Note** 
> When running for the first time, make sure to re-run keycloak container again after which it will properly work

#### Keycloak Setup
Keycloak is configured and run via Docker. After you start the Docker containers, you can access the Keycloak Admin Console at http://localhost:8180/auth.

1. Open your browser and navigate to http://localhost:8180/auth
2. Click on the Administration Console link
3. Log in with the credentials you set in the .env file earlier
4. Once logged in, create a new realm named `application-users`
5. Next, create a client with the following details:
    * Client ID: `access-control-client`
    * Toggle 'Client authentication' and 'Authorization'
    * Set Valid redirect URI to http://localhost:8180/*
    
   Make sure to save the client's secret key generated by Keycloak. This key should be used in the Access Control Service's application.yml file under `client-secret`
6. In addition, another client will be created to check if a user has permissions:
   * Client ID: `user-authentication-client`
   * Do **not** toggle 'Client authentication' and 'Authorization' for this client
   * Set Valid redirect URI to http://localhost:8180/*

Keep in mind that all these settings (such as realm name, client ID, and client secret) should match the configurations set in your application.yml file.

#### Swagger
Once you've got the application running, explore the API documentation through the Swagger UI available at http://localhost:8090/swagger-ui/index.html.

To test endpoints that require authentication, you'll need a bearer token from Keycloak. This token can be retrieved through a POST request to [http://localhost:8180/auth/realms/application-users/protocol/openid-connect/token](http://localhost:8180/auth/realms/application-users/protocol/openid-connect/token).

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Usage
### Initial User Creation
1. **Create User**: make a POST request to the `/v1/users` endpoint. This endpoint is public and doesn't require any permissions. A UUID will be returned, copy the UUID as it will be required for the next step. Here's an example request body:

```
{
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"password": "verySecurePassword"
}
```

2. **First User Script**: Navigate to the first-user-script folder and find the create_first_user.sql file. Scroll to the end of the file to insert the UUID generated in the previous step. Replace the placeholder `'replace with user UUID'` with the UUID generated in the previous step.

```
-- insert UUID generated by Keycloak below
INSERT INTO user_profiles (id, application_user_id, profile_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN('replace with user UUID'), UUID_TO_BIN(@profile_id));
```
> Note: This user will have permissions for all endpoints within the access-control-service.

### Adding Security to a New Microservice
1. **Dependency**: Add the security-core dependency to your pom.xml to integrate security features into your new microservice. This dependency includes security configuration, OpenAPI configuration, and the `CheckPermissionService`.
By adding this dependency, you can leverage pre-configured OpenAPI settings and security configurations in your new microservice.
```
<dependency>
<groupId>com.devoteam</groupId>
<artifactId>security-core</artifactId>
<version>${project.version}</version>
</dependency>
```
2. **Method-Level Security**: Use the @PreAuthorize annotation to secure methods within your microservice. For example:
```
@PreAuthorize("@checkPermissionService.isAccessAllowed('application-name', 'some-business-function', 'permission')")
public void anyGetMethod(){
}
```
3. **Gateway Configuration**: Update the application.yml in the Gateway service to include the routing information for your new microservice. The routes section will need to be extended to include the newly created microservice.

4. **Microservice Configuration**: Ensure your new microservice has its own application.yml file properly configured. This includes port settings, Eureka settings, and security settings. For an example, feel free to refer to the `demo` microservice in this repository.

### Service Ports
The services are exposed on the following ports:

* **Eureka Service** - 8761
* **Gateway Service** - 8080
* **Access Control Service** - 8090


<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Contact

Gabriela de Koning - gabriela.de.koning@devoteam.com

Project Link: [https://github.com/williamsuane/project-valhalla-gabriela](https://github.com/williamsuane/project-valhalla-gabriela)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Acknowledgments

* [William Suane, DevDojo Academy](https://github.com/williamsuane)
* [Devoteam](https://www.devoteam.com/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/williamsuane/project-valhalla-gabriela.svg?style=for-the-badge
[contributors-url]: https://github.com/williamsuane/project-valhalla-gabriela/graphs/contributors
[issues-shield]: https://img.shields.io/github/issues/williamsuane/project-valhalla-gabriela.svg?style=for-the-badge
[issues-url]: https://github.com/williamsuane/project-valhalla-gabriela/issues
[build]: https://img.shields.io/github/actions/workflow/status/williamsuane/project-valhalla-gabriela/maven.yml?style=for-the-badge
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/gabrielaldk
[Java]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/en/
[Spring]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/projects/spring-framework