# MockMvc

## RestTemplate과 차이점

RestTemplate의 원래 목적은 HTTP 클라이언트다. 따라서 실제로 HTTP 호출을 하게된다. 따라서 내부 서버(직접 제작한 스프링 어플리케이션) 뿐만아니라 외부 REST API에 데이터를 요청하거나 전송할 수 도 있다.

MockMvc의 목적은 spring MVC 애플리케이션의 컨트롤러 계층을 테스트다. 실제 HTTP 서버를 구동하지 않고도 Spring MVC의 동작을 시뮬레이션 한다.

MockMvc는 주로 Spring MVC 컨트롤러의 HTTP 요청 처리 로직을 테스트하는 데 사용됩니다. 이를 통해 컨트롤러가 올바른 뷰를 반환하는지, 모델에 적절한 속성이 추가되었는지 등을 검증할 수 있다.

## 목적

@MockMvcTest is a Spring Boot annotation used in unit testing and integration testing for web applications built with the Spring Framework. It's primarily used for testing `controllers and the web layer of your application` by setting up a MockMvc environment, which allows you to simulate HTTP requests and responses without actually starting a web server.   
The purpose of @MockMvcTest is to simplify the testing of your Spring MVC controllers and their interactions with the Spring MVC framework.

It creates a limited Spring application context that includes only the relevant components required for testing the web layer. This context is faster to initialize than a full application context.
It sets up a MockMvc instance, which allows you to send mock HTTP requests to your controllers and receive mock HTTP responses for testing.
It often focuses on testing the controller methods, the request mappings, request and response handling, and the interactions between the controller and other components like services.

## 사용법

To use @MockMvcTest, you typically annotate your test class with it and specify the controller classes you want to test by providing them as arguments to the annotation.
Spring Boot will create a minimal application context containing only the relevant beans, such as the specified controller classes, and set up a MockMvc instance for you to use in your test methods.

## 메서드 사용법

### @AutoConfigureMockMvc 어노테이션

@AutoConfigureMockMvc 어노테이션은 Spring Boot 테스트에서 MockMvc 객체를 자동으로 구성하고 주입하기 위해 사용된다. 클래스 레벨에 적용된다.

### perform() 메서드

시그니처는 아래와 같다.

```<Java>
ResultActions perform(RequestBuilder requestBuilder) throws Exception
```

perform 메서드는 MockMvc 객체를 사용하여 HTTP 요청을 시뮬레이션하고, 그 결과를 ResultActions 객체로 반환한다.

이 메서드는 다양한 요청 방식(GET, POST 등)을 테스트할 때 사용된다.

### andExpect() 메서드

시그니처는 아래와 같다.

```<Java>
ResultActions andExpect(ResultMatcher matcher) throws Exception
```

andExpect 메서드는 ResultActions 객체를 사용하여 HTTP 응답을 검증한다.

다양한 ResultMatcher를 통해 상태 코드, 반환된 컨텐츠, 헤더 등을 검증할 수 있다.
