# Spirng Test

## Content

### Integration Test

Spring 공식 문서는 테스트를 크게 둘로 나눠서 설명한다.

- Unit Testing
- Integration Testing

어떤 프레임워크에서 제공하는 클래스가 있고 이것을 상속받아 사용하는 경우가 이전에는 많았었지만 Spring은 그렇지 않다. XML과 같이 외부 설정(Config)를 통해 의존성 주입을 받는다. 따라서 개발자는 비즈니스 로직에 집중할 수 있다.

> Spring은 코드에 구조적으로 개입하는 게 적어서, 단위 테스트를 쉽게 작성할 수 있다.

언제 Spring의 힘을 빌려서 테스트 할까? IoC 컨테이너를 적극적으로 활용하고 싶거나, Spring Web MVC로 구현된 부분을 테스트하고 싶을 때다. 또는 Spring Data JPA를 사용한다던가..? 이것의 공통점은 이 기능들이 Spring의 힘을 빌려서 작동한다는 것이다. 이런 기능을 테스트하기 위해서는 Spring의 힘을 빌려서 테스트해야 한다. 이런 테스트를 통합 테스트라고 부른다.

---

### Spring Boot Test

Spring Boot 1.4부터 @SpringBootTest Annotation을 써서 쉽게 테스트할 수 있다. [관련 링크](https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4)

### Spring Boot Test 실습 관련 일반적인 내용

PostController을 테스트 해보자.

#### @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

테스트의 환경을 설졍할 수 있다. 이 때 WebEnvironment Enum 값으로 어떤 웹 환경에서 테스트할 것인지 결정할 수 있다.

