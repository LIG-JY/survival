# DTO

## content

### DTO의 이해

[DTO의 정의](https://martinfowler.com/eaaCatalog/dataTransferObject.html)에 대해서 알아보자. Martin Fowler의 Pattern of Enterprise Aplication Architecture에 정의가 언급된다. 여기서 주의할 것은 DTO는 아키텍처 패턴이라는 것이다. 즉 아키텍처에서 반복적으로 사용되는 솔루션을 말한다.

#### 참고: 아키텍처 패턴

아키텍처 패턴은 일반적인 아키텍처 디자인 문제에 대한 재사용 가능한 솔루션이다. 특정한 맥락에서 자주 발생하는 문제에 대한 일반적인 해결책을 제공하며, 이를 사용함으로써 시스템의 설계 및 개발을 더 쉽게 만들 수 있다. 대표적으로 MVC Pattern이 있다.

DTO의 구체적인 정의는 아래와 같다.

> An object that carries data between processes in order to reduce the number of method calls.

여기서 가장 핵심이 되는 것은 between processes이다. 그리고 본문에도 아래와 같은 내용이 있다.

> When you're working with a remote interface, such as Remote Facade (388), each call to it is expensive

remote interface에서 통신하는 것은 비싸다..? 이렇게 2개의 내용을 종합해서 정리하면 [Remote Facade](https://martinfowler.com/eaaCatalog/remoteFacade.html)와 같은 원격 통신 인터페이스에는 제약 조건이 존재한다는 것이다. 이렇게 원격 통신 인터페이스의 제약 조건을 왜 언급하나면 웹 어플레케이션의 아키텍처가 이를 기반으로 하기 때문이다. 결국 DTO는 이런 제약 조건을 고려해서 만들어졌다.

#### 제약조건

다른 프로세스와 통신, 그것도 원격(네트워크를 통해)으로 통신할 때 제약조건이 발생한다. 우선 프로세스간 통신과 제약 조건에 대해서 알아보자

#### 프로세스 간 통신

##### IPC란 무엇이며 왜 필요할까?

[프로세스 간 통신](https://ko.wikipedia.org/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4_%EA%B0%84_%ED%86%B5%EC%8B%A0)의 정의는 위키를 참고하자.

서로 다른 프로세스의 개념을 왜 언급할까? 이는 웹 어플리케이션의 아키텍처 때문이다. 요즘 웹 어플리케이션은 B/E와 F/E로 Tier을 나누며 이를 위해서는 IPC(프로세스 간 통신)가 필수적이다.

##### IPC 방식

- File : 가장 기본적인 접근. 한 파일이 있으면 두 프로세스에서 읽을 수 있다. 이를 읽으면서 소통할 수 있겠죠? 물론 파일 시스템에서는 두 프로세스에서 동시에 쓰려고 하면 문제가 되긴한다. 그래서 한 프로세스에서 쓰고 다른 프로세스에서는 읽기만하는 식으로 소통할 수 있다. 이 방법은 원격 환경(인터넷)에서 활용하기 어렵다.

- Socket : 파일과 유사하게 읽고 쓸 수 있지만, 원격 환경에서도 활용할 수 있다. HTTP 같은 고수준 프로토콜을 활용하면 어느 정도 정해진 틀이 있기 때문에 상대적으로 파일에 비해서 쉽게 활용할 수 있다.(Java의 Socket API, httpServer 등) 여기서 한 번 더 제약(약속)을 걸어서 Socket 통신 기반으로 RESTful 스타일의 통신을 할 수 있다.(Resource에 대한 CRUD, Collection Pattern)  
  -> 통신할 때 제약이 있다는 것이 포인트다.

- RPC :
  Socket을 기반으로 RESTful한 통신을 할 때와 다르게 `제약이 없다`고 볼 수 있다. 즉 RESTful 스타일을 만족하지 않는다. Java에선 RPC를 위해 [RMI(Remote Method Invocation)](./rmi.md)란 기술을 제공한다. RPC(SOAP의 일반적 활용)와 RESTful의 차이점은 뒤에서 보충하겠다.  
  사실 우리가 웹에서 사용하는 기술은 광의의 RPC 개념이 들어가있다. 원격으로 프로시저를 호출하긴 하니까.. RMI라는 개념은 예전에 분산 환경에서 로컬에서도 객체를 사용하고, 원격에서도 객체를 사용하는 상황을 위해서 만들어졌다. 원격의 객체를 그냥 호출한다는 점에서 굉장히 `Free`하다고 볼 수 있다.
  REST는 그래서 객체 지향 관점에서는 퇴보한 방식이라고 볼 수 있다. 리소스에 대한 접근이라는 제약이 걸리기 때문이다.

#### 왜 DTO를 사용하는가

REST에서 핵심이 되는 것은 리소스와 표현이다. 표현에서 무엇을 할 수 있느냐? HTTP 요청 메서드가 생각난다. 원래 객체 지향 프로그래밍에서는 캡슐화로 객체 안의 것들이 들어나지 않는다. 여기서는 CRUD에 초점을 둬서 데이터(리소스)가 직접적으로 들어나게 된다. 따라서 제대로된 객체라고 볼 수 없는 `특별한 객체`를 사용하게 된다.  
여기서 특별하다는 것은 Java의 일반적인 개체의 정의를 만족하지 않는다는 것이다. 즉 원론적인 개체지향에서 상태와 동작을 모두 가지고 있어야 하지만 '특별한 객체'는 그렇지 않다. 마틴 파울러는 [무기력한 도메인](https://martinfowler.com/bliki/AnemicDomainModel.html)이라고 표현한다.

### 그래서 DTO가 뭔가요..?

- 아주 단순하게 setter 와 getter로 이루어짐
- JavaBeans라는 오래된 기술이 있는데 여기서 유래한 Java Bean(보통 그냥 Bean이라고 부름)의 형태에 가깝다.
  - Spring Bean, POJO와 다른 개념이다.
  - 자바빈즈는 관례라서 확실하게 정의된 것은 아님을 기억하자.
- 제대로 된 객체가 아니라 그냥 무기력한 데이터 덩어리이다.
- C/C++ 등에선 구조체(struct)로 구분할 수 있지만, Java에선 구조체 개념이 없어서 불가능하다.
  - 최신 Java에선 record라는 것을 활용할 수 있지만, 오래된 Bean 관련 라이브러리에선 지원하지 않는다.

### Tier 간 통신

- DTO는 Tier간(B/E 와 F/E) 통신에 사용 할 것이다. 하지만 DTO 객체 자체를 전송할 수 없다. 데이터를 전송하려면 바이트 스트림으로 바꿔주어야 한다. 이를 위해서 직렬화(마샬링)이 필요하다.

- 그렇다면 어떤 직렬화 기술을 사용할 것인가? 이것이 Json이다.

  - 참고로 XML도 가능하다.

- B/E와 DB사이에도 데이터 전송을 위해서 DTO 객체가 사용된다. 아주 예전에는 Value Object를 DTO란 의미로 썼지만, 빠르게 Transfer Object로 정정했다.
  - 한국의 오래된 SI 기업에서는 VO(Value Object)를 DTO란 의미로 사용한다. (DAO와 VO를 쓰고 있다면 대부분 여기에 속한다.) 결국 여기서 DTO는 B/E와 DB사이의 데이터를 말한다.
- JPA를 지양하고 DDD를 따르는 사람 중 일부는 ORM(JPA, 하이버네이트) [Active Record](./active-record.md) + DTO처럼 접근하기도 한다.

- Data Transfer의 개념에 집중하면 원격이 아닌 경우에도 DTO를 사용할 수 있다.
  - 마틴 파울러의 정의에 따르면 원격에서 정의된다.

TODO(여기서 개념 정리 필요 : REST VS RPC)
TODO(SOAP & RPC 예제 작성)

## 참고

### REST vs SOAP

| 특징            | REST API                                  | SOAP                                                  |
| --------------- | ----------------------------------------- | ----------------------------------------------------- |
| 아키텍처 스타일 | 간단하고 가벼운 아키텍처 스타일을 강조    | 높은 수준의 트랜잭션 및 신뢰성을 위한 아키텍처 스타일 |
| 프로토콜        | 주로 HTTP 프로토콜 사용                   | HTTP, SMTP, TCP 등 다양한 프로토콜 사용               |
| 포맷            | 주로 JSON 또는 XML을 사용                 | 주로 XML을 사용                                       |
| 상태            | Stateless로 설계되어 상태를 유지하지 않음 | Stateful로 설계되어 상태를 유지함                     |
| RPC 스타일      | RESTful 디자인으로 인해 RPC 스타일을 피함 | RPC 스타일을 따르며 메서드를 호출하여 동작함          |
| 보안            | 보안 기능이 상대적으로 덜 강조            | WS-Security와 같은 보안 기능 제공                     |
| 데이터 크기     | JSON이나 XML 데이터 크기가 작음           | XML 데이터 크기가 큼                                  |
| 사용자 경험     | 간단하고 직관적인 디자인                  | 상대적으로 더 복잡한 디자인                           |
| 확장성          | 유연하며 확장성이 좋음                    | 복잡한 트랜잭션 및 신뢰성을 지원하여 확장성이 좋음    |

#### 보충 설명

- 높은 수준의 트랜잭션? : ACID 조건에 추가적으로 분산 환경에서 고려되는 일관성, 비동기 처리 등의 요구사항이 높은 수준이 되었다고 이해하면 된다.

- WS-Security? : SOAP 기반의 웹 서비스 환경에서 메시지 수준의 보안을 제공하기 위해 디자인된 보안 규격

### Socket

[소켓이란?](https://www.geeksforgeeks.org/socket-in-computer-network/)

우선 소켓의 정의는 다음과 같다. 소켓은 네트워크에서 실행 중인 두 프로그램 간의 양방향 통신 링크 중 하나의 endpoint다. 소켓 메커니즘은 통신이 이루어지는 지정된 접점(contact point)를 설정하여 프로세스 간 통신(IPC)의 수단을 제공한다.

정의가 굉장히 압축적인데 조건을 하나 하나 따져보겠다.

- 조건 1 : 네트워크 환경에서 정의할 수 있다.
- 조건 2 : 실행 중인 두 프로그램(프로세스) 간의 양방향 통신 연결이 존재해야 한다.
- 조건 3 : 양방향 통신 연결에서 각 호스트의 endpoint를 소켓이라고 부른다.

결론적으로 소켓은 IPC(프로세스 간 통신) 수단이다. 여기서 위 조건 3개를 만족하는 개념을 소켓이라고 정의할 수 있다.

소켓의 종류에는 데이터그램 소켓과 스트림 소켓이 있다.

- 데이터그램 소켓  
  연결 지향이 아닌 프로토콜(UDP : User Datagram Protocol)을 사용한다. 따라서 UDP의 특성이 적용된다. 호스트 서로의 상태를 파악하지 않고 데이터 그램이 독립적으로 전송되며, 데이터그램 손실이나 순서가 뒤바뀔 수 있다는 단점이 있다.

- 스트림 소켓
  데이터그램 소켓과 달리 TCP를 사용하며 연결 지향적이다. 따라서 패킷의 순서가 보장된다. 그리고 데이터를 레코드(record)단위로 나눠서 전송하지 않고 경계없는 유일한 데이터 흐름을 제공한다.  
  여기서 레코드라는 개념이 좀 낯선 개념인데 어떤 응용 프로그램이나 프로토콜은 레코드 단위로 데이터를 나눠서 전송한다고 하네요. 여튼 여기서는 말 그대로 스트림이라서 경계가 없는 데이터 흐름이라고 생각하면 된다.

### 소켓 VS 파일

위에서 IPC의 수단에 대해서 소켓과 파일을 비교하는 부분을 보충하려고 한다.

| 특징      | 소켓(Socket)                        | 파일(File)                                  |
| --------- | ----------------------------------- | ------------------------------------------- |
| 통신 방식 | 주로 네트워크 통신 (TCP/IP 등)      | 주로 로컬 파일 시스템                       |
| 통신 범위 | 떨어진 시스템 간 통신 가능          | 주로 같은 시스템 내 프로세스 간 통신        |
| 통신 방향 | 양방향 통신 가능                    | 읽기는 동시에 가능하나 쓰기는 동시에 불가능 |
| 보안      | SSL/TLS 등의 암호화 사용 가능       | 파일 권한 및 접근 제어로 보안               |
| 용도      | 원격 서버와의 통신, 네트워크 서비스 | 로컬 데이터 저장, 공유, 읽기 및 쓰기        |

### RPC

RPC(원격 프로시저 호출)의 정의는 프로그램이 네트워크를 통해 다른 프로그램의 함수 또는 프로시저를 호출하는 메커니즘이다.  
RPC의 주요 구성 요소 및 특징은 아래와 같다.

- 클라이언트-서버 모델: RPC는 일반적으로 클라이언트와 서버 간의 통신 모델을 따른다. 클라이언트는 원격 서버에 있는 함수를 호출하고, 서버는 해당 함수를 실행한 결과를 클라이언트에게 반환한다.

- 프로시저 호출: RPC에서는 원격지에 있는 함수나 프로시저를 로컬 함수를 호출하는 것과 유사하게 호출할 수 있다.

- 프로토콜과 직렬화: RPC는 네트워크 통신을 위해 프로토콜을 사용하며, 함수 호출과 반환 값 등의 데이터를 직렬화하여 전송한다. 널리 사용되는 RPC 프로토콜로는 gRPC, Apache Thrift, CORBA 등이 있다.

- 원격 객체나 서비스: RPC는 원격에서 호출되는 함수 뿐만 아니라, 원격 객체나 서비스의 메서드를 호출하는 데에도 사용된다. 이를 통해 원격 시스템의 기능을 로컬처럼 사용할 수 있다.

- 동기 및 비동기 호출: RPC는 동기적이거나 비동기적인 방식으로 함수를 호출할 수 있습니다. 동기 호출은 함수 호출이 완료될 때까지 대기하며, 비동기 호출은 호출이 완료되기를 기다리지 않고 다른 작업을 계속할 수 있다.

RPC는 분산 시스템에서 서로 다른 시스템 간에 효율적이고 투명한 통신을 가능하게 해주는 중요한 기술 중 하나입니다.

### JavaBeans

[자바빈즈](https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94%EB%B9%88%EC%A6%88)

- 개념: JavaBeans는 재사용 가능한 컴포넌트로서, 일반적으로 GUI와 관련된 요소를 구성하는 데 사용된다. JavaBeans의 조건은 위의 위키피디아를 참고하자.

  - 이 조건은 관례상 조건이다.

- 특징: 대표적인 특징으로 이들은 getter 및 setter 메서드를 통해 접근된다.

### Java record

kotlin의 데이터 클래스와 유사하다.

Java 14부터 도입된 Record는 데이터를 간결하게 표현하기 위해 만들어졌다. Record는 불변(Immutable) 데이터를 나타내기 위한 목적으로 디자인되었으며, 주로 데이터 저장 및 전달을 위한 클래스를 생성하는 데 사용된다.

Record 클래스 규칙은 아래와 같다.

- 불변성(Immutable): Record 클래스는 생성된 후 수정될 수 없는 불변 객체다. 따라서 클래스 필드들은 final로 선언되어 값을 한 번 할당하면 변경할 수 없다.

- 기본 생성자(Default Constructor): Record 클래스는 컴파일러에 의해 자동으로 기본 생성자가 생성되어 필드들을 초기화한다.

- equals(), hashCode(), toString() 자동생성 : 필드들의 값에 기반하여 객체의 동등성(equality)을 확인하는 equals() 및 hashCode() 메소드, 그리고 객체의 문자열 표현을 반환하는 toString() 메소드를 생성한다.

- 프로퍼티(Property): Record 클래스는 각 필드에 대한 "프로퍼티"를 자동으로 생성한다. 프로퍼티는 필드의 값을 읽는 메소드와, 해당 필드를 사용하여 새로운 객체를 생성하는 메소드를 포함한다. [예시](https://docs.oracle.com/en/java/javase/17/language/records.html#GUID-6699E26F-4A9B-4393-A08B-1E47D4B2D263)를 통해 확인하자.

```<Java>
public final class Rectangle {
    private final double length;
    private final double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    double length() { return this.length; }
    double width()  { return this.width; }

    // Implementation of equals() and hashCode(), which specify
    // that two record objects are equal if they
    // are of the same type and contain equal field values.
    public boolean equals...
    public int hashCode...

    // An implementation of toString() that returns a string
    // representation of all the record class's fields,
    // including their names.
    public String toString() {...}
}
```

이렇게 getter와 유사한 프로퍼티 메서드를 자바 컴파일러는 생성한다. 필드와 이름이 똑같다.
