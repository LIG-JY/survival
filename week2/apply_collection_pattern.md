# Apply Collection Pattern

## contents

### [CRUD](https://ko.wikipedia.org/wiki/CRUD)

데이터에 대해 취하는 모든 기능은 다음 4개로 정리할 수 있다.

1. Create
2. Read
3. Update
4. Delete

### [CQS](https://en.wikipedia.org/wiki/Command–query_separation)

CRUD를 중요한 특징에 따라 2가지로 구분해보자.  
Command와 Query로 나눌 수 있다.

#### 1. Command

Create, Update, Delete이 여기에 해당한다.  
데이터의 상태가 변한다. 즉 안전하지 않다.

#### 2. Query

Read만 여기에 해당한다.  
상태가 변하지 않는다. 안전하다. 따라서 분산, 캐시 등이 수월하다.

##### 분산이 수월한 이유

여러 곳에서 요청해도 동일하다. 상태가 변하지 않으니까!

##### 캐시에 수월한 이유

캐시 정합성을 유지하기 수월하다. 상태가 변하지 않으니까!

### HTTP Method 와 Collection Pattern

Collection Pattern과 HTTP Method를 이용해 CRUD를 표현할 수 있다.

**_1. GET → Read_**  
**_2. POST → Create_**  
**_3. PUT, PATCH → Update_**  
**_4. DELETE → Delete_**

#### 예시: 게시물

1. `GET /posts` → 게시물 목록 (Collection을 Read한다.)  
   List라고 부른다. (관습적인 표현 중 하나)

2. `GET /posts/{id}` → 게시물 상세 (Element을 Read한다.)
   Detail라고 부른다. (관습적인 표현 중 하나)
3. `POST /posts` → 게시물 생성 (Create)
   Post ID는 서버에서 생성한다. Collection pattern에서 생성할 때 HTTP message에서 ID를 지정하지 않는다.
4. `PUT 또는 PATCH /posts/{id}` → 게시물 수정 (Element를 Update한다.)
5. `DELETE /posts/{id}` → 게시물 삭제 (Element를 Delement한다.)

**종종 Bulk update, Bulk delete 등을 하기도 한다. 이럴 때는 Collection을 활용하고(즉 id를 안 써줄 수 있다.), API 스펙 문서에 정확히 기록하자.**

#### 예시: 댓글

##### 바로 comments로 시작하는 경우

1. `GET /comments` → 전체 댓글 목록
2. `GET /comments?post_id={post_id}` → 특정 게시물에 대한 댓글 목록
3. `GET /comments/{id}` → 댓글 하나 조회
4. `POST /comments` → 댓글 생성  
   근데 어떤 게시물에 대해 생성하나? 위에서 언급한 Bulk update, delete와 유사한 경우다. 이 때는 HTTP message Body에 Post ID 정보를 담아줘야 한다. (API 스펙 정의가 필요하겠죠?)
5. `POST /comments?post_id={post_id}` → 특정 게시물에 대한 댓글 생성
6. `PUT 또는 PATCH /comments/{id}` → 특정 댓글 수정
7. `DELETE /comments/{id}` → 특정 댓글 삭제

##### 특정 게시물 아래로 표현하는 경우

1. `GET /posts/{post_id}/comments` → 특정 게시물에 대한 댓글 목록
2. `GET /posts/{post_id}/comments/{id}` → 특정 게시물에 달린 특정 댓글 조회
3. `POST /posts/{post_id}/comments` → 특정 게시물에 대한 댓글 작성
4. `PUT 또는 PATCH /posts/{post_id}/comments/{id}` → 특정 게시물에 달린 특정 댓글 수정
5. `DELETE /posts/{post_id}/comments/{id}` → 특정 게시물에 달린 특정 댓글 삭제

   ✅ 이 경우 전체 댓글 목록을 표현하지는 못한다. 예를 들어 최근 댓글 모음같은 것을 만들 때는 따로 API를 만들어야겠죠?

#### 예시: 로그인/로그아웃

로그인과 로그아웃을 어떻게 리소스로 표현할 수 있을까? 이 때 `추상적인 개념`인 "세션"을 도입한다.

1. `POST /session` → 세션 생성 = 로그인
2. `DELETE /session` → 세션 파괴 = 로그아웃

추가적으로 아래와 같이 할 수도 있다.

1. `GET /session` → 세션 확인 → 내 정보 확인?
2. `GET /users/me` → User ID를 me라고 쓰면, 내부에서 현재 사용자의 User ID로 처리하게 정한다. 그리고 API 스펙 문서에 기록한다. (예전에 FaceBook에서 사용하던 방법)
