# Relational Model

관계형 모델에서 쓰이는 3개의 개념이 쓰인다.

- Relation
- Tuple
- Attribute

가장 작은 것부터 역순으로 살펴보자.

## 속성(Attribute)

속성은 `이름과 타입`으로 구성된다. `속성의 이름은 집합 안에서 유일해야 한다.` 하나의 집합에서 속성의 이름은 유일하기 때문에 속성은 속성의 이름으로 식별 및 구분할 수 있다.

예를 들면 아래와 같다.

- 이름/문자열
- 나이/정수(최대 3자리)
- 성별/문자

대개는 RDBMS에서 테이블의 Column으로 구현된다.

## 튜플(Tuple)

Pair(속성, 값) 쌍의 집합. 

예를 들면 다음과 같다:

```<text>
{ (이름/문자열, 견우), (나이/정수, 13), (성별/문자, 남) }
```

대개는 RDBMS에서 테이블의 Row, Record로 구현된다.

개념적으로 튜플은 집합이기 때문에 중복을 허용하지 않지만, `실제 구현된 대부분의 RDBMS는 중복을 허용한다. 그리고 RDBMS는 NULL도 허용한다.`

## 관계(Relation)

[Relational Model](https://en.wikipedia.org/wiki/Relational_model)  
[Relation (Database)](https://en.wikipedia.org/wiki/Relation_(database))

속성의 집합과 튜플의 집합의 쌍. 속성의 집합을 heading, 튜플의 집합을 body라고 구분한다.  
특히 heading은 스키마와 관련이 있다.

예를 들면 다음과 같다:

```
(
	// Heading
	{ 이름/문자열, 나이/정수, 성별/문자 },

	// Body
	{
		{ (이름/문자열, 견우), (나이/정수, 13), (성별/문자, 남) },
		{ (이름/문자열,직녀), (나이/정수, 12), (성별/문자, 여) }
	}
)
```

관계는 시간에 따라 변하기 때문에, `관계 변수(Relation Value)`란 개념을 통해 관계 변수와 관계 값으로 구분하기도 한다.

대개는 RDBMS에서 Table로 구현되고, 속성 집합을 Schema로 표현한다.
