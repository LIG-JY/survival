# Assingment

## 테이블 생성 쿼리에서 "IF NOT EXISTS"가 필요한 이유

CommandLineRunner의 implementation은 callback method run을 구현함

run 메서드는 스프링 부트의 application context가 로드되고 가장 먼저 실행됨

run 메서드 안에서 테이블 생성 쿼리를 실행함

매번 어플리케이션을 실행할 때 마다 run 메서드는 실행되고 테이블 생성 쿼리를 실행하게 됨

이 때 IF NOT EXISTS 때문에 중복해서 테이블을 생성하는 것을 막을 수 있음

## what is the right data type for unique key in postgresql DB?

- SERIAL 타입을 사용

```java
"""
CREATE TABLE IF NOT EXISTS posts (
	id SERIAL PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	author VARCHAR(255) NOT NULL,
	content TEXT NOT NULL
)
""";
```

[참고](https://stackoverflow.com/questions/11778102/what-is-the-right-data-type-for-unique-key-in-postgresql-db)

## queryForObject(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowMapper<T>)' is deprecated

id로 Post를 찾는 쿼리를 jdbcTemplate로 구현했는데 queryForObject 메서드가 deprecated 됬다고 ide에서 경고

```java
jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
```

위에서 아래로 변경

```java
jdbcTemplate.queryForObject(sql, rowMapper, id);
```

## PostNotFoundException 예외처리

데이터베이스 조회 시 EmptyResultDataAccessException가 발생함

캐치해서 PostNotFoundException 던짐

PostNotFoundException는 커스터마이징 할 수 있음

PostNotFoundException에서 stacktrace 생성 비용을 줄일 수 있음

PostNotFoundException에서 상태코드를 지정

[참고](https://jerry92k.tistory.com/42)

## jdbcTemplate update multiple column query

```sql
UPDATE my_table SET column1 = ?, column2 = ? WHERE id = ?
```

## UPDATE, DELETE  동작

```docs
The count is the number of rows updated, including matched rows whose values did not change. Note that the number may be less than the number of rows that matched the condition when updates were suppressed by a BEFORE UPDATE trigger. If count is 0, no rows were updated by the query (this is not considered an error).
```

> UPDATE, DELETE 모두 특정 행을 찾지 못하면 0을 반환함. 따라서 예외가 발생하지 않음

https://www.postgresql.org/docs/current/sql-update.html

https://www.postgresql.org/docs/current/sql-delete.html

## FillInstackTrace