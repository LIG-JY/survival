# Domain Model

## contents

### 도메인 주도 설계

> [도메인 주도 설계 - Layered Architecture](https://wikibook.co.kr/article/layered-architecture/)

도메인을 이해하기 위해서는 application layer와 domain layer을 어떻게 구분하는지 알아야한다.

### Application Layer

Application Layer의 특징을 알아보자.

- 이 계층은 얇게 유지된다.
  - 즉 역할(관심사)를 도메인과 분리한다.
- 업무 규칙이나 지식이 포함되지 않는다.
  - 비즈니스 로직에서 도메인 지식이라고 불리는 영역은 모두 제외된다.
- 오직 작업을 조정하고 도메인 객체의 협력자에게 작업을 위임한다.
  - 직접 작업을 하지 않교 작업을 위임하고 조정하는 역할만 한다.

### 도메인 모델

우선 도메인 모델의 정의를 확인해보자.

#### Domain Model의 정의

[Domain Model](https://martinfowler.com/eaaCatalog/domainModel.html)

> An object model of the domain that incorporates both behavior and data.

정의를 확인해보니 도메인은 일반적인 객체랑 정의가 같다.

#### 도메인은 행위를 강조

도메인은 행위(behavior)를 강조한다. 행위를 예시를 통해 이해 해보자.

- 행위 없이 코딩한다는 것은 `내가 너를 다 파악해서 조작하겠다`라는 의미다. 따라서 협력에 매우 불편하다. 결합도가 높고 복잡하다.

```java
// amount 정보를 파악한다.
Long amount = account.getAmount();
// 조작한다.
account.setAmount(amount + 10_000);
```

- 행위가 있는 코딩은 `내가 알게 뭐야 너 알아서 해`라는 의미다. 즉 위임하는 것이다. 결합도가 낮고 협력에 유리하다.

```java
account.increaseAmount(10_000);
```

#### Unit Test로 행위를 검증

위임을 해서 잘 협력이 되려면 알지 못하는 도메인의 행위가 올바른지 확인할 수 있어야한다. 이 때 필요한 것이 `Unit Testing`이다.

위의 예로 들면 increaseAmount라는 행위가 의도대로 amount를 잘 증가시키는지 테스트하는 것이죠?

### Repository

#### Repositorty의 정의

> [Repository](https://martinfowler.com/eaaCatalog/repository.html)

정의를 봐서는 잘 모르겠다. dao와의 차이점을 통해 알아보자.

#### DAO vs Repository

DAO가 데이터를 관리한다면, Repository는 도메인 모델을 관리한다. rough하게 말하면 전자는 DB중심이고, 후자는 도메인 모델 중심이다.

#### 데이터베이스 주도 개발

예전 기술을 사용하는 조직의 경우 ERD를 먼저 그리고(table, field, schema..등 결정) 그리고 나서 DAO와 VO(DTO)를 사용하는 방식으로 개발한다. 즉 데이터베이스 주도 개발이다. 이렇게 만들면 도메인 모델이 사실상 없다고 볼 수 있다. 따라서 다음과 같은 특징을 가진다.

- 도메인 레이어가 어플리케이션 레이어와 구분되지 않는다.

- dao는 set,get만 할 뿐 행위라고 부를만한 것은 하지 않는다.

- DB가 바뀌면 프로그램이 바뀌게 된다.

#### DDD

DDD를 따르는 조직은 도메인을 먼저 만들고, JPA Entity와 도메인 모델을 따로 만들어서 매핑하다.

프로그램(도메인)이 변하면 이를 지원하기 위해 DB를 조정한다. CQRS, EventSourcing, 솔루션 변경(Mysql -> MongoDB)이 **상대적**으로 쉽다.

#### 무기력한 도메인(Anemic) 에서 풍부한 도메인으로(Rich)

[무기력한 도메인 모델](https://martinfowler.com/bliki/AnemicDomainModel.html)은 안티패턴이다. 하지만 비즈니스 모델이 복잡하지 않은 상황 즉 CURD 중심이면 DB 주도 개발(무기력한 도메인)이 코드량이 적기 때문에 편리하다. 굳이? 라는 생각이든다.

그래서 처음에는 무기력한 도메인에서 시스템이 복잡해지면 점진적으로 풍부한 도메인으로 전환하는게 일반적이다. [유명한 그래프](https://dev.to/crovitz/have-you-anemic-or-rich-domain-model-2ala)를 살펴보자.

### 실습

#### 1. 도메인 모델 패키지 만들기

models라는 패키지를 만들자. domain으로 해도 상관없다. 벌써 코드량이 늘어나는 게 보이죠? DDD의 단점

#### 2. 도메인 모델 구현

Post라는 클래스를 만들자. 아직까지는 DTO와 별 다를게 없다. 이 Post를 풍성하게 하면서 DTO와 차별점을 만들어보자.

```<java>
public class Post {
    private long id;
    private String title;
    private String content;

    public Post(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
```

##### id 멤버 변수 구체화

PostId라는 클래스를 만들어서 id 멤버 변수를 구체화해보자.

```<java>
public class PostId {

    private String id;

    public PostId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostId postId)) return false;

        return Objects.equals(id, postId.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
```

##### content 멤버 변수 구체화

content 멤버 변수를 구체화하는 MultilineText 클래스를 만들어보자.

```<java>
public class MultilineText {

    private final List<String> lines;

    public MultilineText(String text) {
        this.lines = List.of(text.split("\n"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultilineText that)) return false;

        return lines.equals(that.lines);
    }

    @Override
    public int hashCode() {
        return lines.hashCode();
    }
}
```

##### unit test

이제 MultilineText가 잘 생성자의 함수 인자로 들어온 문자열을 잘 split해서 생성되는지 `unit test` 해보자.

split한 문자열을 다시 toString 메서드로 합쳐서 잘 생성되는지 확인하자.

intellij에서 `command + shift + t` 하면 테스트를 생성해준다.

```<java>
class MultilineTextTest {

    @Test
    void creation() {
        MultilineText multilineText = new MultilineText("Hello\nWorld");

        assertEquals("Hello\nWorld", multilineText.toString());
    }

}
```

###### assertEquals

assertEquals 메서드에는 첫번째 함수 인자로 기대하는 값, 두번째 함수 인자로 테스트할 대상을 넣는다.

즉 multiLineText 객체가 생성되면서 헬로 월드를 split해서 List로 저장하다가, toString()을 호출해서 다시 합쳐서 기대값 "Hello\nWorld"와 같은지 확인하는 것이다.

###### toString() 오버라이딩

```<java>
public class MultilineText {

    private final List<String> lines;

    public MultilineText(String text) {
        this.lines = List.of(text.split("\n"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultilineText that)) return false;

        return lines.equals(that.lines);
    }

    @Override
    public int hashCode() {
        return lines.hashCode();
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
```

그래서 toString()을 오버라이딩해서 구현하자. 테스트 하면 잘 통과한다.
이제 post는 풍성한 도메인(?)이 되었다.

```<java>
public class Post {
    private PostId id;
    private String title;
    private MultilineText content;

    public Post(PostId id, String title, MultilineText content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
```

#### 3. repositories 패키지 만들기

이제 도메인 레이어와 애플리케이션 레이어를 분리하기 위해 PostService에서 관심사의 분리를 적용하자. 현재 코드에 DAO로 데이터를 다루고 있기 때문에 도메인 모델이라고 할 수 없다.

repositories 패키지를 만들고 repository 클래스를 구현해보자.

#### 4. PostRepository 구현과 PostService 수정

도메인 모델인 Post를 관리하는 PostRepository를 구현해보자.

##### 정적 펙토리 메서드

그 전에 `정적 펙토리 메서드`를 PostId, MultiLineText 클래스에 구현해서 깔끔하게 사용해보자. 아래와 같이 public 접근 제어자, static, 반환형 클래스는 자기 자신, 생성자의 인자와 똑같은 함수 인자를 구성하면 된다.

```<java>
public static MultilineText of(String text) {
        return new MultilineText(text);
    }
```

##### findAll 구현과 PostService 수정

이제 Repository의 메서드를 구현하면 된다. 우선 findAll만 구현하고 PostService에 적용해보자.

```<java>
public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
```

##### PostDTO 수정

그리고 우선 Post를 PostDTO로 변환하는 과정을 거치기 위해서 PostDto 클래스를 수정하자!

```<java>
// Domain -> DTO
    public PostDto(Post post) {
        this(post.id().toString(), post.title(), post.content().toString());
    }
```

도메인을 함수 인자로 받아 DTO 자기 자신을 생성해 리턴하는 생성자를 추가하자! 생성자의 함수 인자에 id, content의 경우 타입이 String이 아니라 (PostId, MultilineText) toString() 메서드를 통해 변환이 필요하다.

참고로 DDD에서는 도메인의 getter대신 멤버 변수 이름과 메서드 이름을 동일하게 해서 getter처럼 사용한다. 도메인에 멤버 변수를 리턴하는 함수를 만들자.

```<java>
public class Post {
    private PostId id;
    private String title;
    private MultilineText content;

    public Post(PostId id, String title, MultilineText content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }


    // getter
    public PostId id() {
        return id;
    }

    public String title() {
        return title;
    }

    public MultilineText content() {
        return content;
    }
}
```

##### PostService 수정

PostService의 list() 메서드를 수정하자.

```<java>
public List<PostDto> list() {
        // domain repository에서 도메인을 가져온다.
        List<Post> posts = this.postRepository.findAll();

        // 가져온 도메인을 DTO로 변환한다.
        return posts.stream()
                .map(PostDto::new)
                .toList();
    }
```

##### find 구현과 PostService 수정

```<java>

>> PostRepository

public Post find(PostId id) {
        return posts.get(id);
    }
```

```<java>

>> PostService

public PostDto detail(String id) {

        // 도메인 찾기
        Post found = this.postRepository.find(PostId.of(id));

        // 도메인을 DTO로 변환한다.
        return new PostDto(found);
    }
```

##### PostService의 createPost 수정

책임을 확실하게 분산해보자.

```<java>
>> PostService

public PostDto create(PostDto body) {

        // 도메인 생성
        Post newPost = new Post(
                body.getTitle(),
                MultilineText.of(body.getContent())
        );

        // 도메인 저장
        this.postRepository.save(newPost);

        // 도메인을 DTO로 변환한다.
        return new PostDto(newPost);
    }
```

우선 PostService에서 도메인 생성, 저장 그리고 DTO로 변환을 하는 메서드 create를 구현했다. 진짜 이 service 클래스 즉 애플리케이션 레이어는 데이터의 조정만 관여하게 되었다. 저장하는 작업은 postRepository로 위임했습니다. 물론 아직 더 위임할 여지가 있는데 여기까지

```<java>
public class Post {
    private PostId id;
    private String title;
    private MultilineText content;

    public Post(PostId id, String title, MultilineText content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Post(String title, MultilineText content) {
        this.id = PostId.generate();
        this.title = title;
        this.content = content;
    }

    // getter
    public PostId id() {
        return id;
    }

    public String title() {
        return title;
    }

    public MultilineText content() {
        return content;
    }
}
```

도메인 Post 클래스를 수정했다. 생성자를 추가했는데 여기서 풍부한 도메인의 장점을 확인할 수 있다. 만약에 생성자의 인자로 들어오는 id ,title, content가 모두 String이라고 생각해보자. 이를 사용하는 곳에서 헷갈릴 수 있다. 명확하게 구분이 안 가고, 테스트도 어렵다. 하지만 지금처럼 PostId, MultilineText로 확실하게 다른 자료형으로 구분하니까 가독성이 좋아졌다.

새로 만든 생성자에서는 Id를 생성자의 인자로 받지 않는다. 대신 PostId 클래스의 정적 메서드 generate()를 통해 생성한다. 이것도 권한의 위임이다. Id 생성 작업을 PostId 클래스로 위임했다!

```<java>
public static PostId generate() {
        return new PostId(TsidCreator.getTsid().toString());
    }
```

그리고 PostRepository에서 save는 아래와 같이 구현했다.

```<java>
public void save(Post newPost) {
        this.posts.put(newPost.id(), newPost);
    }
```

참고로 지금 PostRepository에 자료구조를 Map으로 쓰고 있는데 이 때 Key의 중복을 막기 위해서 PostId 클래스에 hashCode(), equals()메서드를 구현해야한다. 이미 구현해서 다행히 별 문제가 없다.

##### PostService의 updatePost수정

```<java>
public PostDto updatePost(String id, PostDto body) {

        // 도메인을 찾는다.
        Post foundPost = this.postRepository.find(PostId.of(id));

        // 도메인을 수정한다.
        foundPost.update(
                body.getTitle(),
                MultilineText.of(body.getContent())
        );

		// DTO로 변환해서 리턴한다.
        return new PostDto(foundPost);
    }
```

수정하는 행동을 도메인으로 위임했다.

```<java>
public void update(String title, MultilineText content) {
        this.title = title;
        this.content = content;
    }
```

도메인 Post 클래스의 update가 setter와 유사하긴 한데.. 일단 이렇게 구현했다. 참고로 DDD에서는 최대한 getter을 안 써야된다. getter을 사용했다면 일단 뭔가 이상하다고 느껴야한다.

##### PostService의 deletePost수정

```<java>
public void deletePost(String id) {

        // 도메인을 찾는다.
        Post foundPost = this.postRepository.find(PostId.of(id));

        // 도메인을 삭제한다.
        this.postRepository.delete(foundPost);
    }
```

```<java>
public void delete(Post post) {
        this.posts.remove(post.id());
    }
```

논리는 매우 유사하다. 삭제 메서드가 좀 이상하긴 한데... 일단 이렇게 합시다.

## 참고

- [오브젝트 (강력 추천)](https://wikibook.co.kr/object/)

- 마켓컬리 사례

  1. [Database Driven Development에서 진짜 DDD로의 선회 -1-](https://helloworld.kurly.com/blog/road-to-ddd/)

  2. [Database Driven Development에서 진짜 DDD로의 선회, 이벤트 스토밍 -2-](https://helloworld.kurly.com/blog/event-storming/)
