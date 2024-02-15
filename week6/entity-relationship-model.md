# Entity Relationship Model

## 개념적 데이터 모델

가장 인기 있는 개념적 데이터 모델.

- ERM에서 Entity는 개별적으로 다룰 수 있는 데이터를 의미함
- Entity Type은 같은 Attribute들을 가진 Entity의 집합임(OOP의 Class와 유사하다)
- 따라서 ERD 등을 그릴 때 쓰이는 건 Entity Type임(Class Diagram을 떠올려보자)

참고로 OOP, DDD에선 Entity를 연속성과 식별성이 있는 객체란 의미로 사용한다. 똑같이 Entity란 표현을 쓰지만 실제로는 완전히 다른 의미를 갖는다.

데이터 모델링과 객체 모델링은 목표도 다르고, 용어, 원칙 등도 다르니 주의하자.

> [Entity-Relationship Model](https://en.wikipedia.org/wiki/Entity–relationship_model)
> 

> [The Entity Relationship Data Model](https://opentextbc.ca/dbdesign01/chapter/chapter-8-entity-relationship-model/)
>

## ERD

> [ER Diagram MMORPG](https://commons.wikimedia.org/wiki/File:ER_Diagram_MMORPG.png)

> [Cardinality (data modeling)](https://en.wikipedia.org/wiki/Cardinality_(data_modeling)) 

- 개념적 데이터 모델인 ERM을 시각화하는 (엄청나게 인기 있는) 방법
- 논리적 설계에 들어가기 전에 그려보면 도움이 된다.
- 마름모로 표시하는 Relationship을 꼭 그려보자 이 때 `말이 되는` 설계가 되어야 한다!!!

> 도구나 표기법에 집착하지 말고, 모델을 검증하는 도구로 활용할 것! 엔티티를 발견(!)하고, 적정하게 재배치하는 것만으로도 많은 통찰을 얻을 수 있다. 기계적으로 스키마 변환 작업만 하거나, 한번 정하고 수정하지 않는 건 추천하지 않는다.  
> Crow's Foot Notation을 쓰더라도 Relationship을 꼬박꼬박 써줄 수도 있다.

Crow's Foot Notation 예제 : [ERD-artist-performs-song](https://commons.wikimedia.org/wiki/File:ERD-artist-performs-song.svg)