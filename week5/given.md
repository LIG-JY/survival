# Mockito의 given 메서드

## 메서드 구현

```<Java>
public static <T> BDDMyOngoingStubbing<T> given(T methodCall) {
        return new BDDOngoingStubbingImpl(Mockito.when(methodCall));
    }
```

- 매개 변수: 이 메소드는 제네릭 타입 T를 반환하는 메소드 호출을 인자로 받습니다. 여기서 T는 인자의 메소드가 반환하는 객체의 타입입니다.

- 반환형 : Mockito.when 메소드를 사용하여 주어진 메소드 호출(methodCall)에 대한 동작을 설정합니다. 이후 BDDOngoingStubbingImpl 객체를 생성하여 반환합니다. BDDOngoingStubbingImpl은 BDD 스타일의 stubbing을 위한 구현체입니다.

## 역할 및 사용

- BDD 스타일: 이 함수는 BDD 스타일의 테스트 케이스 작성을 지원합니다. BDD는 'Given-When-Then' 형식을 사용하여 테스트를 보다 이해하기 쉽고 자연스러운 언어로 기술하는 것을 목표로 합니다. given은 이 형식의 'Given' 부분에 해당합니다.

- Stubbing: 함수는 메소드 호출에 대한 stubbing을 설정합니다. Stubbing은 테스트 중에 특정 메소드가 호출될 때 원하는 특정 값이나 동작을 반환하도록 설정하는 것을 의미합니다. 예를 들어, 어떤 메소드가 특정 인자로 호출될 때 특정 값을 반환하거나, 예외를 발생시키도록 설정할 수 있습니다.

## 사용 예시

```<Java>
given(someMock.someMethod("someArg")).willReturn("mockedValue");
```

이 예시에서 someMethod가 "someArg" 인자로 호출될 때 "mockedValue"를 반환하도록 설정하고 있습니다.
