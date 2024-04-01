# Assignment 7

## GloablExceptionHandler

```java
package kr.megaptera.assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException e, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static class ErrorDetails {
        private HttpStatus status;
        private String message;
        private String details;

        public ErrorDetails(HttpStatus status, String message, String details) {
            this.status = status;
            this.message = message;
            this.details = details;
        }
    }
}

```

### ErrorDetail과 ResponseEntity의 HTTP 상태코드는 다를 수 있음

핸들러의 코드를 보면 ErrorDetail에 HTTP 상태코드가 이미 포함되어 있는데, ResponseEntity에 HTTP 상태코드를 또 포함하는지 의문점이 들 수 있다.

ErrorDetail 객체의 Http 상태코드는 오류에 대한 세부정보다. 응답 메시지(에러)의 내용 중 일부임

ResponseEntity의 Http 상태코드는 서버가 클라이언트에게 응답을 보낼 때 나타난다.

결론적으로 세부 정보(ErrorDetail)과 실제로 클라이언트에게 반환되는 응답 코드는 다를 수 있다. 필요에 따라 클라이언트에게 200 상태 코드를 보내면서, 오류 상세 정보를 전달할 수 도 있음.

### inner 클래스에는 static을 붙여주는 것이 좋음

- 독립성 : static inner class는 외부 클래스(GlobalExceptionHandler) 개체 생성 없이 생성될 수 있고, 외부 클래스의 멤버에 접근할 수 없다.

- 메모리 효율 : static inner class는 외부 클래스에 대한 [숨겨진 참조](https://saranganjana.medium.com/non-static-inner-classes-in-java-f9b05dbe3f57)를 가지지 않음

따라서 두 클래스가 명확하게 독립적임을 나타내기 위해 static을 붙일 것을 권함

## DDL

테이블과 같은 데이터 구조(스키마)를 정의하는데 사용되는 명령어

### 스키마

- 데이터베이스 내에 어떤 테이블이 존재하는지
- 테이블들은 어떤 열을 가지고 있는지
- 각 열의 데이터 타입은 무엇인지
- 테이블끼리 관계
- 테이블의 key
- 테이블에 적용되는 제약조건

### DDL과 스키마의 관계

데이터베이스, 테이블, 인덱스 등 새롭게 정의하고, 수정하고 삭제하는 명령어를 DDL이라고 함

### JPA와 DDL

JPA는 클래스의 매핑 정보를 통해서 데이터베이스 스키마를 생성할 수 있다. (데이터 베이스 방언도 참조함)

이 기능은 ddl-auto를 설정 파일의 프로퍼티로 추가하면 사용할 수 있다. (여기서 설정 파일은 순수 jpa를 사용하면 persistence.xml, sprig-data-jpa 사용 시 application.properties)

참고로 show-sql 옵션을 프로퍼티로 추가하면 스키마를 생성하기 위해 jpa에서 만들어주는 DDL을 출력할 수 있다. 확인하는 용도로 사용하면 된다.

## spring-data-jap ddl-auto: create 주의할 점

create 설정은 애플리케이션이 시작될 때마다 데이터베이스의 스키마를 새로 생성한다는 것을 의미한다. 이 과정에서 기존에 존재하던 테이블은 삭제되고, 새로운 스키마가 정의된 대로 테이블이 다시 생성된다. 결과적으로, 이전에 데이터베이스에 저장되어 있던 모든 데이터는 사라지게 됩니다. 따라서 개발 환경에서는 해도되는데 운영에서 `절대`사용해선 안 된다!

예전 프로젝트에서 팀원이 이거 설정해나서 DB 날린 경험이.. ㅎㅎ;

## Intellij 경고

PostId를 구현하고 @Embeddable 어노테이션을 붙여서 Post 개채의 id 개체로 사용하고 있음

```java
@Embeddable
public class PostId {
    private final Tsid value;

    private PostId(Tsid value) {
        this.value = value;
    }

    ...
}
```

```java
@Entity
public class Post {
    @EmbeddedId
    @Column(name = "ID")
    private PostId postId;

    ...
}
```

이렇게 구현하면 PostId에 빨간줄이 나오고 컴파일러 경고가 나옴.. 
> Class 'PostId' should implement 'java.io.Serializable'

근데 실행하면 정상 작동함..ㅋ

찾아보니까 JPA 표준 스펙에서 임베디드 타입은 직렬화 해야한다는 규칙이 있다고 한다.

[참고](https://www.inflearn.com/questions/17117/serializable-질문-드립니다)

> 결론부터 말씀드리면 kim님이 생각하신게 맞습니다. 그런데 JPA 표준 스펙은 Entity에 Serializable를 구현하도록 되어 있습니다. JPA 구현체에 따라 엔티티를 분산 환경에서 사용할 수 있거나, 직열화해서 다른 곳에 전송할 수 있는 가능성을 열어준거지요. 하지만 저희가 주로 사용하는 하이버네이트 구현체는 제가 아는 바로 다음 사례를 제외하고는 Entity를 직열화해서 내부에서 사용하는 경우는 못봤습니다. https://www.inflearn.com/questions/16570 그래서 저는 실용적인 관점에서 실무에서 엔티티에 Serializable를 거의 사용하지 않습니다. (하지만 표준 스펙이니 적용해두는게 더 나은 선택입니다.) 감사합니다^^

결론은 이렇다. 그래서 PostId 클래스에 implements Serializable을 추가해주면 경고가 나오지 않음.