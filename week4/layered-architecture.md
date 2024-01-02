# Layered Architecture

## Contents

### 개요

Layer Architecture은 아키텍쳐 스타일이다.

아키텍쳐 스타일의 정의를 생각해보자. 아키텍쳐 스타일은 소프트웨어 시스템을 설계하고 구축할 때 사용되는 `일련의 설계 원칙과 규칙의 집합`이다. 이런 설계 원칙과 규칙을 정의하면 뭐가 좋을까? 반복되는 패턴을 일반화해서 특정한 문제나 요구사항에 대한 효과적인 솔루션을 제공할 수 있다. 가장 직관적으로 떠오르는 개념은 템플릿이다. 글을 쓸 때 템플릿이 있으면 비슷한 류의 글을 쓰는 양식을 통해 효과적으로 글을 쓸 수 있다.

다시 레이어드 아키텍쳐 스타일로 돌아오자. 레이어드 아키텍쳐 스타일을 이해하기 위해서 기본 개념? 전제가 필요하다. 바로 `관심사의 분리`다.

### Separation of Concerns

[관심사의 분리](https://ko.wikipedia.org/wiki/%EA%B4%80%EC%8B%AC%EC%82%AC_%EB%B6%84%EB%A6%AC)가 왜 필요할까? 커다란 프로그램은 유지보수가 어렵다. 인간의 한계와 공간의 한계를 생각해야한다. 인간은 동시에 아주 큰 것을 파악하고 조작하기 어렵다. 마찬가지로 아주 큰 것은 물리적으로나 한 공간에 두기 어렵다. 따라서 이런 한계를 극복하기 위해서 유지보수가 가능하도록 커다란 프로그램을 적절하게 나누고, 그룹화해야 한다. 파일 시스템의 폴더나 Java의 패키지 등이 관심사의 분리의 예시라고 볼 수 있다. 직관적으로 생각하면 같은 목적을 가지 것들끼리 묶어서 잘 정리하는 것이라고 생각하면 된다.

참고로 관심사의 개념이 굉장히 추상적인데 컴퓨터 프로그램 코드에 영향을 미치는 정보의 집합이라고 한다.

#### 응집도와 결합도

이 관심사의 분리에서 관련된 개념이 [응집도](<https://en.wikipedia.org/wiki/Cohesion_(computer_science)>)와 [결합도](<https://en.wikipedia.org/wiki/Coupling_(computer_programming)>)다. 일반적으로 소프트웨어에서 응집도가 높을 수록 결합도는 떨어지는 경향이 있다.

### Layer

관심사의 분리를 위해서 관심사를 나누는 기준이 필요하다. Layer을 기준으로 나누게 된다. 그래서 [다층 구조](https://ko.wikipedia.org/wiki/%EB%8B%A4%EC%B8%B5_%EA%B5%AC%EC%A1%B0)라고 부른다. 레이어드 아키텍쳐에서도 [여러 종류](https://github.com/ahastudio/til/blob/main/architecture/layered-architecture.md)가 있는데 이는 나중에 다루게 된다.

현재 우리는 웹에서 레이어드 아키텍쳐를 적용하는 것을 공부하고 있다. 결론부터 말하면 기능은 웹과 분리될 수 있다. 흔히 전자를 Business Logic이라고 부르고, 후자를 UI이라고 부른다.  
구현 관점에서 생각해보면 웹은 UI Layer에서 다루며 Spring Web MVC의 Controller로 구현된다. 반면 비즈니스 로직은 그 아래 계층에서 다룬다.
실습을 통해서 분리를 해보자

### UUID

실습을 하기 전 필요한 개념이 있다. 실습에서 Post(게시글)을 관리하기 위해서 [식별자](https://github.com/ahastudio/til/tree/main/identifier)를 사용해야한다. 기존에는 long id 값을 그냥 넣었는데, [UUID](https://ko.wikipedia.org/wiki/%EB%B2%94%EC%9A%A9_%EA%B3%A0%EC%9C%A0_%EC%8B%9D%EB%B3%84%EC%9E%90)라는 범용적인 방식을 사용한다. Java 언어에서 기본적으로 [UUID 클래스](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/UUID.html)를 제공하기 때문에 따로 라이브러리를 불러올 필요가 없이 사용할 수 있다.

### UUID로 Post 식별자 만들기 실습

우선 Post를 생성하는 메서드를 UUID를 활용해서 게시글 식별자로 사용할 수 있도록 해보자. replace함수는 UUID가 생성될 때 하이픈을 포함하는데 이게 보기 싫어서 바꿨다.

```<java>
	// Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody PostDto body) {

		// Post 추가 기능
        String id = UUID.randomUUID().toString().replace("-", "");
        body.setId(id);
        return body.getId() + body.getContent() + body.getTitle();
    }
```

httpie로 POST 요청을 보내보자

```<bash>
http POST localhost:8080/posts/ title="title" content="content"

HTTP/1.1 201
Connection: keep-alive
Content-Length: 44
Content-Type: application/json
Date: Tue, 02 Jan 2024 10:08:07 GMT
Keep-Alive: timeout=60

63f4158f38234b94bf40a6d0344b0d8fcontenttitle

```

문자열로 UUID + content + title 값이 잘 나왔다. 그러나 UUID는 문제점이 있다. 랜덤으로 생성되서 sorting하기 어렵다. 생성 순서로 정렬하게 만드는 방법이 필요하다. (실제로 Post를 여러 번 생성해보면 id가 순서가 없음을 알 수 있다.)

### ULID

[이 녀석](https://github.com/ulid/spec)은 Sortable하다. timestamp를 포함해서 계산하기 때문에 생성 순으로 id값을 구할 수 있다.​ 이를 활용하기 위해서 [ULID Creator](https://github.com/f4b6a3/ulid-creator)라는 라이브러리를 사용하면 된다.

### ULID 실습

build.gradle에 의존성을 추가하자. 그리고 코드를 수정하자.

```<gradle>
implementation 'com.github.f4b6a3:ulid-creator:5.1.0'
```

​

```<java>
	// Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody PostDto body) {

        // Post 추가 기능
        String id = UlidCreator.getUlid().toString();
        body.setId(id);
        return body.getId() + body.getContent() + body.getTitle();
    }
```

이제 Create를 여러번 하고 Post List를 불러와서 생성 순서대로 Id값이 형성됬는지 확인하자.

```<bash>
http GET localhost:8080/posts

[{"id":"01HK4V59BCZ5REQQQDT0Q5HSKF","title":"title","content":"content"},{"id":"01HK4V5B2VF1YTVVVMBGV9TCMB","title":"title","content":"content"},{"id":"01HK4V5CBJAHKVNJ92WTHE0W8W","title":"title","content":"content"},{"id":"01HK4V5E8CR939207ZXM5H7V00","title":"title","content":"content"}]

```

Id 값 앞자리가 비슷하죠? 이게 생성 순서대로 값이 증가합니다.

### TSID

위에서 사용한 라이브러리와 동일한 개발자가 ULID를 개선했습니다. 그래서 요즘은 TSID만 업데이트 되네요.
마찬가지로 build.gradle에 의존성 추가해서 사용하면 됩니다.

```<gradle>
implementation 'com.github.f4b6a3:tsid-creator:5.2.0'
```

```<java>
String id = TsidCreator.getTsid().toString();

```

### 이어서 옛날(?) 방식으로 리팩토링 해보자!

루트 디렉토리에 services 패키지를 만들고 PostService 클래스를 구현해보자.

//TODO(Optional 정리)

```<java>
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService = new PostService();

    // Read
    @GetMapping("")
    public List<PostDto> list() {
        return postService.list();
    }

    @GetMapping("/{id}")
    public PostDto detail(
            @PathVariable("id") String id
    ) {
        return this.postService.detail(id);
    }

    // Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto create(@RequestBody PostDto body) {
        return this.postService.create(body);
    }

    // Update

    @PatchMapping("/{id}")
    public PostDto update(@PathVariable("id") String id, @RequestBody PostDto body) {

        return this.postService.updatePost(id, body);
    }
}
```

```<java>
public class PostService {

    private final List<PostDto> postDtoList = new ArrayList<>();

    public List<PostDto> list() {
        return this.postDtoList;
    }

    public PostDto detail(String id) {
        return postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }

    public PostDto create(PostDto body) {
        // Post 추가 기능
        String id = UlidCreator.getUlid().toString();
        body.setId(id);
        this.postDtoList.add(body);

        return body;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }
}
```

자 여기서 잠깐 PostService의 updatePost메서드와 detail메서드에 중복된 코드가 보인다. 사실 update를 하기 위해서 id로 찾는 과정이 포함되어있다.

```<java>
public class PostService {

    private final List<PostDto> postDtoList = new ArrayList<>();

    public List<PostDto> list() {
        return this.postDtoList;
    }

    public PostDto detail(String id) {
        return findPost(id);
    }

    public PostDto create(PostDto body) {
        // Post 추가 기능
        String id = UlidCreator.getUlid().toString();
        body.setId(id);
        this.postDtoList.add(body);

        return body;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = findPost(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    private PostDto findPost(String id) {
        return postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }
}
```

짜잔 이렇게 공통 메서드 findPost로 묶어내었다! 이것이 리펙토링이다.

이어서 삭제하는 메서드까지 구현해보자.

```<java>
public class PostService {

    private final List<PostDto> postDtoList = new ArrayList<>();

    public List<PostDto> list() {
        return this.postDtoList;
    }

    public PostDto detail(String id) {
        return findPost(id);
    }

    public PostDto create(PostDto body) {
        // Post 추가 기능
        String id = UlidCreator.getUlid().toString();
        body.setId(id);
        this.postDtoList.add(body);

        return body;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = findPost(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        PostDto foundPost = findPost(id);
        this.postDtoList.remove(foundPost);
    }

    private PostDto findPost(String id) {
        return postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }
}
```

```<java>
public class PostService {

    private final List<PostDto> postDtoList = new ArrayList<>();

    public List<PostDto> list() {
        return this.postDtoList;
    }

    public PostDto detail(String id) {
        return findPost(id);
    }

    public PostDto create(PostDto body) {
        // Post 추가 기능
        String id = UlidCreator.getUlid().toString();
        body.setId(id);
        this.postDtoList.add(body);

        return body;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = findPost(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        PostDto foundPost = findPost(id);
        this.postDtoList.remove(foundPost);
    }

    private PostDto findPost(String id) {
        return postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }
}
```

이제 PostService 클래스가 기능(business logic)에 관심사를 두게 되었다. Controller에서 기능에 대한 관심사는 사라졌죠? Controller는 웹에만 관심을 두고있습니다. 물론 아직 살짝 부족하긴 한데.. 서비스에서 사용하는 DTO가 웹과 관련이 있어서.. 이 처리는 나중에 하겠습니다.

### Service

- [Presentation Domain Data Layering](https://martinfowler.com/bliki/PresentationDomainDataLayering.html)

각 기능을 개별 메서드로 분리하고, 이를 모아서 PostService 클래스로 모았는데, 패키지 이름을 application으로 변경해주자. layer의 이름을 반영했다.

이렇게 설계를 개선하는 것이 리팩토링이다!

## 참고 자료

### Refactoring

> [Extract Method](https://refactoring.com/catalog/extractFunction.html)

> [Extract Class](https://refactoring.com/catalog/extractClass.html)
