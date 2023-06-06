# Jwt Authentication Service

This service sets up the necessary configurations for JWT authentication and authorization 
in a Spring Boot application, including custom filters, security rules, 
and authentication providers.

To run the project execute: 
`docker build -t jwt-auth .` # inside of jwtAuth folder  
`docker run -p 9999:8080 jwt-auth`

To test the full cycle /auth REST endpoint with curl, you can use the following command: 
```bash 
 curl -X POST \
  http://localhost:8080/api/v1/auth \
  -H 'Content-Type: multipart/form-data' \
  -F 'username=admin' \
  -F 'password=password'

```

This will send a POST request to the /auth endpoint with the specified username and password. 
If the username and password are valid, the server will return a 200 OK response with a JWT token in the Authentication heaser. 
You can then use this JWT token to access other protected resources on the server.

Here is an example of a successful response:

```
> POST /api/v1/auth HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.85.0
> Accept: */*
> Content-Length: 253
> Content-Type: multipart/form-data; boundary=------------------------e6dd036802b2d544
>
* We are completely uploaded and fine
* Mark bundle as not supporting multiuse
  < HTTP/1.1 200
  < Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4NjA0Mjk0MywiZXhwIjoxNjg2MDQyOTUzfQ.GnwuKumtm3qlkkZP0be240YnguyEId__9YTfZAt6XKzMQ5UHLrHNH8pFjf_gsqZJXYu1oUjHhxiKxFbPGXZp3g
  < X-Content-Type-Options: nosniff
  < X-XSS-Protection: 0
  < Cache-Control: no-cache, no-store, max-age=0, must-revalidate
  < Pragma: no-cache
  < Expires: 0
  < X-Frame-Options: DENY
  < Content-Length: 0
  < Date: Tue, 06 Jun 2023 09:15:43 GMT
  <
* Connection #0 to host localhost left intact
```


Project is build with the following libraries: 

com.example:jwtAuth:jar:0.0.1-SNAPSHOT
+- org.springframework.boot:spring-boot-starter-web:jar:3.1.0:compile
|  +- org.springframework.boot:spring-boot-starter:jar:3.1.0:compile
|  |  +- org.springframework.boot:spring-boot-starter-logging:jar:3.1.0:compile
|  |  |  +- ch.qos.logback:logback-classic:jar:1.4.7:compile
|  |  |  |  \- ch.qos.logback:logback-core:jar:1.4.7:compile
|  |  |  +- org.apache.logging.log4j:log4j-to-slf4j:jar:2.20.0:compile
|  |  |  |  \- org.apache.logging.log4j:log4j-api:jar:2.20.0:compile
|  |  |  \- org.slf4j:jul-to-slf4j:jar:2.0.7:compile
|  |  +- jakarta.annotation:jakarta.annotation-api:jar:2.1.1:compile
|  |  \- org.yaml:snakeyaml:jar:1.33:compile
|  +- org.springframework.boot:spring-boot-starter-json:jar:3.1.0:compile
|  |  +- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.15.0:compile
|  |  +- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.15.0:compile
|  |  \- com.fasterxml.jackson.module:jackson-module-parameter-names:jar:2.15.0:compile
|  +- org.springframework.boot:spring-boot-starter-tomcat:jar:3.1.0:compile
|  |  +- org.apache.tomcat.embed:tomcat-embed-el:jar:10.1.8:compile
|  |  \- org.apache.tomcat.embed:tomcat-embed-websocket:jar:10.1.8:compile
|  +- org.springframework:spring-web:jar:6.0.9:compile
|  |  \- org.springframework:spring-beans:jar:6.0.9:compile
|  \- org.springframework:spring-webmvc:jar:6.0.9:compile
|     +- org.springframework:spring-context:jar:6.0.9:compile
|     \- org.springframework:spring-expression:jar:6.0.9:compile
+- org.springframework.boot:spring-boot-starter-security:jar:3.1.0:compile
|  +- org.springframework:spring-aop:jar:6.0.9:compile
|  +- org.springframework.security:spring-security-config:jar:6.1.0:compile
|  \- org.springframework.security:spring-security-web:jar:6.1.0:compile
+- io.jsonwebtoken:jjwt-api:jar:0.11.2:compile
+- io.jsonwebtoken:jjwt-impl:jar:0.11.2:runtime
+- io.jsonwebtoken:jjwt-jackson:jar:0.11.2:runtime
|  \- com.fasterxml.jackson.core:jackson-databind:jar:2.15.0:compile
|     +- com.fasterxml.jackson.core:jackson-annotations:jar:2.15.0:compile
|     \- com.fasterxml.jackson.core:jackson-core:jar:2.15.0:compile
+- org.apache.tomcat.embed:tomcat-embed-core:jar:10.1.8:compile
|  \- org.apache.tomcat:tomcat-annotations-api:jar:10.1.8:compile
+- org.springframework.boot:spring-boot-starter-actuator:jar:3.1.0:compile
|  +- org.springframework.boot:spring-boot-actuator-autoconfigure:jar:3.1.0:compile
|  |  \- org.springframework.boot:spring-boot-actuator:jar:3.1.0:compile
|  +- io.micrometer:micrometer-observation:jar:1.11.0:compile
|  |  \- io.micrometer:micrometer-commons:jar:1.11.0:compile
|  \- io.micrometer:micrometer-core:jar:1.11.0:compile
|     +- org.hdrhistogram:HdrHistogram:jar:2.1.12:runtime
|     \- org.latencyutils:LatencyUtils:jar:2.0.3:runtime
+- org.springframework.boot:spring-boot-devtools:jar:3.1.0:runtime
+- org.springframework.boot:spring-boot-configuration-processor:jar:3.1.0:compile
