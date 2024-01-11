# Unit Test

## Content

### [V-Model](https://ko.wikipedia.org/wiki/V_모델)

프로그래밍을 하는 이유는 비즈니스 문제를 해결하기 위해서다. 문제가 잘 해결됬는지 어떻게 확인할 수 있을까? 라는 고민에서 시작된다.

폭포수 모델은 뒤로 돌아갈 수 없다. 따라서 현실에 부합하지 않다. 하지만 폭포수 모델이 다루는 단계를 바탕으로 V 모델은 각 단계에 대한 테스트를 나누고
처음부터 어떻게 테스트 해야 하는지 결정하려고 노력한다.

1. 요구사항 분석 → 사용자 중심 ⇒ 인수 테스트
2. 시스템 설계 → 시스템 사양 결정 ⇒ 시스템 테스트
3. 아키텍처 설계 → 고수준 설계 ⇒ 통합 테스트
4. 모듈 설계 → 저수준 설계 ⇒ 단위 테스트
5. 구현 → 코딩

### Test Matrix

> 우리가 일반적으로 말하는 품질은 외적 품질. 눈에 보이지 않는 내적 품질은 당장에 큰 성과를 내지 않기 때문에 놓칠 때가 많다. 클린 코드 등이 회자되는 까닭도, 거기에 어떤 비즈니스 가치가 있어서가 아니라 “엉망인채로 가면 큰일난다”라는 걸 본능적으로 알기 때문. 맛만 좋으면 되지 주방이 깨끗할 필요가 있냐고 항변하는 식당, 어떻게든 수술만 하면 되지 손을 씻을 필요가 있냐고 항변하는 의사 등을 옹호할 사람은 아무도 없다.
> 품질이 내적 품질과 외적 품질로 나눠진다는 점에서도, 그리고 테스트 영역이 굉장히 넓다는 점에서도 우리는 절반만 감당할 수 있다. 하지만, 내적 품질이 우수하면 외적 품질을 끌어올리거나 대응하기 좋아진다. 산업혁명이나 정보혁명이 폭발적인 이유는 “도구를 개선하는 도구”의 존재가 장기적으로 “생산성”을 높였기 때문.

### Unit Test by JUnit5

자동화된 테스트를 지원하는 툴이다. 우리는 유닛 테스트에 초점을 맞추고 도구 사용법을 배운다.

- 단위 테스트의 관점에서 2가지 질문
  1. 믿고 쓸 수 있는 부품인가?
  2. 믿고 쓸 수 있다면 어떻게 하면 되는가?

### JUnit 5 (Unit Test)

JUnit은 자동화된 테스트를 지원하는 도구. 이름에 Unit이 들어가지만 단위 테스트만 지원하는 건 아님. 통합 테스트, 심지어는 E2E 테스트를 작성하는데도 사용한다.

단위 테스트의 관점에서 질문을 던져보자:

- 믿고 쓸 수 있는 부품인가?
- 믿을 수 있는 부품이 있다면 어떻게 하면 되는가?

#### SICP 1.1.7 Newton’s Method 예제

