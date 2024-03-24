# Spring Data JPA

## DAO와 Repository

- DAO : Data Access에 초점을 둔다.
- Repository : 개체를 관리하는 것에 초점을 둔다.

EntityManager가 Persistence Context를 사용하고 있기 때문에 JPA의 Repository를 DDD의 Repository처럼 사용할 수 있다.

## @Transactional

트랜잭션 범위는 Spring AOP를 이용한 @Transactional 애너테이션으로 지정할 수 있다. 일반적으로는 DDD의 Application Layer가 트랜잭션 경계가 된다.

![alt text](https://wikibook.co.kr/images/readit/20141002/figure4-1.png)

@Transactional 어노테이션은 class level, method level 자유롭게 사용하면 된다. class level로 지정하면 하위 메소드가 모두 @Transactional이 적용된다.

## Application layer를 테스트 하는데 중점을 두고 싶다면?

unit test로 aplication layer 즉 @service가 붙은 개체에만 초점을 두고 테스트하고 싶다면 repository를 mocking하면 된다.

반면 service는 ApplicationContext의 것을 그대로 사용한다.

아래는 @MockBean을 사용하는 예제다.

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class CreateItemServiceTest {

    @Autowired
    private CreateItemService createItemService;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    void createItemTest() {
        // 가정: itemRepository.save() 메서드 호출 시, 동일한 Item 객체를 반환하도록 설정
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(1000);

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // 행동: CreateItemService를 통해 아이템 생성
        Item createdItem = createItemService.createItem(item);

        // 검증: 반환된 Item 객체가 예상대로 설정되었는지 검증
        assertNotNull(createdItem);
        assertEquals("Test Item", createdItem.getName());
        assertEquals(1000, createdItem.getPrice());

        // 검증: itemRepository.save()가 한 번 호출되었는지 확인
        verify(itemRepository, times(1)).save(any(Item.class));
    }
}
```

## @Transactional 테스트의 실용성에 대한 설명

### DB를 사용하는 테스트는 어려움

내장형 DB가 아니라면 테스트 대상 애플리케이션 밖에서 동작하는데다, 데이터가 테스트 수행마다 변경이 되면 일관된 테스트 결과를 보장해줄 수 없기 때문이다.

Spring에서 권장하는 롤백 테스트를 @Transactional로 수행할 수 있다. 이는 독립적인 테스트를 보장할 수 있다. 하지만 @Transactional 테스트는 테스트 수행 중에 `단 한 개의 트랜잭션 경계`만 사용이 되고, 그 경계를 테스트 메소드로 확장을 해도 문제가 없는 상황에서만 유효하다.

### @Transactional의 문제점 예시

1. JPA의 detached 상태 오브젝트의 변경이 자동감지 되지 않는 코드가 @Transactional이 붙은 테스트에서는 변경되는 현상

@Transactional을 테서트 메서드에 사용하면 테스트 메서드 내부에서 수행되는 모든 JPA 관련 작업은 단일 영속성 컨텍스트 내에서 처리된다. 이는 @Transactional 어노테이션이 테스트 메서드에 적용될 때, 해당 메서드를 실행하는 전체 과정이 하나의 트랜잭션으로 묶이기 때문이다.

결국 문제는 실제 어플리케이션의 트랜잭션 경계와 테스트에서 트랜잭션 경계가 달라진다는 것이다.

2. @Transactional이 동일 클래스의 메소드 사이의 호출에서는 적용되지 않는(스프링의 기본 프록시AOP를 사용하는 경우라면) 문제

스프링은 AOP를 이용하여 @Transactional 애노테이션을 처리한다. 특정 메소드에 @Transactional이 적용될 때, 스프링은 해당 객체의 프록시를 생성한다.(AOP) 이 프록시는 실제 객체를 감싸고 있으며, 메소드 호출을 가로채 트랜잭션 관리 로직을 수행한 후 실제 객체의 메소드를 호출한다.

동일 클래스 내의 메소드 호출에서는 프록시를 거치지 않고 직접 메소드를 호출하기 때문에 @Transactional 애노테이션이 적용되지 않는다. 따라서 트랜잭션이 생성되지 않는다.

이 문제를 해결하기 위한 일반적인 접근 방법은 자기 참조(self-reference)를 사용하는 것이다. 즉, 클래스 내부에서 자신의 또 다른 메소드를 호출할 때는 자기 자신의 프록시를 통해 호출해야 한다. 프록시에서 트랜잭션 관리 로직을 수행하기 때문이다. ApplicationContext를 사용하여 현재 빈의 프록시 객체를 가져온 후, 이 프록시 객체를 통해 메소드를 호출할 수 있다.

```java
@Service
public class MyService {
    @Autowired
    private ApplicationContext context;

    public void methodA() {
        MyService proxy = context.getBean(MyService.class);
        proxy.methodB();
    }

    @Transactional
    public void methodB() {
        // 트랜잭션 관련 작업
    }
}
```

3. @Transactional + JPA 조합을 사용하는 상황에서 save한 오브젝트가 영속 컨텍스트에만 존재하고 db로 flush되지 않은 상태로 rollback되기 때문에 명시적으로 flush하지 않으면 실제 db `매핑에 문제가 있어도 검증하지 못한다`는 문제가 생긴다.


[링크](https://www.inflearn.com/questions/792383/테스트에서의-transactional-사용에-대해-질문이-있습니다)

https://www.youtube.com/watch?v=-961J2c1YsM