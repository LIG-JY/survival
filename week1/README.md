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

## 참고하기

### 서적

- [HTTP 완벽 가이드](http://aladin.kr/p/jboK0)
- [러닝 HTTP/2](http://aladin.kr/p/OUtE5)
- [리얼월드 HTTP](http://aladin.kr/p/ReV1A)

### HTTP의 역사

- [웹의 아버지](https://ko.wikipedia.org/wiki/팀_버너스리)
- [HTTP의 진화](https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/Evolution_of_HTTP)

### HTTP/0.9

최초의 HTTP 스펙. 원래 버전이 있던 게 아니라, HTTP/1.0이 나오면서 최초의 스펙을 0.9 버전이라 부르게 됨.

요청은 “<method> <path>”, 응답은 “<body>”로 매우 단순함.

### HTTP/1.0

Start-line, Headers, Body로 구성된 HTTP 메시지 구조를 확립.

### HTTP/1.1

최초의 표준 프로토콜. 지속적으로 개선됨.

### HTTP/2

Google에서 제안한 SPDY 프로토콜을 기반으로 함. 성능을 위해 HTTP의 구현만 바꾸고 HTTP/1.1의 의미 체계를 그대로 사용하기 때문에, 기반 라이브러리 등만 교체하면 기존 프로그램을 그대로 사용할 수 있음. TLS를 강제하는 건 아니지만, 모든 웹 브라우저가 TLS을 사용하기 때문에 사실상 필수적이다.

여러 요청에 대해 단일 소켓을 사용한다. 사람이 읽기는 쉽지만 비효율적인 텍스트 기반에서 성능과 효율성에 무게를 둔 바이너리, 프레임, 스트림 기반으로 변경. 헤더 압축을 위해 HPACK 사용.

Start-line은 Headers 프레임으로 통합되는데, 예를 들어 “GET / HTTP/1.1”은 “:scheme: https”,“:method: GET”, “:path: /” 같은 식으로 헤더 프레임에 들어가게 됨. “HTTP/1.1 200 OK”는 사람이 읽는 용도로만 쓰이던 텍스트 메시지를 제거하고 “:status: 200” 하나로 헤더 프레임에 들어감.

HTTP/1.1에서 성능 최적화를 위해 도입했던 몇몇 기법이 HTTP/2에선 오히려 역효과를 내는데, Cookie-less Domain이나 CSS Image Sprite 같은 게 대표적.

https://d2.naver.com/helloworld/140351

https://blog.theodo.com/2019/09/cookieless-domain-http2-world/

https://developer.mozilla.org/ko/docs/Web/CSS/CSS_Images/Implementing_image_sprites_in_CSS

### HTTP/3

UDP 기반의 QUIC 프로토콜 사용. HTTP/2가 응용 계층만 바꾼 거라면, HTTP/3는 전송 계층까지 바꿈.

https://docs.google.com/document/d/1RNHkx_VvKWyWg6Lr8SZ-saqsQx7rFV-ev2jRFUoVD34/view

https://blog.cloudflare.com/ko-kr/http3-the-past-present-and-future-ko-kr/

https://www.slideshare.net/secret/18Rh7yI0cXocCE

### HTTP 클라이언트 도구

- [curl](https://curl.se/)
- [HTTPie](https://httpie.io/)
