# Jackson ObjectMapper

## Content

### JOM

Jackson ObjectMapper를 사용해서 DTO를 JSON(문자열)으로 변환하거나, JSON(문자열)을 DTO로 변환할 수 있다. 순서대로 알아보자.

1.먼저, DTO를 위한 class를 만든다.

- JSON의 스키마를 작성한다는 느낌으로 만들면 된다.
- Json 문자열의 프로퍼티는 getter 메서드의 이름을 따른다는 점에 주의하자.
- 만약 프로퍼티 이름을 강제하고 싶다면 @JsonProperty 애노테이션을 사용하자.

```<java>
package com.gyo.api.rest.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDto {

    private String id;

    private String title;

    private String content;

    // Java Beans 규약을 따르는 클래스는 기본 생성자가 필요하다.
    public PostDto() {
    }

    public PostDto(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // DTO 특징 : 모든 필드에 대해서 Getter를 제공한다.
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    // JsonProperty 어노테이션을 사용하면 JSON으로 변환할 때 특정 필드(key)의 이름을 변경할 수 있다.
    @JsonProperty("다른 이름")
    public String getContent() {
        return content;
    }
}

```

2.Spring DI를 통해 컨트롤러에서 Jackson ObjectMapper를 얻는다. 스프링 프레임워크(DI container)은 등록된 객체(Bean)를 관리하고 있고, 생성자에 명시하면 받아서 사용할 수 있다.

- 사용=의존성/의존관계를 주입 받는 것을 말함(Dependency Injection).

Jackson ObjectMapper를 써서 DTO를 JSON 포맷의 String으로 변환한다. JsonProcessingException 예외가 발생할 수 있는데, 여기서는 간단히 JacksonException(상위 예외임) 예외를 사용한다.

```<java>
@RestController
@RequestMapping("/posts")
public class PostController {

    // final 키워드는 변수를 상수화 시킨다. 따라서 선언과 동시에 초기화가 필요하기 때문에 생성자로 초기화한다.
    private final ObjectMapper objectMapper;

    // final 키워드를 사용했기에 생성자는 필수다!
    public PostController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    // Read
    @GetMapping("/")
    public String list() {
        return "게시물 목록";
    }

    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") String id
    ) throws JacksonException {
        PostDto postDto = new PostDto(id, "제목", "Test Content");

        // DTO to String
        return objectMapper.writeValueAsString(postDto);
    }
}
```

3.이렇게 만들고 curl 요청을 해보자

```<bash>
curl localhost:8080/posts/5
{"id":"5","title":"제목","다른 이름":"Test Content"}%
```

DTO에 getter의 이름에 따라 프로퍼티(key)가 결정되었다. getContent 메서드에 JsonPropery 어노테이션이 붙어있어서 프로 퍼티가 변경된 것을 확인하자.

4.여기서 의문점은 컨트롤러에 objectMapper을 의존성을 주입받았다면 objectMapper가 속한 Jackson 라이브러리를 이미 스프링 부트가 포함하고 있다는 뜻이다. `build.gradle`을 확인해보자 의존성에 포함된 spring-boot-starter-web에 Jackson이 포함되어있다. 신기하게도 spring boot에서 굳이 컨트롤러에 objectMapper 의존성을 주입 안 해도 DTO를 알아서 변환해주는 기능을 제공한다.

```<kotlin>
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

```<java>
@GetMapping("/{id}")
    public PostDto detail(
            @PathVariable("id") String id
    ) {
        // Return DTO
        return new PostDto(id, "제목", "Test Content");
    }
```

그래서 저렇게 메서드를 작성해도 ResponseBody에 Json String으로 반환된다.  
아래와 같이 List<DTO>형태로 반환해도 알아서 Json String으로 잘 변환해준다.

```<java>
// Read
    @GetMapping("/")
    public List<PostDto> list() {
        return List.of(
                new PostDto("1", "제목1", "내용1"),
                new PostDto("2", "제목2", "내용2"),
                new PostDto("3", "제목3", "내용3")
        );
    }
