# Jdbc Template

## Jdbc Template과 Jdbc의 관계

JdbcTemplate은 스프링 프레임워크에 포함된 관계형 데이터베이스와 상호작용하기 위한 모듈이다. JdbcTemplate은 JDBC를 추상화했다. 직접적으로 Jdbc를 사용하면 코드량이 많아지고 불편한 점이 발생한다. 따라서 JdbcTemplate을 사용해 편리하게 데이터베이스와 상호작용할 수 있다.

## JDBC Test 방법

Test Resource Root(green color directory)에서 JdbcTemplate를 주입받아서 테스트하면 된다.

### 통합 테스트 예시

`@SpringBootTest`: Loads the full application context for integration testing.

`@AutoConfigureTestDatabase`: Configures an in-memory database for testing, ensuring your tests do not affect the real database.

`@Autowired`: Injects the YourRepository and JdbcTemplate beans into the test.


1. Set Up Your Maven or Gradle Dependencies

```xml
<dependencies>
    <!-- Spring Boot Starter Test for testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <!-- H2 Database for in-memory DB -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

2. Create a Repository Class

Assuming you have a simple repository class that uses JdbcTemplate

```Java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class YourRepository {

    private final JdbcTemplate jdbcTemplate;

    public YourRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String findNameById(Long id) {
        return jdbcTemplate.queryForObject("SELECT name FROM your_table WHERE id = ?", new Object[]{id}, String.class);
    }
}
```

3. Write Your Test Class

Create a test class that uses @SpringBootTest to load the application context and @AutoConfigureTestDatabase to replace the application's datasource with an in-memory H2 database for testing.

```Java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class YourRepositoryTest {

    @Autowired
    private YourRepository yourRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testFindNameById() {
        // Arrange
        jdbcTemplate.execute("CREATE TABLE your_table (id BIGINT PRIMARY KEY, name VARCHAR(255));");
        jdbcTemplate.update("INSERT INTO your_table (id, name) VALUES (?, ?)", 1L, "Test Name");

        // Act
        String name = yourRepository.findNameById(1L);

        // Assert
        assertThat(name).isEqualTo("Test Name");

        // Clean up
        jdbcTemplate.execute("DROP TABLE your_table;");
    }
}
```

### 유닛 테스트 예시

To unit test a class that uses JdbcTemplate `without involving Spring's application context loading` and `without using an actual database`, you can mock JdbcTemplate using a mocking framework like `Mockito`.

`@Mock`: This annotation is used to create a mock instance of JdbcTemplate.

`@InjectMocks`: This annotation is used to create an instance of YourRepository and inject the mocked JdbcTemplate into it.

`@BeforeEach and MockitoAnnotations.openMocks(this)`: Initializes the mocks(@Mock, @InjectMocks 모두 초기화) before each test. Note that from Mockito 3.4.0, you can use @ExtendWith(MockitoExtension.class) instead of MockitoAnnotations.openMocks(this) for better integration with JUnit 5.

1. Set Up Your Maven or Gradle Dependencies

```xml
<dependencies>
    <!-- JUnit Jupiter API for JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.7.0</version>
        <scope>test</scope>
    </dependency>
    <!-- JUnit Jupiter Params for parameterized tests -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <version>5.7.0</version>
        <scope>test</scope>
    </dependency>
    <!-- JUnit Jupiter Engine to run JUnit 5 tests -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.7.0</version>
        <scope>test</scope>
    </dependency>
    <!-- Mockito for mocking -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>3.6.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

2. Write Your Test Class with Mockito

```Java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class YourRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private YourRepository yourRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindNameById() {
        // Arrange
        Long id = 1L;
        String expectedName = "Test Name";
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class))).thenReturn(expectedName);

        // Act
        String actualName = yourRepository.findNameById(id);

        // Assert
        assertThat(actualName).isEqualTo(expectedName);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(String.class));
    }
}
```

## CommandLineRunner

### CommandLineRunner은 왜 사용할까?

CommandLineRunner은 어떤 목적으로 application이 실행된 후, 그리고 그 어플리케이션이 요청을 받기 전 (CLI로) 코드를 실행하기 위해서 사용된다.

### CommandLineRunner 사용법

CommandLineRunner을 구현한 클래스는 run method를 오버라이딩 해야한다. run 메소드의 인자(String... args)에 값을 입력하기 위해서는 스프링 부트 어플리케이션을 실행할 때 명령줄에서 인자를 입력하면 된다.  
아래 예시를 확인해보자!

- gradle

```sh
gradle bootRun --args='--your.custom.argument=customValue --anotherArgument=anotherValue'
```

- maven

```sh
mvn spring-boot:run -Dspring-boot.run.arguments=--your.custom.argument=customValue,--anotherArgument=anotherValue
```

- java jar

```sh
java -jar yourapp.jar --your.custom.argument=customValue --anotherArgument=anotherValue
```

### 의문점 Main함수도 마찬가지로 명령줄 인수를 받는데 run의 인자와 값이 동일한가?

결론부터 말하면 동일하다. 스프링 부트의 메커니즘에서 가장 먼저 Main함수가 실행되고 여기의 인자로 명령줄 인수를 입력받는다. 그 후에 빈이 생성되고, 이 빈에 포함되는 CommandLineRunner 개체도 생성된다. 그리고 Run의 인자로 이미 받은 명령줄 인수의 값이 전달된다.

아래는 CommandLineRunner가 실행되는 메커니즘이다.

1. 스프링 어플리케이션 실행 : JVM이 Main함수를 실행한다.

2. 명령줄 인수로 받은 값을 메인 함수로 pass

3. Bootstrapping Spring Context : 스프링 프레임워크를 초기화하고, application context 설정, component scan, 빈 생성, auto-configuration 등을 수행한다.

4. CommandLineRunner Bean 생성 : 이 빈은 다른 나머지 빈 보다 우선해서 개체화 된다.

5. run 메소드에 명령줄 인자 값 입력받고 실행 : application context가 완전히 초기화된 후 스프링 부트는 자동으로 run 메소드를 호출하게 된다. 이 때 run의 인자로는 메인 함수로 입력받은 명령줄 인수의 값이 복사된다.

### CommandLineRunner 소스 코드 및 사용 예시

```Java
@FunctionalInterface
public interface CommandLineRunner {
    void run(String... args) throws Exception;
}
```

추상 메서드가 오직 하나인 함수형 인터페이스이다. 사용 예시는 다음과 같다.

```Java
@Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println(args);
        };
    }
