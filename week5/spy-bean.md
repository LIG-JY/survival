# SpyBean

Spy Bean은 테스트 시에 실제 Bean의 동작을 "간첩처럼" 관찰하면서, 특정 메서드의 동작만을 오버라이드(재정의)하거나 검증하고 싶을 때 사용하는 기술이다.

Spy Bean은 실제 Bean의 인스턴스를 사용하되, 그 중 일부 동작을 모의(Mock) 객체와 같이 변경할 수 있다.

## 특징

- 실제 Bean 사용: Spy Bean은 실제 Bean의 인스턴스를 사용합니다. 이는 Mock 객체와의 주요 차이점입니다. Mock 객체는 전체 Bean을 가짜로 대체하지만, Spy Bean은 기존 Bean을 유지하면서 `특정 부분만` 변경할 수 있습니다.

- 선택적 오버라이드: Spy Bean을 사용하면 Bean의 특정 메서드를 오버라이드해서 원하는 행동으로 대체할 수 있습니다. 나머지 메서드는 실제 Bean의 동작을 그대로 유지합니다.

- 행동 검증: Spy Bean을 사용하여 메서드 호출이 예상대로 이루어졌는지, 몇 번 호출되었는지 등을 검증할 수 있습니다.

## 사용법

Spring 테스트에서 @SpyBean 어노테이션을 사용하여 Spy Bean을 생성할 수 있습니다.

```<Java>
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;

@SpringBootTest
public class MyServiceTest {

    @SpyBean
    private MyService myService;

    @Test
    public void testSomeMethod() {
        // 메서드 동작을 오버라이드
        Mockito.doReturn("Something else").when(myService).someMethod();

        // 테스트 실행
        String result = myService.someMethod();

        // 결과 검증
        assertEquals("Something else", result);

        // 원래 메서드가 호출되었는지 확인
        Mockito.verify(myService).someMethod();
    }
}

```

## Mockito.verify() 메서드

Mockito에서 verify 메서드는 테스트 중에 특정 메서드가 호출되었는지, 호출되었다면 몇 번이나 호출되었는지 검증하는 데 사용됩니다.

### 기본 시그니처

```<Java>
public static <T> T verify(T mock)
```

- T mock: 검증하고자 하는 모의 객체(Mock object)입니다. 이 객체의 메서드 중 어느 하나가 호출되었는지 검증하고자 할 때 사용합니다.

### 호출 횟수를 지정하는 시그니처

```<Java>
public static <T> T verify(T mock, VerificationMode mode)
```

- T mock: 검증하고자 하는 모의 객체입니다.

- VerificationMode mode: 메서드 호출 횟수나 특정 조건(예: times(int), never(), atLeast(int), atMost(int) 등)을 지정합니다.

### verify() 예시

```<Java>
// 일반적인 사용법
verify(mockObject).someMethod();

// 메서드가 정확히 한 번 호출되었는지 검증
verify(mockObject, times(1)).someMethod();

// 메서드가 호출되지 않았는지 검증
verify(mockObject, never()).someMethod();
```

## argThat() 메서드

Mockito의 argThat 메서드는 인자가 특정 조건을 만족하는지 검증할 때 사용됩니다. 이 메서드는 ArgumentMatcher 인터페이스를 사용하여 메서드 호출 시 전달된 인자가 정의된 조건을 만족하는지 확인합니다.

주로 모의 객체(Mock Object)의 메서드 호출을 검증할 때 사용되며, 특히 인자의 특정 속성이나 상태를 검사해야 할 때 유용합니다.

### argThat의 시그니처

```<Java>
public static <T> T argThat(ArgumentMatcher<T> matcher)

```

T: 검증하고자 하는 인자의 타입입니다.

ArgumentMatcher<T> matcher: 인자가 만족해야 할 조건을 정의하는 ArgumentMatcher입니다.