> [Example: Square Roots by Newton's Method](https://mitp-content-server.mit.edu/books/content/sectbyfn/books_pres_0/6515/sicp.zip/full-text/book/book-Z-H-10.html#%_sec_1.1.7)

맨 처음에는 몇 가지 부품이 있다고 가정하고 코드를 작성한다. goodEnough, improve가 부품에 해당한다.

```java
public double sqrtIter(double guess, double x) {
	if (goodEnough(guess, x)) {
		return guess;
	}

	return sqrtIter(improve(guess, x), x);
}
```

올바른 goodEnough와 improve만 주어진다면, 이 코드는 대부분의 경우엔 문제가 없을 것이다.
(특정한 경우 : improve를 했더니 오히려 발산하면 무한 반복하는 문제가 발생!)

일단, 꽤 괜찮은 값이 뭔지 정해보자.

```java
class NewtonMethodTest {
	private NewtonMethod sut;  // System Under Test

	@BeforeEach
	void setUp() {
		sut = new NewtonMethod();
	}

	@Test
	void goodEnough() {
		// 아래 둘은 너무 명확한 경우
		assertThat(sut.goodEnough(2, 4)).isTrue();
		assertThat(sut.goodEnough(1, 4)).isFalse();

		// 이 정도면 괜찮다 싶은 경우
		assertThat(sut.goodEnough(1.999999, 4)).isTrue();
	}
}
```

이 테스트를 통과시키려면 goodEnough를 이렇게 만들면 된다.

```java
public boolean goodEnough(double guess, double x) {
	final double epsilon = 0.001;
	return Math.abs(Math.pow(guess, 2) - x) < epsilon;
}
```

핵심인 improve를 만들기 위해 뉴튼법으로 2의 제곱근을 구하는 과정을 살펴보자.

- Guess: 1 → (2/1) = 2 → ((2 + 1)/2) = **1.5**
- Guess: 1.5 → (2/1.5) = 1.33333 → ((1.33333 + 1.5)/2) = **1.4166**
- Guess: 1.4166 → (2/1.4166) = 1.41183 → ((1.4166 + 1.41183)/2) = **1.4142**

테스트 코드로 써보자. 몫을 정확히 구할 수 없으므로 DecimalFormat을 도입해서 소수점 자리 수의 제한을 둔다.

```java
@Test
void improve() {
	DecimalFormat decimalFormat = new DecimalFormat("0.####");
	assertThat(decimalFormat.format(sut.improve(1, 2))).isEqualTo("1.5");
	assertThat(decimalFormat.format(sut.improve(1.5, 2))).isEqualTo("1.4166");
	assertThat(decimalFormat.format(sut.improve(1.4166, 2))).isEqualTo("1.4142");
}
```

improve 함수를 구현하자.

```java
public double improve(double guess, double x) {
	return (guess + (x / guess)) / 2;
}
```

테스트 코드

```java
@Test
void sqrtIter() {
	DecimalFormat decimalFormat = new DecimalFormat("0.######");
	assertThat(decimalFormat.format(sut.sqrtIter(1, 2))).isEqualTo("1.414216");
	assertThat(decimalFormat.format(sut.sqrtIter(1, 3))).isEqualTo("1.732143");
	assertThat(decimalFormat.format(sut.sqrtIter(1, 4))).isEqualTo("2");
}

@Test
void sqrt() {
	DecimalFormat decimalFormat = new DecimalFormat("0.######");
	assertThat(decimalFormat.format(sut.sqrt(2))).isEqualTo("1.414216");
	assertThat(decimalFormat.format(sut.sqrt(3))).isEqualTo("1.732143");
	assertThat(decimalFormat.format(sut.sqrt(4))).isEqualTo("2");
}
```

최종 구현

```java
public double sqrt(double x) {
	return sqrtIter(1, x);
}
```

이런식으로 계속 테스트를 나누고, 생각나는 테스트 케이스를 넣어볼 수 있다.

앞으로 Spring과 Mockito 등을 적극적으로 쓰는 테스트 코드를 많이 작성할 거지만, 기본은 단위 테스다!
단순한 Unit Test가 제일 많아야 하고, 이를 통해 신뢰할 수 있는 토대를 구축해야 한다. 믿을 수 있는 부품을 잘 테스트해보자!

## 참고

- Test Matrix

  > [Internal And External Quality](https://wiki.c2.com/?InternalAndExternalQuality)  
  > [My Agile testing project](http://www.exampler.com/old-blog/2003/08/21.1.html)  
  > [Developer Testing](https://developertesting.rocks/)

- Junit

  > [JUnit 5](https://junit.org/)  
  > [SUT](http://xunitpatterns.com/SUT.html)

- Unit Test
  > [The Practical Test Pyramid](https://martinfowler.com/articles/practical-test-pyramid.html)  
  > [“Mocking 때문에 테스트 코드를 작성하기 어렵나요” 영상](https://youtu.be/RoQtNLl-Wko)  
  > [“프론트엔드도 테스트해야 하나요?” 영상](https://youtu.be/-kUmsKRmOnA)
