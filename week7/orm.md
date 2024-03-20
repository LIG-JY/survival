# ORM

## ORM이란

[ORM](https://en.wikipedia.org/wiki/Object%E2%80%93relational_mapping)은 객체와 관계형 데이터를 매핑하는 작업 내지는 기술, 도구 등을 의미한다.

도구의 경우 M은 Mapper를 의미하는 경우가 많다.

ORM은 아래와 같은 기능을 포함한다.

- SELECT 즉 데이터베이스에서 조회한 뒤 객체를 생성하고 객 체의 생명주기를 관리한다.
- 객체의 상태를 활용해 변화를 감지하고 데이터베이스에 UPDATE한다. 이 때 트랜잭션 범위를 고려하게 된다.

## ORM의 장점

- 일반적인 ORM 도구들은 SQL문을 자동으로 생성하고 다양한 DBMS에 마다 세부적으로 다른 부분에 대응하기 좋다.

- SQL 작성 및 매핑 작업이 자동으로 이뤄지기 때문에 우리가 비즈니스 로직에 집중할 수 있게 돕고 유지보수하기 좋다. 이를 통해 데이터베이스에 끌려가는 무기력한 도메인 객체가 아닌, 객체지향 원칙을 따르는 도메인 객체를 영속화할 수 있게 된다.

## Jakarta EE

https://ko.wikipedia.org/wiki/자카르타_EE

깃허브 레포를 보면 Jakarata EE 플랫폼의 다양한 컴포넌트를 확인할 수 있다.

## JPA

Jakarta EE의 관계형 데이터 관리 API. Java에서 사용하는 ORM 표준

JPA는 인터페이스, 즉 스펙만 다루고 있기 때문에 Hibernate, EclipseLink 등의 구현을 사용하게 된다. 공식적으로는 EclipseLink가 참조 구현이지만, 업계에서 오래 전부터 써왔고 JPA 스펙에 막대한 영향을 끼친 Hibernate가 가장 널리 사용된다.

## JPA Entity

JPA는 Entity라는 단위를 활용하는데, JPA에 익숙해지면 DB 세계(특히 ERM)의 Entity와 OOP 세계(특히 DDD)의 Entity를 절묘하게 결합하는데 활용할 수 있다.

DB 세계와 관련: “Entities represent persistent data stored in a relational database automatically using container-managed persistence.” (저장된 데이터)

OOP 세계와 관련: “An entity models a business entity or multiple actions within a single business process.” (데이터와 행동을 모두 포함)

전자에만 집중하면 단순히 SQL문을 자동으로 생성해주는 도구로 쓰게 되고, 이렇게 하면 간단한 CRUD 프로그램을 만들 때는 생산성 향상에 도움이 된다. 하지만 후자를 지속적으로 관리하지 않으면, 어느샌가 관계형 모델도, 객체지향도 만족시키지 못 하는 애매한 DB에 끌려가는 프로그램이 될 수 있다.