```

curl, httpie 요청을 해보았다.

```<bash>
>>> curl
 $ curl localhost:8080/posts/
[{"id":"1","title":"제목1","다른 이름":"내용1"},{"id":"2","title":"제목2","다른 이름":"내용2"},{"id":"3","title":"제목3","다른 이름":"내용3"}]%

>>> httpie
 $ http GET localhost:8080/posts/
HTTP/1.1 200
Connection: keep-alive
Content-Type: application/json
Date: Fri, 29 Dec 2023 15:21:04 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

[
    {
        "id": "1",
        "title": "제목1",
        "다른 이름": "내용1"
    },
    {
        "id": "2",
        "title": "제목2",
        "다른 이름": "내용2"
    },
    {
        "id": "3",
        "title": "제목3",
        "다른 이름": "내용3"
    }
]

```

5.objectMapper의 readValue로 String을 DTO로 변환해보자. 아까 objectMapper 의존성을 지웠다면 다시 추가해야한다.

```<java>
@RestController
@RequestMapping("/posts")
public class PostController {

    private final ObjectMapper objectMapper;

    public PostController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Read
    public List<PostDto> list() {
        return List.of(
                new PostDto("1", "제목1", "내용1"),
                new PostDto("2", "제목2", "내용2"),
                new PostDto("3", "제목3", "내용3")
        );
    }

    @GetMapping("/{id}")
    public PostDto detail(
            @PathVariable("id") String id
    ) {
        // Return DTO
        return new PostDto(id, "제목", "Test Content");
    }

    // Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody String body) throws JacksonException {
        PostDto postDto = objectMapper.readValue(body, PostDto.class);
        return postDto.getId() + postDto.getTitle() + postDto.getContent();
    }
}
```

이제 POST 요청을 해보자

```<bash>
>>> httpie
 http POST localhost:8080/posts/ id=3 title=gg "다른 이름"=ㅎㅎ
HTTP/1.1 201
Connection: keep-alive
Content-Length: 9
Content-Type: application/json
Date: Fri, 29 Dec 2023 16:05:28 GMT
Keep-Alive: timeout=60

3ggㅎㅎ

```

지금 PostDTO에 getContent 메서드에 @JsonProperty("다른 이름") 이렇게 어노테이션이 있어서 요청 시 Json의 프로퍼티에 이와 일치하는 "다른 이름"이라는 프로퍼티를 넣어주어야한다.

무심코 content로 프로퍼티 명을 지정하면 null이 나온다.

```<bash>
http POST localhost:8080/posts/ id=3 title=gg content=ㅎㅎ
HTTP/1.1 201
Connection: keep-alive
Content-Length: 7
Content-Type: application/json
Date: Fri, 29 Dec 2023 16:06:42 GMT
Keep-Alive: timeout=60

3ggnull

```

마찬가지로 curl 요청도 동일하다.

```
 curl -X POST localhost:8080/posts/ -d '{"title":"제목"}' -H 'Content-Type: application/json'
null제목null%
```

title 이외의 다른 프로퍼티는 요청에 포함하지 않아서 null이 나왔다.

POST API 메서드도 spring-boot-starter-web에 내장된 기능으로 HTTP 요청 시 body를 내부적으로 DTO로 변환해주는 기능을 활용할 수 있다.

```<java>
// Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody PostDto body) {
        return body.getId() + body.getContent() + body.getTitle();
    }
```

메서드 함수 인자의 자료형이 PostDto로 바뀐 것을 확인하자.

CRUD 모두 이런 식으로 DTO를 활용할 수 있다. 이때 적절한 요청 또는 응답을 처리하도록 `DTO를 세분화`해도 좋다. 예를 들어, Create(POST 요청)에선 id를 직접 넘겨주지 않는다는 걸 명확히 드러내기 위해 id가 없는 DTO를 따로 만들어서 사용해도 된다.

### 참고 Jackson 활용법 정리

TODO(Jackson 활용법 정리)

https://www.baeldung.com/jackson
