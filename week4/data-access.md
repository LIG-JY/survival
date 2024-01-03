# Data Access

## Contents

### 데이터를 관리하는 방법들

여러 데이터를 관리하는 방법은 하나가 아니다. 기존에 Service 코드에서는 List를 사용했지만, Map을 사용할 수도 있고, 다른 방식을 사용할 수도 있다. 따라서 지금 코드가 데이터를 관리하는 방식에 영향을 받고 있다.(의존) 데이터 관리라는 관심사를 분리해보자. 전통적인 3계층에서는 데이터 관리를 하는 레이어를 Data Layer라고 부른다.

우선 관심사의 분리를 실현하기 위해 기존의 ArrayList로 구현된 postDtoList를 DAO 클래스로 분리해보자. DAO 클래스는 앞으로 daos 패키지로 관리할 예정이다.

우선 서비스 클래스부터 수정해보자.

```<java>
public class PostService {

    private final PostDAO postDAO;

    public PostService() {
        this.postDAO = new PostDAO();
    }

    public List<PostDto> list() {
        return this.postDAO.findAll();
    }
}
```

이제 PostDAO 클래스를 구현해보자. 데이터르 관리하는 자료구조는 그대로 List를 사용한다.

```<java>
public class PostDAO {

    private final List<PostDto> postDtoList;

    public PostDAO() {
        this.postDtoList = new ArrayList<>();
        this.postDtoList.add(new PostDto("1", "title1", "content1"));
    }

    public List<PostDto> findAll() {
        return this.postDtoList;
    }
}
```

잠깐 여기서 주의할 점이 있다. 현재 DAO에서 내부의 List를 그대로 리턴하는데, 이 ArrayList는 Mutable하기 때문에 이를 그대로 리턴했다가 수정될 여지가 있다. 즉 캡슐화라는 객체 지향의 특징에 어긋나게 된다. 뭐 단일 책임 원칙이라고 생각해도 되겠죠? 결론은 읽기 전용으로 리스트를 복사해서 리턴하자!

findAll에서 읽기 전용으로 복사해서 리턴하고, 다른 메서드도 마저 구현하면 아래와 같다.

```<java>
public class PostService {

    private final PostDAO postDAO;

    public PostService() {
        this.postDAO = new PostDAO();
    }

    public List<PostDto> list() {
        return this.postDAO.findAll();
    }

    public PostDto detail(String id) {
        return this.postDAO.find(id);
    }

    public PostDto create(PostDto body) {

        // 원본 body를 수정하지 않는다.
        String id = UlidCreator.getUlid().toString();
        PostDto newPostDto = new PostDto(id, body.getTitle(), body.getContent());

        // DAO에 저장한다.
        this.postDAO.save(newPostDto);

        return newPostDto;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = this.postDAO.find(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        this.postDAO.delete(id);
    }
}
```

```<java>
public class PostDAO {

    private final List<PostDto> postDtoList;

    public PostDAO() {
        this.postDtoList = new ArrayList<>();
        this.postDtoList.add(new PostDto("1", "title1", "content1"));
    }

    public List<PostDto> findAll() {
        // 읽기 전용 반환
        return new ArrayList<>(this.postDtoList);
    }

    public PostDto find(String id) {
        return this.postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }

    public void save(PostDto postDto) {
        this.postDtoList.add(postDto);
    }

    public void delete(String id) {
        this.postDtoList.removeIf(postDto -> postDto.getId().equals(id));
    }
}
```

list 말고 다른 자료 구조인 map을 이용해서 구현해보자. 자료구조가 변했기 때문에 findAll 메서드에서 순서대로 나오지 않을 수 있으며, 하나만 찾는 find 메서드에서 null인지 확인하고 예외를 던지는 코드가 추가된다. 서비스 클래스에서는 생성자에서 초기화하는 필드만 postMapDAO로 변경했다.

```<java>
public class PostService {

    private final PostMapDAO postMapDAO;

    public PostService() {
        this.postMapDAO = new PostMapDAO();
    }

    public List<PostDto> list() {
        return this.postMapDAO.findAll();
    }

    public PostDto detail(String id) {
        return this.postMapDAO.find(id);
    }

    public PostDto create(PostDto body) {

        // 원본 body를 수정하지 않는다.
        String id = UlidCreator.getUlid().toString();
        PostDto newPostDto = new PostDto(id, body.getTitle(), body.getContent());

        // DAO에 저장한다.
        this.postMapDAO.save(newPostDto);

        return newPostDto;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = this.postMapDAO.find(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        this.postMapDAO.delete(id);
    }
}

```

```<java>
public class PostMapDAO {
    private final Map<String, PostDto> postDtoMap;

    public PostMapDAO() {
        this.postDtoMap = new HashMap<>();
        this.postDtoMap.put("1", new PostDto("1", "title1", "content1"));
    }

    public List<PostDto> findAll() {
        // ID 순으로 정렬되지 않는다. HashMap의 특징이다.
        return new ArrayList<>(this.postDtoMap.values());
    }

    public PostDto find(String id) {
        PostDto found = this.postDtoMap.get(id);
        // 못 찾을 경우 예외를 던진다.
        if (found == null) {
            throw new PostNotFoundException();
        }
        return found;
    }

    public void save(PostDto postDto) {
        this.postDtoMap.put(postDto.getId(), postDto);
    }

    public void delete(String id) {
        this.postDtoMap.remove(id);
    }
}
```

흠 코드의 중복이 많은데? 중복을 제거하기 위해서 PostDAO라는 인터페이스를 만들고, PostListDAO와 PostMapDAO로 클래스를 구현해보자.

```<java>
public class PostService {

    private final PostDAO postDAO;

    public PostService() {
        // 인터페이스만 알면 좋은데 구현도 알게되서 아쉽다.
        this.postDAO = new PostMapDAO();
    }

    public List<PostDto> list() {
        return this.postDAO.findAll();
    }

    public PostDto detail(String id) {
        return this.postDAO.find(id);
    }

    public PostDto create(PostDto body) {

        // 원본 body를 수정하지 않는다.
        String id = UlidCreator.getUlid().toString();
        PostDto newPostDto = new PostDto(id, body.getTitle(), body.getContent());

        // DAO에 저장한다.
        this.postDAO.save(newPostDto);

        return newPostDto;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = this.postDAO.find(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        this.postDAO.delete(id);
    }
}
```

서비스 코드에서 아쉬운 점은 생성자에서 DAO 인터페이스의 구현체를 알 수 밖에 없다는 점이다. 인터페이스 및 다른 DAO 구현체는 위 코드의 중복을 제거한 것 뿐이라 생략

## 참고

- [Presentation Domain Data Layering](https://martinfowler.com/bliki/PresentationDomainDataLayering.html)
