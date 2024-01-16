# 5주차 DI & Spring Test

## 목표

1. 의존성 주입이란 무엇인지 이해한다.

2. 왜 테스트 코드를 작성하는지를 알고 테스트 코드를 작성할 수 있다.

## 목차

1. [Dependency Injection](./dependency-injection.md)
2. [Unit Test](./unit-test.md)
3. [Spring Test](./spring-test.md)
4. [TDD](./tdd.md)
5. [부록 : AssertJ](./assertj.md)
6. [부록 : restTemplate의 메서드](./rest-template.md)

## keyword

### Dependency Injection

- Facotry Pattern

  1.단일 책임 원칙  
  2.싱글턴
  3.Pluggable

- Dependency Injection
- IoC(Inversion of Control)
- BeanFactory
  - BeanDefinition
  - BeanDefinitionRegistry
- ApplicationContext
  - Java Config 에 @Bean 어노테이션을 활용해 빈 등록
  - 클래스에 @Component를 포함한 어노테이션을 활용해 빈 등록

### Unit Test

- V 모델
- Test Matrix
- JUnit
- 단위 테스트
