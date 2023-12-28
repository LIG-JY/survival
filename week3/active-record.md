# Active Record

[Active Record Pattern](https://www.martinfowler.com/eaaCatalog/activeRecord.html)

[Wiki](https://ko.wikipedia.org/wiki/%EC%95%A1%ED%8B%B0%EB%B8%8C_%EB%A0%88%EC%BD%94%EB%93%9C_%ED%8C%A8%ED%84%B4)

Java와 Hibernate의 예시를 살펴보자

```<java>
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    // 기본 생성자와 게터/세터는 생략
}
```

```<hibernate.cfg.xml>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>

    <session-factory>
        <!-- 데이터베이스 연결 정보 -->
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/your_database</property>
        <property name="hibernate.connection.username">your_username</property>
        <property name="hibernate.connection.password">your_password</property>

        <!-- Hibernate 사용 시 SQL 출력 -->
        <property name="hibernate.show_sql">true</property>

        <!-- Hibernate 사용 시 DDL을 자동으로 생성 -->
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!-- 데이터베이스 방언 설정 -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- User 클래스 위치 지정 -->
        <mapping class="your.package.path.User"/>
    </session-factory>
</hibernate-configuration>
```

```<java>
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        // Hibernate 설정 가져오기
        Configuration configuration = new Configuration().configure();

        // SessionFactory 생성
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // Session 열기
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // 새로운 사용자 생성
            User newUser = new User();
            newUser.setName("John Doe");
            newUser.setEmail("john@example.com");
            session.save(newUser);

            // 사용자 조회
            User foundUser = session.get(User.class, newUser.getId());
            System.out.println("Found User: " + foundUser.getName() + ", " + foundUser.getEmail());

            // 사용자 업데이트
            foundUser.setEmail("john.doe@example.com");
            session.update(foundUser);

            // 사용자 삭제
            session.delete(foundUser);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```
