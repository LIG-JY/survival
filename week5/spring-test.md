# Spirng Test

## Content

### Integration Test

Spring 공식 문서는 테스트를 크게 둘로 나눠서 설명한다.

- Unit Testing
- Integration Testing

언제 Spirng의 힘을 빌려서 테스트 할까? IoC 컨테이너를 적극적으로 활용하고 싶거나, Spring Web MVC로 구현된 부분을 테스트하고 싶을 때다. 이런 테스트를 통합 테스트라고 부른다.

---

### Spring Boot Test

Spring Boot 1.4부터 @SpringBootTest Annotation을 써서 쉽게 테스트할 수 있다.

### Spring Boot Test 실습

PostController을 테스트 해보자.

#### @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

테스트의 환경을 설졍할 수 있다. 이 때 WebEnvironment Enum 값으로 어떤 웹 환경에서 테스트할 것인지 결정할 수 있다.

[Difference between webEnvironment = RANDOM_PORT and webEnvironment = MOCK](https://stackoverflow.com/questions/58364490/difference-between-webenvironment-random-port-and-webenvironment-mock)

이번 실습은 통합 테스트를 하기 위해 *RANDOM_PORT*로 지정한다. 통합 테스트는 최대한 프로덕션 환경과 유사할 수록 좋다고 합니다.

#### @Value("${local.server.port}")

Spring의 프로퍼티(예: **application.properties 또는 application.yml 파일**)나 환경 변수에서 local.server.port 키에 해당하는 값을 찾는다.

그리고 이 값을 변수에 할당하게 된다.

#### TestRestTemplate

@Autowired로 TestRestTemplate 빈을 주입받는다.

조회 테스트

생성 테스트

삭제 테스트

정규식

## 참고

> [Testing](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)

> [Testing improvements in Spring Boot 1.4](https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4)

> [Testing the Web Layer](https://spring.io/guides/gs/testing-web/)

> [@Value](https://www.baeldung.com/spring-value-annotation)
