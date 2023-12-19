# REST API

## contents

### Layer의 구분

#### Layer vs Tier

관례상 Tier은 물리적인 구분, Layer는 논리적인 구분을 의미한다.

#### F/E 와 B/E

사용자에 가까운 부분을 Front-end, 줄여서 F/E라고 부르고, 사용자에게서 먼 부분을 Back-end, 줄여서 B/E라고 부른다.  
F/E와 B/E를 `연결하는 방법`이 다양한데, 최근에는 HTTP, 더 정확히는 Web 기술을 활용하고, REST API라는 걸 사용한다. 이외에도 SOAP, GraphQL 등 다른 것도 사용할 수 있다.

Rough하게 이야기하면, 서버는 백엔드, 클라이언트는 프론트엔드에 해당한다. 일반적인 웹 서비스에선 웹 브라우저라는 클라이언트 프로그램 위에서 작동하는 JavaScript 프로그램을 개발할 때 프론트엔드를 개발한다고 이야기하고, Tomcat 등의 웹 애플리케이션 서버(WAS) 위에서 작동하는 Java Servlet, Spring Boot 프로그램을 개발할 때 백엔드를 개발한다고 이야기한다.

- 참고
- [Multitier architecture](https://en.wikipedia.org/wiki/Multitier_architecture)
- [REST와 SOAP 비교](https://www.redhat.com/ko/topics/integration/whats-the-difference-between-soap-rest)
- [GraphQL](https://graphql.org/)
- [Apache Tomcat](https://ko.wikipedia.org/wiki/아파치_톰캣)

### API

[Application Programming Interface](https://ko.wikipedia.org/wiki/API)

#### Interface

[GoF의 디자인패턴 1.6 : 인터페이스란 무엇인가? 인터페이스를 정의할 때 사용하는 용어와 표현](https://github.com/ahastudio/til/blob/main/oop/glossary.md)

객체가 선언하는 모든 연산은 연산의 이름, 매개변수로 받아들이는 객체들, 연산의 반환 값을 명세합니다. 이를 연산의 시그너처(signature)라고 한다.

인터페이스(interface)는 객체가 정의하는 연산의 모든 시그너처들을 일컫는 말로 객체의 인터페이스는 객체가 받아서 처리할 수 있는 연산의 집합이다.

- Communication
- Specification
- [Information Hiding (Principle)](https://en.wikipedia.org/wiki/Information_hiding)
- Encapsulation (Technique)
  - 기술적으로 정보 은닉을 하는 대표적인 방법이 캡슐화이다.
- Implementation(구현과 인터페이스의 구분)

### REST(Representational State Transfer)

[Web API](https://en.wikipedia.org/wiki/Web_API)

> **Roy Fielding** - “[Architectural Styles and the Design of Network-based Software Architectures](https://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm)” (2000)

아키텍처와 아키텍처 스타일을 설계 상에서는 엄격하게 구분한다.

#### 해당 논문의 5장 - [Representational State Transfer (REST)](https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm)

##### 1️⃣ **Starting with the Null Style**

설계는 초기에는 아무것도 없는 상태에서 제약 조건을 식별하고 적용하면서 이루어진다.

Null Style은 제약 조건이 없는 초기의 상태를 말한다. 즉 컴포넌트간의 뚜렸한 영역 구분이 없는 상태이다.

##### 2️⃣ **Client-Server**

Separation of Concerns(관심사의 분리)가 기본 원칙이다. UI 관심사와 데이터 저장 관심사를 분리함으로써 여러 플랫폼에서 UI의 이식성(Portability)을 향상시킬 수 있다. 그리고 서버 컴포넌트는 확장성(Scalability)이 향상된다. 즉 관심사의 분리는 각각 컴포넌트가 독립적으로 진화할 수 있게 도와준다.

##### 3️⃣ **Stateless**

HTTP의 무상태성과 유사한 개념이다. 클라이언트가 서버로 보내는 요청은 서버가 요청을 이해하는데 필요한 모든 정보를 포함해야한다. 따라서 세션 상태는 전적으로 클라이언트에 의해서 유지된다.  
논문에 따르면 무상태성의 장점과 단점에 대한 설명까지 나와있다. 가장 대표적인 장점은 서버의 확장성이다.

> (장점) This constraint induces the properties of visibility, reliability, and scalability. Visibility is improved because a monitoring system does not have to look beyond a single request datum in order to determine the full nature of the request. Reliability is improved because it eases the task of recovering from partial failures. Scalability is improved because not having to store state between requests allows the server component to quickly free resources, and further simplifies implementation because the server doesn't have to manage resource usage across requests.  
> (단점) Like most architectural choices, the stateless constraint reflects a design trade-off. The disadvantage is that it may decrease network performance by increasing the repetitive data (per-interaction overhead) sent in a series of requests, since that data cannot be left on the server in a shared context. In addition, placing the application state on the client-side reduces the server's control over consistent application behavior, since the application becomes dependent on the correct implementation of semantics across multiple client versions.

##### 4️⃣ **Cache**

캐시 제약조건의 요구 사항은 응답에 포함되는 데이터는 cacheable 한지 non-cacheable한지 표시(labeled)되어야 한다는 말이다.

##### 5️⃣ **Uniform Interface → REST architectural style의 핵심!**

아래는 논문의 인용이다.

1. “The **central feature** that distinguishes the REST architectural style from other network-based styles is its emphasis on a **uniform interface** between components”
   - components는 rough하게 서버와 클라이언트라고 생각하면 된다.
2. “By applying the **software engineering principle of generality** to the component interface, the overall system architecture is **simplified** and the **visibility** of interactions is improved.”
3. “**Implementations** are **decoupled** from the services they provide, which encourages **independent evolvability**.”
4. “The **trade-off**, though, is that a uniform interface **degrades efficiency**, since information is transferred in a **standardized form** rather than one which is specific to an application's needs.”
   - standardized form이기 때문에 개별적인 application의 필요에 최적화된 커스터마이징 수단은 아니다.
5. “The REST interface is designed to be efficient for **large-grain hypermedia data transfer**, optimizing for the **common case of the Web**, but resulting in an interface that is not optimal for other forms of architectural interaction.”
6. **필딩 제약 조건**  
   논문 저자 이름에서 유래한 조건이다.

   1. Four Interface Constraints

      Identification of Resources → URI 등으로 리소스를 식별할 수 있다.

      Manipulation of Resources through Representations → 표현으로 리소스를 조작한다.

      Self-descriptive Messages → 메시지는 [자기서술적](#자기서술적)이기 때문에 여러 레이어에서 처리/변환 가능하다.

      > JSON 같은 범용 포맷을 작게 사용하면 어떻게 해석해야 하는지 알 수 없기 때문에 자기서술적이기 어렵다. 뒤에서 다룰 MIME 타입으로 설명한다면, application/json이 아니라 application/dns+json 같은 타입을 써야 한다.
      > `REST API를 이야기할 때 까다로운 부분 중 하나.`

      Hypermedia as the Engine of Application State → 줄여서 HATEOAS라고 부른다. `REST API를 이야기할 때 까다로운 부분 중 하나.`

   2. 아키텍처 요소(5.2)에서 리소스와 표현을 구분

      Resource는 특정 시점의 스냅샷이 아니라 `추상`이다. 즉 모든 시간에 통용되는 엔티티 집합이다 객체지향에서 말하는 Entity라고 생각하면 편하다. <객체지향의 사실과 오해>의 표현을 빌린다면, “앨리스”라는 리소스는 키가 커지던 작아지던 항상 “앨리스”다.

      Representation = Data + Metadata + Meta-metadata…  
      사실상 HTTP 메시지라고 보면 된다. 예를 들어, 리소스를 어떻게 조작할 것인가는 HTTP Method로 표현하게 되고, 리소스를 무엇으로 조작할 것인가는 Content-Type과 Body로 표현하게 된다.

   3. URI 파트(6.2)에서 리소스에 대해 다시 강조

      “The resource is not the storage object. The resource is not a mechanism that the server uses to handle the storage object.”

      리소스, 표현, 실제 데이터 등은 전부 구분된다는 것을 명심하자.

   4. 아키텍처 데이터 뷰(5.3.3)에서 HATEOAS에 대해 언급.

      마지막 문단의 첫 문장: “The model application is therefore an **engine** that moves from one state to the next by examining and **choosing** from among the alternative **state transitions** in the current set of **representations**.”

      > 이렇게 하려면 표현에 선택 가능한 상태 전환이 포함돼야 한다.  
      > 이게 바로 `하이퍼미디어 링크`

      현실에서 대부분은 효율 문제로 표현에 링크를 넣지 않고, 클라이언트 개발자가 API 문서를 활용해 처리한다. 표현에서 상태 전환을 선택하는 게 아니라, API 문서(말 그대로 API 문서에 API 사용법이 적혀있다. 대표적으로 Swagger)를 참조해서 상태 전환을 강제하는 것.

      [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)

      > `RESTful Web API`의 공저자인 레오나르드 리처드슨은 Hypermedia Control(대표적인 게 바로 링크)을 강조.  
      > 성숙한 REST라면 표현에 하이퍼미디어 컨트롤(링크)이 포함되어야 한다.

##### 6️⃣ **Layered System**

##### 7️⃣ **Code-On-Demand**

REST는 API를 위한 아키텍처 스타일이 아니다. 논문에서 밝힌 것처럼 “common case of the Web”에 특화된 방법이다. 하지만 API를 만들 때 유용하게 활용할 수 있다.

로이 필딩은 필딩 제약 조건을 지키지 않는 API를 REST API라고 부르는 것에 반대하겠지만, 업계에서 그냥 리처드슨 성숙도 모델의 레벨 2만 만족해도 REST API라고 부르고 있기 때문에 여기서도 그냥 REST API라고 이야기할 예정.

###### 자기서술적

여러 layer가 나눠진 상황에서 메세지를 처리하거나 변환할 수 있어야한다. 메세지를 처리 또는 변환하려면 메세지의 해석해야한다. 따라서 자기서술적인 성질이 필요하다. 즉 컴퓨터 입장에서 이해할 수 있어야한다. 참고로 HTML은 대부분 자기 서술적이다.
