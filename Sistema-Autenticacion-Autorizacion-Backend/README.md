# 🛡️ Sistema de Autenticación y Autorización - Backend

Este es el **backend** de un sistema desarrollado como proyecto personal para fortalecer mis bases en desarrollo **Full Stack**, enfocado en la **seguridad de aplicaciones web**.  
El objetivo principal es gestionar la **autenticación y autorización de usuarios**, controlando el acceso a las distintas interfaces según los **roles y permisos** definidos.

---

## 🚀 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **BCryptPasswordEncoder**
- **Spring Data JPA**
- **MySQL**
- **Lombok**

---

## 🔐 Funcionalidades principales

✅ **Autenticación de usuarios** mediante JWT.  
✅ **Autorización basada en roles y opciones.**  
✅ **Encriptación segura de contraseñas con BCrypt.**  
✅ **Control de acceso** según permisos asignados.  
✅ **Tokens con expiración configurable.**  
✅ **Gestión de usuarios y roles** desde el panel administrador (conectado al frontend Angular).  
✅ **Comunicación segura con DTOs** para proteger la información sensible.

---

## 🧩 Estructura del proyecto
```
CiberPass-Backend/
│
├── src/
│ ├── main/
│ │ ├── java/pe/edu/cibertec/ciberpass/
│ │ │ ├── controller/ → Controladores REST
│ │ │ ├── entity/ → Entidades JPA (Usuario, Rol, Opcion, etc.)
│ │ │ ├── repository/ → Interfaces de persistencia
│ │ │ ├── security/ → Configuración de seguridad, JWT, UserDetailsService
│ │ │ ├── service/ → Lógica de negocio
│ │ │ └── dto/ → Objetos de transferencia de datos (DTOs)
│ │ └── resources/
│ │ ├── application.properties → Configuración del proyecto
│ │ └── data.sql / schema.sql (si aplica)
│ └── test/ → Tests unitarios
│
└── pom.xml → Dependencias y configuración Maven
```


## ⚙️ Configuración y ejecución

### 1️⃣ Clonar el repositorio
```

```

### 2️⃣ Configurar la base de datos

En el archivo application.properties:
```
spring.application.name=CiberPass-Backend

spring.datasource.url=jdbc:mysql://localhost:3306/authsystem
spring.datasource.username=root
spring.datasource.password=root123

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
logging.level.org.hibernate.orm.jdbc.bind=trace
logging.level.org.hibernate.type=trace
logging.level.org.hibernate.stat=debug

server.port=8090


```
## 🧠 Conceptos aplicados

✅ **Seguridad y cifrado: implementación de BCryptPasswordEncoder para proteger las contraseñas.**  
✅ **JWT: autenticación basada en tokens con expiración configurable.**  
✅ **Control de acceso granular: los roles determinan qué opciones o interfaces puede usar cada usuario.**  
✅ **Buenas prácticas: separación por capas, uso de DTOs, logs, y manejo de excepciones.**  


## 👨‍💻 Autor

Yezid Perez    
📧 Contacto: LinkedIn  
https://www.linkedin.com/in/yezid-p%C3%A9rez-99aba7234/
