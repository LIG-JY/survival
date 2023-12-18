# URI & mime type

## contents

### URI

[리소스를 식별하는 방법](https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/Identifying_resources_on_the_Web)

식별을 어떻게 할 것인가? 식별자(Identifier = ID)를 활용한다.

#### URI의 분류

URI은 크게 둘로 나눌 수 있다.

1. URL(Uniform Resource Locator) → 리소스의 위치. 위치 변경에 취약함.
2. URN(Uniform Resource Name) → 리소스의 “유니크”한 이름. 사실상 쓰이지 않음.

URI라고 쓰는 건 대부분 URL을 의미한다. 따라서 URI와 URL을 크게 구별하지 않고 동의어에 가깝게 사용한다.

### [MIME Type](https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types) (Content Type, Media Type)

Content의 종류를 표현하는 방식이다. `<type>/<subtype>`의 형태로 쓴다. HTTP Headers에 `Content-Type` 속성으로 전달한다. [IANA](https://www.iana.org/assignments/media-types/media-types.xhtml)에 등록된 목록을 참고하자.  
아래는 Web developers에게 중요한 MIME type이다.

1. `text/plain` ⇒ E-mail에서 자주 사용.
2. `text/html` ⇒ 일반적인 웹 문서. HTML 문서.
3. `text/css`
4. `text/javascript`
5. `application/xml` ⇒ 범용. 자기서술적이기 상대적으로 어렵다.
6. `application/atom+xml`
7. `application/json` ⇒ 범용. 자기서술적이기 굉장히 어렵다.
8. `application/dns+json`

`범용적일 수록 자기서술적이기 어렵다!!!`
