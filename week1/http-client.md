# HTTP Client

## contents

### TCP/IP 통신

#### 인터넷 프로토콜 스위트

[인터넷 프로토콜 스위트(Internet Protocol Suite)](https://ko.wikipedia.org/wiki/%EC%9D%B8%ED%84%B0%EB%84%B7_%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C_%EC%8A%A4%EC%9C%84%ED%8A%B8)는 인터넷에서 컴퓨터들이 서로 정보를 주고받는 데 쓰이는 통신규약(프로토콜)의 모음이다.

#### 전송계층

[전송 계층](https://ko.wikipedia.org/wiki/%EC%A0%84%EC%86%A1_%EA%B3%84%EC%B8%B5)의 프로토콜 중 가장 잘 알려진 것은 연결 지향 전송 방식을 사용하는 전송 제어 프로토콜 (TCP)이다. 보다 단순한 전송에 사용되는 사용자 데이터그램 프로토콜 (UDP)도 있다.

- TCP: 연결이 필요하다. 전달 및 순서를 보장한다. (전화)
- UDP: 연결하지 않고 데이터를 보낸다. 전달 및 순서를 보장하지 않는다. (편지)

### Socket

[Socket](https://ko.wikipedia.org/wiki/%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC_%EC%86%8C%EC%BC%93)과 [Socket API](https://en.wikipedia.org/wiki/Berkeley_sockets)를 구분해야한다.

Socket은 기본적으로 파일과 유사하게 다룰 수 있다. 즉 열어서 쓰고 닫고 등을 할 수 있다.(유닉스에서는 파일 디스크립터의 일종).

Java에서는 키보드 입력(System.in), 화면 출력(System.out), 파일 입출력 등과 마찬가지로 [Stream(Java 8에서 도입된 Stream API가 아니다.)](https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html)으로 다룰 수 있다.

#### Java Stream

입출력 스트림은 Java에서 제공하는 유연한 입출력 메커니즘으로, 다양한 소스와 대상에 대한 입출력을 쉽게 처리할 수 있게 해준다. 다양한 소스에는 아래와 같은 것들이 있다.  
파일, 네트워크 통신, 메모리의 바이트 배열 및 문자열, 키보드 및 마우스(표준 입출력), 프로세스 간 통신

### TCP 통신 순서

1. 서버는 접속 요청을 받기 위한 소켓을 연다. → Listen
2. 클라이언트는 소켓을 만들고, 서버에 접속을 요청한다. → Connect
3. 서버는 접속 요청을 받아서 클라이언트와 통신할 소켓을 따로 만든다. 요청을 받기 위한 소켓과 클라이언트와 connect하는 소켓이 다르다는 것이 중요하다. 서버는 여러 클라이언트의 요청을 처리해야하기 때문에 이런 구조로 소켓을 사용한다. → Accept
4. 소켓을 통해 서로 데이터를 주고 받는다. → Send & Receive 반복
5. 통신을 마치면 소켓을 닫는다. → Close ⇒ 누구든지 먼저 Close를 하면 상대방은 Receive로 인지할 수 있다.

### HTTP Client Example with Java

#### 1. 작업 디렉토리로 가서 gradle init

```<bash>
Starting a Gradle Daemon (subsequent builds will be faster)

Select type of project to generate:
  1: basic
  2: application
  3: library
  4: Gradle plugin
Enter selection (default: basic) [1..4] 2

Select implementation language:
  1: C++
  2: Groovy
  3: Java
  4: Kotlin
  5: Scala
  6: Swift
Enter selection (default: Java) [1..6] 3

Generate multiple subprojects for application? (default: no) [yes, no] no
Select build script DSL:
  1: Kotlin
  2: Groovy
Enter selection (default: Kotlin) [1..2] 1

Select test framework:
  1: JUnit 4
  2: TestNG
  3: Spock
  4: JUnit Jupiter
Enter selection (default: JUnit Jupiter) [1..4] 4

Project name (default: http-client): client
Source package (default: client): com.gyo.http.client
Enter target version of Java (min. 7) (default: 18): 18
Generate build using new APIs and behavior (some features may change in the next minor release)? (default: no) [yes, no


> Task :init
To learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.5/samples/sample_building_java_applications.html

BUILD SUCCESSFUL in 2m 4s
2 actionable tasks: 2 executed
```

#### 2. Connect

호스트는 IP 주소 또는 도메인 이름을 사용할 수 있다. 도메인의 경우 DNS를 활용하기 때문에 제대로 하려면 복잡해질 수 있지만, 일단은 "example.com"으로 한다.

```java
String host = "example.com";
```

HTTP의 기본 포트 번호는 80.

```java
int port = 80;
```

IP 주소와 포트 번호만 알면, 서버에 접속할 수 있다.

```java
Socket socket = new Socket(host, port);
```

객체 생성이지만 동시에 바로 서버에 접속 요청한다. 접속이 실패하면 ConnectException 예외가 발생한다.

#### 3. Request

요청 메시지를 만들고, TCP로 전송하면 된다.  
⚠️ 헤더 넣고 빈줄 넣는 것 빼먹지 않도록 주의하자.

```<java>
GET http://example.com/ HTTP/1.1
(빈 줄)
```

또는

```<java>
GET / HTTP/1.1
Host: example.com
(빈 줄)
```

후자의 형태를 더 많이 쓴다.

Java 코드

```java
String message = """
	GET / HTTP/1.1
	Host: example.com

	""";
```

또는

```java
String message = "" +
	"GET / HTTP/1.1\n" +
	"Host: example.com\n" +
	"\n";
```

소켓에서 Output Stream을 얻어서 write 할 수 있다. 이 때 byteStream으로 전송한다.(outputStream의 특징)

```java
OutputStream outputStream = socket.getOutputStream();
outputStream.write(message.getBytes());
```

문자열을 직접 전송하고 싶다면 Writer를 쓴다(추천).

```java
OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

writer.write(message);
writer.flush();
```

⚠️ writer는 내부적으로 버퍼가 있기 때문에 flush를 잊지 않아야 한다.

#### 4. Response

소켓에서 Input Stream을 얻어서 요청을 읽을 수 있다.

```java
InputStream inputStream = socket.getInputStream();
```

Byte 배열을 준비하고, 여기로 데이터를 읽어온다. 응답 데이터가 우리가 준비한 배열보다 클 수도 있는데, 이 경우엔 반복해서 여러 번 읽는 작업이 필요하다. 즉 우리가 준비한 배열이 Chunk(한번에 처리하는 단위)가 된다. 단순하게 하기 위해 예제는 Chunk를 1로 설정하고 읽는다. 따라서 바이트 배열을 넉넉하게 설정한다.

```java
byte[] bytes = new byte[1_000_000];
int size = inputStream.read(bytes);
```

read 메서드는 읽어온 바이트의 수를 리턴한다. 이를 활용해
실제 데이터 크기만큼 Byte 배열을 자르고, 문자열로 변환해 출력한다.

```java
byte[] data = Arrays.copyOf(bytes, size);
String text = new String(data);

System.out.println(text);
```

Reader를 쓰면 훨씬 편하다(추천). size를 구하고 chunk 작업을 Reader에서 대신 해주기 때문이다.

```java
InputStreamReader reader = new InputStreamReader(socket.getInputStream());

CharBuffer charBuffer = CharBuffer.allocate(1_000_000);  // response로 받아온 데이터를 담는 CharBuffer

reader.read(charBuffer);

charBuffer.flip();

System.out.println(charBuffer.toString());
```

⚠️ CharBuffer에서 읽기 전에 flip을 잊지 않아야 한다.

##### 참고) CharBuffer와 Flip

[CharBuffer & Flip](https://jamssoft.tistory.com/221/)

출력결과를 개발자 모드에서 확인해보자. 본문과 동일함을 확인할 수 있다.

```<jav>
> Task :app:compileJava UP-TO-DATE
> Task :app:processResources NO-SOURCE
> Task :app:classes UP-TO-DATE

> Task :app:run
Hello World!
Connect!
Reqeust!

// Response Data
HTTP/1.1 200 OK
Accept-Ranges: bytes
Age: 547965
Cache-Control: max-age=604800
Content-Type: text/html; charset=UTF-8
Date: Tue, 12 Dec 2023 09:44:09 GMT
Etag: "3147526947"
Expires: Tue, 19 Dec 2023 09:44:09 GMT
Last-Modified: Thu, 17 Oct 2019 07:18:26 GMT
Server: ECS (laa/7B8E)
Vary: Accept-Encoding
X-Cache: HIT
Content-Length: 1256

<!doctype html>
<html>
<head>
    <title>Example Domain</title>

    <meta charset="utf-8" />
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <style type="text/css">
    body {
        background-color: #f0f0f2;
        margin: 0;
        padding: 0;
        font-family: -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;

    }
    div {
        width: 600px;
        margin: 5em auto;
        padding: 2em;
        background-color: #fdfdff;
        border-radius: 0.5em;
        box-shadow: 2px 3px 7px 2px rgba(0,0,0,0.02);
    }
    a:link, a:visited {
        color: #38488f;
        text-decoration: none;
    }
    @media (max-width: 700px) {
        div {
            margin: 0 auto;
            width: auto;
        }
    }
    </style>
</head>

<body>
<div>
    <h1>Example Domain</h1>
    <p>This domain is for use in illustrative examples in documents. You may use this
    domain in literature without prior coordination or asking for permission.</p>
    <p><a href="https://www.iana.org/domains/example">More information...</a></p>
</div>
</body>
</html>


BUILD SUCCESSFUL in 1s
2 actionable tasks: 1 executed, 1 up-to-date

```

#### 5. Close

입출력 작업이 끝났으니 이제 socket을 close하면 된다.

```java
socket.close();
```

Socket은 Closeable이기 때문에 try-with-resources를 써도 된다.

```java
try (Socket socket = new Socket(host, port)) {
	// Request
	// Response
}
```
