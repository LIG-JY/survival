# 정규식

## Pattern 클래스

### compile

Java에서 정규표현식을 처리할 때 사용하는 클래스다. 정적 메서드 compile()을 통해 정규표현식 문자열을 컴파일해 Pattern 객체를 생성한다.

```<Java>
public static Pattern compile(String regex)
```

## Matcher 클래스

패턴 객체와 문자열을 비교할 때 사용하는 클래스다.

### 주요 메소드

- matches(): 문자열이 정규 표현식과 완전히 일치하는지 확인합니다.

- find(): 문자열에서 다음에 일치하는 부분을 찾습니다.

- group(): 마지막으로 일치된 부분을 반환합니다.

- start()와 end(): 마지막으로 일치된 부분의 시작과 끝 인덱스를 반환합니다.

- replaceAll()와 replaceFirst(): 문자열 내에서 패턴과 일치하는 부분을 교체합니다.

### 사용법

```<Java>
Pattern pattern = Pattern.compile("[a-z]+");
Matcher matcher = pattern.matcher("test123");
boolean isMatch = matcher.matches();
```

1. 패턴 객체 생성(컴파일)
2. 패턴 객체에서 Matcher 객체 생성
3. Matcher 객체의 패턴 일치 메서드 사용

### group 메서드 사용법

group(): 가장 최근에 일치된 전체 문자열을 반환합니다.

group(int group): 지정된 그룹 번호에 해당하는 부분 문자열을 반환합니다. 그룹 번호는 1부터 시작합니다.

#### 그룹화

정규 표현식에서 괄호 ()를 사용하여 그룹을 만들 수 있습니다. 예를 들어, 정규 표현식 (a)(b)는 두 개의 그룹을 정의한다.

예시를 보자

```<Java>
Pattern pattern = Pattern.compile("(\\d+)-(\\d+)");
Matcher matcher = pattern.matcher("123-456");

if (matcher.find()) {
    System.out.println("전체 매칭: " + matcher.group());    // 전체 매칭된 문자열: "123-456"
    System.out.println("첫 번째 그룹: " + matcher.group(1)); // 첫 번째 그룹에 해당하는 문자열: "123"
    System.out.println("두 번째 그룹: " + matcher.group(2)); // 두 번째 그룹에 해당하는 문자열: "456"
}

```

0번 인덱스는 전체 매칭 문자열, 1번은 ()로 묶인 첫번째 그룹 즉 123이죠, 2번은 456


