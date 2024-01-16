# AssertJ

## assertThat() 메소드

### 목적

AssertJ의 assertThat() 메소드의 목적은 테스트에서 assertion을 만드는 것이다.

### 특징

- assertThat() 메소드는 테스트에서 특정 조건을 만족하는지 확인할 수 있다.

- assertThat() 메소드는 fluent API다. 따라서 가독성, 메소드 체이닝의 효과가 있다.

- assertThat() 메소드 뒤에는 다양한 조건을 assertion 하기 위한 여러 메소드가 따라올 수 있다.

### 메서드 시그니처

```<Java>
public static <T> AbstractAssert<?, T> assertThat(T actual)
```

- T : assertion 하는 객체의 타입이다. 제너릭 타입으로 어떤 타입의 객체든 assertion 할 수 있는 함수 인자다.

- ? : AbstractAssert<?, T>가 리턴 타입이다. AbstractAssert 클래스는 다양한 assertion 메소드를 지원한다. assertThat() 메소드에 입력으로 들어오는 객체의 타입 T에 따라서 와일드 카드 ? 가 결정된다. 예를 들어 T가 String 타입이라면 String 관련 assertion 메소드를 지원하는 AbstractAssert의 하위 클래스 타입을 리턴하게 된다.
