# 데이터베이스

## DB (Database)

***organized collection of data stored and accessed***

[데이터 베이스](https://en.wikipedia.org/wiki/Database)는 **구조화**(조직화된) 정보의 집합이다.

[오라클에서 정의한 Database](https://www.oracle.com/kr/database/what-is-database/)

## DBMS

최종 사용자 또는 응용 프로그램은 데이터베이스에서 데이터를 쉽게 찾을 수 있어야 하고, 효율적으로 조작(추가, 수정, 삭제)할 수 있어야 한다. 또한 접근 권한, 보안 문제를 다룰 수 있어야 한다. 즉, 데이터베이스를 관리하는 소프트웨어가 필요하다. 이것이 [데이터베이스 관리 시스템](https://ko.wikipedia.org/wiki/데이터베이스_관리_시스템)이다.

### 가장 인기 있는 DBMS

현재 가장 인기 있는 건 RDBMS(Relational Database Management System)다. MySQL, MariaDB, PostgreSQL, MS SQL Server, Oracle 등이 모두 여기에 속한다. 그래서 일반적으로 DB라고 부르는 건 DBMS, 그 중에서도 RDBMS를 의미할 때가 많다.

### DBMS가 지원하는 데이터 베이스 언어

- 데이터 베이스 언어 : 높은 레이어의 개념 데이터 베이스와 같은 레이어
- SQL : 구현된 데이터 베이스 언어 DBMS와 같은 레이어

DBMS는 [데이터베이스 언어](https://ko.wikipedia.org/wiki/데이터베이스_언어)를 제공한다. 데이터 베이스 언어의 종류 3가지는 아래와 같다.

- DDL (Data Definition Language) : Schema
- DML (Data Manipulation Language) : Query & Command
- DCL (Data Control Language) : Grant, Revoke, Commit, Rollback

**대부분의 RDBMS에서 데이터베이스 언어는 모두 SQL로 표현(구현)된다.** 특히 Schema란 걸 강조하기 위해 **DDL**이라는 표현은 따로 자주 사용하니까 기억해두면 좋다.

## Data Model

- 데이터 모델, 데이터 베이스는 같은 레이어

데이터 베이스는 데이터를 구조화한다. 그래서 어떻게 `구조화` 한다는 건데? 라고 질문했을 때 대답이 [데이터 모델]((https://en.wikipedia.org/wiki/Data_model))이다.

[데이터 모델은 크게 3가지 종류](https://opentextbc.ca/dbdesign01/chapter/chapter-4-types-of-database-models/) 로 구분한다.

- Conceptual Data Model
- Logical Data Model
- Physical Data Model

일반적으로 데이터 모델을 구분한다고 하면 위 3가지 종류를 다시 세분화한 모델(특히 논리적 데이터 모델을 세분화함)의 유형을 다룬다.

## 관계형 모델

- 관계형 모델이라는 추상화된 개념을 실제로 구현한 것이 관계형 데이터 베이스(RDBMS)의 테이블

논리적 데이터 모델에서 가장 인기 있는 모델은 [관계형 모델](https://ko.wikipedia.org/wiki/관계형_모델)이다.  
다른 논리적 데이터 모델에 Hierarchical, Network, Object-based Model 등이 있다.

[이 책](https://opentextbc.ca/dbdesign01/chapter/chapter-7-the-relational-data-model/)에 관계형 모델을 포함한 다양한 논리적 데이터 모델에 대한 내용을 참고하자.

관계형 모델을 기반으로 실제로 구현된 데이터베이스 시스템(소프트웨어)가 바로 [관계형 데이터베이스](https://ko.wikipedia.org/wiki/관계형_데이터베이스).

### ERM vs RM

정말 많은 사람들이 ERM(Entity-Relationship Model)과 RM(Relational Model)을 구분하지 못 한다. ERM의 Relationship은 Entity 사이의 관계를 의미하고, RM의 Relation은 `Tuple의 집합(거칠게 말하면 Table이다. 물론 Tuple을 관계형 데이터베이스 시스템에서 구현한 것이 테이블)`을 의미한다. Relationship과 Relation은 유사해 보이지만(심지어 번역하면 둘 다 “관계”라서 구분하기 어렵다), 실제로 이 둘은 “락”과 “락스”처럼 아무 상관 없는 용어라고 생각하자.

[참고](https://stackoverflow.com/questions/25589394/what-is-the-difference-between-an-erd-and-a-relational-diagram)

### 관계형 모델을 제안한 논문

💽 [A Relational Model of Data for Large Shared Data Banks](https://web.archive.org/web/20070612235326/http://www.acm.org/classics/nov95/toc.html)

*“The term **relation** is used here in its accepted mathematical sense. Given sets S1, S1, ···, Sn, (not necessarily distinct), R is a **relation** on these n sets if it is a **set of n-tuples** each of which has its first element from S1, its second element from S1, and so on.”*