[Difference between webEnvironment = RANDOM_PORT and webEnvironment = MOCK](https://stackoverflow.com/questions/58364490/difference-between-webenvironment-random-port-and-webenvironment-mock)

이번 실습은 통합 테스트를 하기 위해 *RANDOM_PORT*로 지정한다. 통합 테스트는 최대한 프로덕션 환경과 유사할 수록 좋다고 합니다.

#### 테스트 코드 설정의 편의를 위해 필드 주입을 이용

```<Java>
@Value("${local.server.port}")
private int port;

@Autowired
private TestRestTemplate restTemplate;
```

#### @Value("${local.server.port}")

필드에 값을 대입해 쓸 때는 @Value를 이용하게 된다.

Spring의 프로퍼티(예: **application.properties 또는 application.yml 파일**)나 환경 변수에서 local.server.port 키에 해당하는 값을 찾는다. 그리고 이 값을 필드 변수에 할당하게 된다.

#### TestRestTemplate

객체의 경우 필드에 대입할 때 @Autowired을 활용하게 된다. TestRestTemplate 빈을 주입받는다. IoC 컨테이너가 이를 주입해준다.

테스트할 때만 의존성이 생기게 된다.

### 게시물 목록 조회 테스트

```<Java>
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostFeatureTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("게시물 목록을 조회한다.")
    void list() {
        String url = "http://localhost:" + port + "/posts";
        String body = restTemplate.getForObject(url, String.class);

        assertThat(body).contains("content1");
        assertThat(body).contains("이건 없지");
    }
}
```

```<bash>
Expecting actual:
  "[{"id":"1","title":"title1","content":"content1"}]"
to contain:
  "이건 없지"
```

- body의 값은 response body이다. 객체 배열을 나타내는 Json String이죠?
  이런식으로 asserThat 메소드에서 지원하는 문자열을 포함하는 지 확인하는 함수를 통해 assertion 하게 된다.

### 게시물 목록 생성 테스트

postForLocation 사용법만 알면 특별할 것은 없다. DTO를 postForLocation의 두번째 함수 인자로 넘겨줘야 하는 것을 주의하자.

DTO의 목적이 Web layer와 서버 간의 데이터 전송 용도임을 기억!

```<Java>
@Test
    @DisplayName("게시물을 생성하고 목록을 조회한다.")
    void createAndGetList() {
        String url = "http://localhost:" + port + "/posts";
        PostDto postDto = new PostDto("2", "새제목", "제곧네");
        URI uri = restTemplate.postForLocation(url, postDto);
        String body = restTemplate.getForObject(url, String.class);

        assertThat(body).contains("제곧네");
        assertThat(body).contains("새제목");
    }
```

### 게시물 목록 삭제 테스트

```<Java>
private String findLastId(String body) {
        Pattern pattern = Pattern.compile("\"id\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(body);

        String id = "";
        while (matcher.find()) {
            id = matcher.group(1);
        }
        return id;
    }
```

정규식을 처리하는 헬퍼 함수를 이용해 삭제 테스트를 해보자.

```<Java>
@Test
    @DisplayName("마지막 게시물을 삭제하고 목록을 조회해서 확인한다.")
    void delete() {
        String url = "http://localhost:" + port + "/posts";
        // 생성
        PostDto postDto = new PostDto("2", "새제목", "제곧네");
        URI uri = restTemplate.postForLocation(url, postDto);
        // 삭제
        String id = findLastId(restTemplate.getForObject(url, String.class));
        restTemplate.delete(url + "/" + id);
        // 목록 조회
        String body = restTemplate.getForObject(url, String.class);

        assertThat(body).doesNotContain("제곧네");
    }
```

목록 조회해서 받아온 문자열(문자열의 값은 Json 형태)에서 정규식을 이용해 마지막 Post의 Id를 찾고 이 Id로 다시 delete 요청을 테스트하는 흐름이다.

---

### MockMvc를 이용한 테스트

```<Java>
package com.gyo.api.rest.demo.controllers;

import com.gyo.api.rest.demo.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void list() throws Exception {
        this.mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("title1")
                ));
    }

    @Test
    public void create() throws Exception {
        String json = """
                {
                	"title": "새 글",
                	"content": "제곧내"
                }
                """;

        int oldSize = postRepository.findAll().size();

        this.mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        int newSize = postRepository.findAll().size();

        assertThat(newSize).isEqualTo(oldSize + 1);
    }

    // Repository에 기본으로 Post(PostId.of("1"), "title1", MultilineText.of("content1"))) 존재
    @Test
    public void deletePost() throws Exception {

        int oldSize = postRepository.findAll().size();

        String id = "1";
        this.mockMvc.perform(
                        delete("/posts/" + id))
                .andExpect(status().isOk());

        int newSize = postRepository.findAll().size();
        assertThat(newSize).isEqualTo(oldSize - 1);
    }
}
```

테스트 논리는 생성하고, 제거할 때 목록의 사이즈를 비교하는 것이다.

#### static import로 코드 가독성을 높일 수 있음

```<Java>
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
```

#### 참고 : 한글 인코딩 문제가 발생한다면

인코딩 문제를 해결하기 위해 application.properties 파일에 관련 설정 추가.

```<application.properties>
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.force=true
```

MockMvc를 사용하면서 불편한 점은 알아야할 가정이 많다는 것이다. 기존에 레포지토리에 뭐가 있는지 알아야하며, 각각의 id, content와 같은 속성의 값까지 알아야한다...

---

### SpyBean을 이용한 테스트

> Spies are stubs that also record some information based on how they were called.

Post 개수를 세는 방식, 즉 post 목록의 길이를 조회하는 방식이 아니라, 정말로 PostRepository의 save를 호출했는지만 확인해 보자.

SpyBean을 이용하면 이를 확인할 수 있다. spy는 호출될 때 기록을 남기기 때문이다.

```<Java>
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PostRepository postRepository;

    @Test
    public void list() throws Exception {
        this.mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("title1")
                ));

        Mockito.verify(postRepository).findAll(); // findAll이 호출되었는지 확인
    }

    @Test
    public void create() throws Exception {
        String json = """
                {
                	"title": "새 글",
                	"content": "제곧내"
                }
                """;

        this.mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        Mockito.verify(postRepository).save(any(Post.class)); // save가 호출되었는지 확인
    }

    // Repository에 기본으로 Post(PostId.of("1"), "title1", MultilineText.of("content1"))) 존재
    @Test
    public void deletePost() throws Exception {
        String id = "1";
        this.mockMvc.perform(
                        delete("/posts/" + id))
                .andExpect(status().isOk());

        Mockito.verify(postRepository).delete(any(Post.class)); // delete가 호출되었는지 확인
    }
}
```

verify를 통해서 SpyBean으로 주입받은 postRepository에서 save라는 메서드를 호출했는지 확인했다.  
save에는 Post 타입의 인자가 들어가서 호출되야 한다.

```<Java>
@Test
    public void create() throws Exception {
        String json = """
                {
                	"title": "새 글",
                	"content": "제곧내"
                }
                """;

        this.mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        Mockito.verify(postRepository).save(argThat(
                post -> {
                    return post.title().equals("새 글");
                }
        )); // save가 호출되었는지 확인
    }
```

argThat을 이용해서 인자로 들어오는 객체의 속성까지 확인할 수 있다.

## 참고

> [Testing](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)

> [Testing improvements in Spring Boot 1.4](https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4)

> [Testing the Web Layer](https://spring.io/guides/gs/testing-web/)

> [@Value](https://www.baeldung.com/spring-value-annotation)

> [SpyBean](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/SpyBean.html)

> [TestDouble](https://martinfowler.com/bliki/TestDouble.html)