```

method-level annotation `@Bean`은 스프링 어플리케이션이 실행되는 동안 return value를 빈으로 application context에 등록하게 된다. 이 때 메서드 이름인 commandLineRunner가 빈의 이름이 된다.

CommandLineRunner은 함수형 인터페이스다. 함수형 인터페이스는 inner 익명 클래스 대신 람다 식을 통해 인터페이스를 구현할 수 있다. 이 때 람다식의 메서드 시그니처는 함수형 인터페이스의 유일한 추상 메서드의 메서드 시그니처와 동일하다. 즉 람다식은 `함수형 인터페이스의 유일한 추상 메서드를 구현`하게 된다.

이런 함수형 인터페이스의 성질이 스프링의 메서드 레벨 어노테이션의 성질과 결합되어 CommandLineRunner 타입의 빈을 등록하는 것이다. CommandLineRunner 빈의 경우, Spring Boot 애플리케이션은 애플리케이션 시작 시 `자동으로` 이러한 CommandLineRunner 인스턴스를 찾아서 실행한다.

위에서 설명한 것 처럼 CommandLineRunner는 Spring Boot가 제공하는 특별한 유형의 빈으로 부트 스트랩 이후 가장 먼저 생성되는 개체이기 때문이다.

## TransactionTemplate

### TransactionTemplate을 이용한 트랜젝션 관리

TransactionTemplate은 스프링 프레임워크에 포함된 모듈이다. transaction을 추상화했다. 보통 PlatformTransactionManager에서 TransactionTemplate에 대한 configuration을 하고, execute 메서드를 이용해서 트랜젝션을 다룰 수 있다. 아래는 그 예시다.

```java
@Component
public class AppRunner implements CommandLineRunner {
	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;
	
	public AppRunner(JdbcTemplate jdbcTemplate,
				TransactionTemplate transactionTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.transactionTemplate = transactionTemplate;
	}
	
	@Override
	public void run(String... args) throws Exception {
		transactionTemplate.execute(status -> {
			String sql = """
				INSERT INTO people(name, age, gender) VALUES(?, ?, ?)
				""";
	
			jdbcTemplate.update(sql, "홍길동", 15, "남");
	
			// 이런 식으로 예외를 던지면 전부 없던 일로 만들 수 있다.
			// throw new RuntimeException("FAIL!");
	
			return null;
		});
	}
}
```

### 예외

주석 처리된 RuntimeException의 주석을 해제하고 예외를 던지면 예외가 발생하고 롤백되서 데이터베이스에는 영향을 주지 않게 된다.  
주석처리를 해제하면 IDE에서 throws Exception이 필요없다고 알려준다.

- Unchecked Exceptions : RuntimeException은 unchecked exception으로 RuntimeException을 상속받는다. unchekced exception의 경우 Java에서는 메소드 시그니처 옆에 throws clause를 적을 필요 없다.

- Lambda Exception Handling: TransactionTemplate.execute 메서드는 인자로 TransactionCallback<T> action를 받는다. 이는 함수형 인터페이스이며 checked exception을 선언하지 않다. 위 예시에서는 아니지만 만약 execute의 메서드 인자로 람다 표현식이나 익명 클래스를 사용하는 경우 람다 내에서 던져진 checked exception는 throws 절로 선언할 수 없으므로 이를 catch하거나 unchekced exception로 변환해야 한다.

- TransactionTemplate Handling: TransactionTemplate.excute는 내부적으로 예외를 처리하도록 설계되었다. 따라서 TransactionCallback<T>을 실행하는 동안 런타임 예외가 발생하면 TransactionTemplate이 catch해 트랜잭션을 롤백한 다음 예외를 던진다.

### DAO

## (?, ?, ?)로 sql injection 막기