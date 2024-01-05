# Serialization

## Contents

### 직렬화

#### 왜 직렬화가 필요한가요?

객체를 그 자체로 DB에 저장하거나 네트워크로 전송하는 건 불가능하기 때문이다.

결국 객체를 전송하기 위해서는 변형(데이터화)이 필요하다. 그리고 전송 후 원래의 상태로 복구할 수 있어야 한다.

#### Java에서 직렬화

- Java에서는 모든 데이터를 전송할 때 Byte Stream으로 데이터화해서 전송하게 된다.

- 이 때 기계가 파싱할 수 있고 사람도 읽을 수 있는 `형태`로 데이터화하는 과정도 포함된다. 대표적으로 XML, JSON, YAML 같은 형태가 인기가 많다.

- 데이터를 전송 받는 곳에서는 Json 형식을 바탕으로 데이터를 `복사(재구성)`해서 사용하게 된다.

- **직렬화**와 **마샬링**은 거의 같지만, Java에선 마샬링을 특수하게 다룬다.

  - 직렬화(Serialization): 역직렬화(Deserialization)를 통해 객체 또는 데이터의 복사본을 만들 수 있다.

  - 마샬링(Marshalling): 직렬화와 같거나, 원격 객체(remote)로 복원할 수 있다. 원격 객체의 경우 메서드 호출은 RPC(또는 RMI)가 된다. 마샬링이 더 넓은 개념이라고 생각하면 됩니다!!

### JSON (JavaScript Object Notation)

- JavaScript Good Parts로 유명한 Douglas Crockford가 만든 `문자 기반`의 표준 데이터 포맷. 사람이 읽기 쉽고, 기계도 해석 또는 생성하기 쉽다.

- JSON은 `문자열 형태`로 존재한다. 따라서 네트워크를 통해 전송할 때 편하긴 하지만 실제로 데이터에 접근하기 위해서는 네이티브 JSON 객체로 변환될 필요가 있다.

#### JSON 객체

- Javascript는 JSON 전역 객체를 통해 문자열과 JSON 객체의 상호 변환을 지원한다. 그래서 메서드 이름이 parse, stringify이다.

- JSON.parse로 파싱된 문자열 형태의 Json은 객체가되어 .이나 []을 통해 객체에 접근할 수 있게 된다.

- JavaScript의 object는 기본적으로 key-value 쌍이다.

- 심지어 Array도 제한된 key-value + length 관리에 불과하다. 원래 개념의 배열에는 데이터가 순서대로 메모리에 들어가 있어야하지만 js에서는 그렇지 않다. 인덱스가 Key 해당 인덱스의 값이 Value로 쌍이고 길이라는 속성이 원래의 배열의 길이와 개념이 다르다.

#### Java Map과 Json

Java는 Map이 key-value 쌍의 객체이며 Json을 위해서 Map을 직렬화해서 쓸 수도 있다. 하지만 `스키마 관리` 및 `타입 안전성`을 위해 DTO를 활용한다. 아래 예시를 살펴보자.

```<java>
// DTO 클래스 정의
public class PersonDTO {
    private String name;
    private int age;

    // 생성자, getter, setter 생략
}

// DTO를 사용하는 코드
public class Main {
    public static void main(String[] args) {
        // DTO 인스턴스 생성
        PersonDTO person = new PersonDTO();
        person.setName("John Doe");
        person.setAge(30);

        // DTO를 JSON으로 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(person);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

```

```<java>
import com.fasterxml.jackson.databind.ObjectMapper;

// Map을 사용하는 코드
public class Main {
    public static void main(String[] args) {
        // Map 사용
        Map<String, Object> personMap = new HashMap<>();
        personMap.put("name", "John Doe");
        personMap.put("age", 30);

        // Map을 JSON으로 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(personMap);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
```

Map을 Json 문자열 직렬화하면 다음과 같은 문제가 발생한다.

- Value에 자료형으로 Object를 사용하게 된다. 따라서 명시적으로 데이터 스키마를 알기 어렵다. 반면 DTO에서는 메서드 이름부터 정확히 필드를 알 수 있죠?

- Value에 잘못된 타입 인한 `런타임 오류`가 발생할 수 있다. 예를 들어 age에 문자열을 넣으면 Value가 Object타입 이라 `컴파일 오류`는 발생하지 않지만, JsonProcessing(직렬화) 과정에서 `런타임 오류`가 발생한다.

#### 구체적으로 Java 세계에서 어떻게 JSON 문자열로 변환하나요? 직렬화 과정에 대해서 설명해주세요

- 생성: DTO (Java 세계) → 변환기 → JSON 문자열
- 해석: JSON 문자열 → 변환기 → DTO (Java 세계)

우리는 변환기의 정체를 알아야합니다! Java에선 [Jackson](https://github.com/FasterXML/jackson)이란 도구가 유명하고, Spring Boot에서 Web 의존성을 추가하면 바로 사용할 수 있습니다.(즉, 우리는 딱히 아무 것도 안 해도 된다!)

### DTO class

#### 광의의 DTO

- DTO는 여러 곳에서 사용될 수 있고, 그 의미는 계속 확대된다. 예를 들어 Tier, 즉 Remote 통신이 아닌 상황인 로컬의 Layer 사이나 내부 객체를 감춘 공개 인터페이스를 만들 때도 DTO를 활용. “데이터 전송”이기만 DTO를 사용한다고 해도 딱히 잘못된 것이 아니다.

#### 협의의 DTO

- B/E와 F/E 사이 통신을 위한 JSON 스키마로서의 DTO.

#### DTO 용어의 Context

- DTO를 쓰는 다양한 상황을 상상해 보자. 이는 “DTO 변환을 어디에서(객체) 해야 하나요?” 같은 질문과 연결된다.

  - 예를들어 Json 스키마로써 DTO는 웹에서 쓰인다고 하면 컨트롤러에서 변환해주는 것이 맞다.

  - 즉 결론은 `광의의 DTO`는 어디서 사용되냐에 따라 어디서 하는지 문제가 된다. 결론적으로 B/E 와 F/E 사이의 `JSON 스키마로써 DTO`를 사용한다면 보통 컨트롤러 패키지에서 변환할 것이다!

### 요약

1. Java에서 DTO 클래스로 객체를 만든다.
2. 이 객체를 직렬화한다.
3. Jackson의 도움을 받아 직렬화 하게 되고 이 때의 형태는 Json이다.

### 참고

[JSON](https://en.wikipedia.org/wiki/JSON)

[JSON 개요](https://www.json.org/json-ko.html)

[JSON으로 작업하기](https://developer.mozilla.org/ko/docs/Learn/JavaScript/Objects/JSON)

[직렬화](https://ko.wikipedia.org/wiki/직렬화)

[마샬링(컴퓨터 과학)](<https://ko.wikipedia.org/wiki/마샬링_(컴퓨터_과학)>)
