# JDBC

## JDBC (Java Database Connectivity)

> [JDBC](https://ko.wikipedia.org/wiki/JDBC)
> 

> [JDBC driver](https://en.wikipedia.org/wiki/JDBC_driver)
> 

> [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
> 

Java에서 RDBMS를 사용할 수 있게 해주는 API.

API는 그냥 인터페이스기 때문에, 각 DBMS 벤더에서 제공하는 JDBC Driver(구현)가 있어야 실제로 사용할 수 있다.

어플리케이션을 만들어서 실습한다. 아래에 실습 절차가 있다.

## Connection

> [Establishing a Connection](https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html)

이 튜토리얼을 참고하자

### 연결 java 코드

```java
import java.sql.Connection;

String url = "jdbc:postgresql://localhost:5432/postgres";

Properties properties = new Properties();
properties.put("user", "postgres");
properties.put("password", "password");

Connection connection = DriverManager.getConnection(url, properties);
```

### “No suitable driver found for jdbc:postgresql://localhost:5432/postgres” 에러

- PostgreSQL용 JDBC Driver가 필요함을 보여준다.
- `build.gradle` 파일에 의존성 추가. 버전은 최신 버전으로 맞출 것.

- [pgJDBC](https://jdbc.postgresql.org/)
- [Maven Repository](https://mvnrepository.com/artifact/org.postgresql/postgresql)

### Connections vs JDBC Driver

> java.sql.Connection is an interface in Java's SQL package that represents a connection to a specific database. When a Connection object is created, it establishes a link between the Java application and a database, allowing the application to execute SQL queries and retrieve results.  
A JDBC (Java Database Connectivity) driver, on the other hand, is an implementation of the JDBC API that facilitates this connection. It acts as a mediator between the Java application and the database, interpreting the requests from the Java program and translating them into a format understood by the specific database system.  
The key difference between the two is that java.sql.Connection is a part of the JDBC API, an abstract concept representing a connection in the Java program, while a JDBC driver is a concrete implementation that enables this connection, making it possible for Java applications to communicate with a database. Essentially, you use the JDBC driver to establish a Connection, and then use the Connection to interact with the database.

- Java Connection 
	- 특정 데이터베이스와 연결을 represent하는 개체
	- Java Applicagion에서 connection 개체를 만들어 SQL을 실행할 수 있다.
- JDBC driver
	- JDBC API의 구현체
	- Java Application과 데이터베이스를 중재한다.
	- Java application에서 JDBC API를 호출하면, JDBC driver은 이를 Database의 protocol에 맞게 번역한다. 각 데이터베이스마다 사용하는 프로토콜이 다르고 데이터베이스 query language도 각기 다르기 때문이다.
	- JDBC가 있어야지 Connection개체를 사용할 수 있다.

## Statement

[Processing SQL Statements with JDBC](https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html)

```Java
public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties properties = new Properties();
        properties.put("user", "postgres");
        properties.put("password", "password");
        Connection connection = DriverManager.getConnection(url, properties);

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM people";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            System.out.print(rs.getString("name"));
            System.out.print(" : ");
            System.out.println(rs.getString("age"));
        }
    }
```

## Prepared Statement

- Concept of PreparedStatement: When a PreparedStatement is created, it is given an SQL statement. This statement is sent to the database management system (DBMS) immediately, where it is compiled and stored. Because of this precompilation, the PreparedStatement contains not just the SQL statement but a precompiled version of it.

> PresparedStatement는 DBMS로 전송되서 컴파일(executed by the database engine SQL을 DBMS engine이 효율적으로 실행할 수 있도록 하는 작업)되어 저장된다.

- Comparison with Statement: The main difference between PreparedStatement and a regular Statement is that the latter executes SQL queries without precompilation. Each time a Statement executes, the DBMS compiles the query, which can be inefficient if the same SQL statement is executed multiple times.

  - Advantages of PreparedStatement: Precompilation: As the SQL statement in a PreparedStatement is precompiled, it doesn't need to be compiled every time it's executed. This significantly improves performance, especially when executing the statement multiple times.

  - Parameterization: PreparedStatement allows the use of parameters in SQL queries. Parameters are placeholders in the SQL statement that are filled in with actual values at runtime. This feature is especially useful for executing the same statement repeatedly with different values.

> 그냥 Statement는 매번 쿼리할 때마다 precompilation이 없기 때문에 DBMS에서 매번 컴파일 하게되어 비효율적이다. 반면 PreparedStatment는 여러 번 동일한 statment를 실행할 때 매번 컴파일 할 필요가 없어서 효율적이다.
> PrepraredStatement는 SQL 문의 paramater화가 가능하다. 내부적으로 precompilation에서 SQL 구조를 가지고 있기에 가능하다. 값만 바꾸면 된다.

```Java
public static void main(String[] args) {
        // Database URL, username and password
        String url = "jdbc:mysql://localhost:3306/exampledb";
        String username = "root";
        String password = "password";

        // SQL query with parameters
        String sql = "SELECT * FROM employees WHERE department = ? AND salary > ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting parameters
            pstmt.setString(1, "Engineering"); // First parameter (department)
            pstmt.setDouble(2, 50000);         // Second parameter (salary)

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Retrieving data from result set
                    String employeeName = rs.getString("name");
                    double salary = rs.getDouble("salary");
                    System.out.println("Employee: " + employeeName + ", Salary: " + salary);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
```

- Preventing SQL Injection:

  - SQL Injection: This is a security vulnerability that occurs when an attacker is able to manipulate an SQL query by injecting malicious code into it. This can happen when applications concatenate user input directly into SQL statements without proper validation or escaping.

  - Role of PreparedStatement: A PreparedStatement treats all input as data and not as part of the SQL syntax. This means that user input, regardless of its content, is never treated as executable code by the SQL engine. This inherent behavior of PreparedStatement makes it a key tool in preventing SQL injection attacks.

> sql injection은 유저의 인풋(문자열)이 바로 concatenated되서 sql문이 될 때 발생한다. PreparedStatemnt는 "?"를 통해서 SQL문을 parameterize한다. PreparedStatement의 쿼리가 컴파일되면 Precompilation 결과물은 고정되어 저장된다. 따라서 SQL의 구조는 결정되고 ?자리의 파라매터만 바뀌게 된다. 따라서 sql injection으로 value 값에 SQL 구조를 변경하는 값을 넣어도 구조가 변하지 않아 막을 수 있다.

> [Using Prepared Statements](https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)

- [기본 중 기본인 SQL Injection 공격](https://ko.wikipedia.org/wiki/SQL_삽입)
- [Exploits of a Mom](https://xkcd.com/327/)
- [국내 사례](https://www.google.com/search?q=뽐뿌+SQL+Injection)