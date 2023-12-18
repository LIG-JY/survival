# 목표

1. HTTP 가 어떻게 구성되어있는지 이해하기
2. 직접 웹 서버 구현하기

## 목차

1. [HTTP의 이해](./understanding-of-http.md)
2. [HTTP Client](./http-client.md)
3. [HTTP Server](./http-server.md)
4. [Java HTTP Server](./java-httpserver.md)
5. [Spring Web MVC](./spring-web-mvc.md)

## keyword

### HTTP의 이해

- HTTP(Hypertext Transfer Protocol)
- HTTP와 HTTPS의 차이(TLS)
- 클라이언트-서버 모델
- stateless와 stateful
- HTTP Cookie와 HTTP Session
- HTTP 메시지 구조
  - HTTP 요청(Request)와 응답(Response)
    - multipart/form-data
  - HTTP 요청 메서드(HTTP request methods)
    - 멱등성
  - HTTP 응답 상태 코드(HTTP response status code)
    - 리다이렉션

### HTTP Client

- TCP/IP 통신
- TCP와 UDP
- Socket과 Socket API 구분
- URI와 URL
- 호스트(host)
  - IP 주소
  - Domain name
  - DNS
- 포트(port)
- path(경로)
- Java text blocks
- Java InputStream과 OutputStream
- Java try-with-resources

### HTTP Server

- Java ServerSocket
- Blocking vs Non-Blocking

### Java HTTP Server

- Java HTTP Server
- Java NIO
- Java Lambda expression(람다식)
  - Java Functional interface(함수형 인터페이스)

### Spring Web MVC

- [Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/overview.html#overview)
- Spring Boot
- Spring initializer
- Web Server와 Web Application Server(WAS)
  - Tomcat
- Model-View-Controller(MVC) 아키텍처 패턴
- 관심사의 분리(Seperation of Concern)
- Spring MVC
- Java Annotation
- Spring Annotation
  - @RestController
    - @Controller
    - @ResponseBody
  - @GetMapping
    - @RequestMapping

## TODO

각 키워드가 페이지에 녹았는지 점검하기
