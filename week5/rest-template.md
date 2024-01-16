# rest-template

restTemplate의 메서드들을 알아보자.

## getForObejct

Spring application에서 HTTP GET 요청을 하고 응답을 받는 메서드다.

### getForObejct의 목적

- 함수 인자로 받은 URL로 GET 요청을 한다.

- 응답을 받아 함수 인자로 주어진 Class 타입에 맞게 변환한다. (deserialization)

### getForObejct의 특징

- 자동 역직렬화

- Error Handling : 4xx or 5xx의 상태코드 응답을 받으면 각각 상태코드에 맞는 예외를 던진다. (HttpClientErrorException or HttpServerErrorException)

### getForObejct의 메서드 시그니처

```<Java>
public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException
```

- <T> : getForObejct의 리턴 타입을 T로 결정한다. 즉 T 타입으로 역직렬화 하게 된다.

- url : 말 그대로 요청할 url

- responseType : 역직렬화할 타입을 결정하는 인자로 Class<T> 타입이다. 예를 들어 String.class

- urlVariables : 동적으로 url을 조작할 때 필요한 placeholder가 포함된다. 선택사항이다.

- throws RestClientException : 클라이언트 사이드 HTTP 에러가 발생하면 RestClientException을 던진다.

### getForObejct의 메서드 사용 예시

```<Java>
restTemplate.getForObject(url, String.class)
```

이렇게 사용하면 url로 HTTP GET 요청을 보내고 응답을 String으로 역직렬화 하게 된다. 웹의 RESTful api에서 흔히 사용하는 패턴이다. 이 때 문자열은 JSON 또는 XML의 형태를 가진다.

## postForLocation

### postForLocation의 목적

- RestTemplate은 Spring에서 제공하는 템플릿으로 POST 요청을 보내고, 응답으로 반환된 URI를 리턴한다.

- 서버가 새로 생성된 리소스의 URI를 [Location 헤더](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Location)에 넣어 응답할 때 이를 추출할 수 있다.

### postForLocation의 특징

- Null 반환: 서버가 Location 헤더를 반환하지 않는 경우, 이 메서드는 null을 반환한다.

- 예외 처리 : 서버에서 4xx 또는 5xx와 같은 클라이언트 또는 서버 오류 응답을 반환하는 경우, RestTemplate은 HttpClientErrorException 또는 HttpServerErrorException을 던진다.

- Location 헤더의 URI는 새로 생성된 리소스(status code 201)말고 redirection(3xx)에도 사용된다. 따라서 test할 때 URI 타입의 반환값을 사용하기 위해서는 Controller에서 응답 헤더에 Location 속성을 넣는 처리를 해줘야한다.

### postForLocation의 시그니처

```<Java>
public URI postForLocation(String url, Object request, Object... urlVariables)
```

- url : 요청할 url
- request : 요청 본문에 들어갈 객체. 일반적으로 이 Java 객체를 JSON 형태로 변환 후 직렬화해서 서버에 요청하죠.
- urlVariables : URL 템플릿에서 {}로 표시된 부분에 대응되는 변수들을 순서대로 전달할 수 있다.
