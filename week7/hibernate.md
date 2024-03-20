# Hibernate

JPA 인터페이스의 구현체다.

## 데이터 모델과 객체 모델 불일치 문제

https://en.wikipedia.org/wiki/Object%E2%80%93relational_impedance_mismatch

>> 객체 지향 프로그래밍과 관계형 데이터베이스 간의 구조적인 불일치 구체적인 사례는 위키에 잘 나와있다.

## EntityManager와 EntityManagerFactory

엔티티 매니저의 생성 과정은 다음과 같다.

1. persistence.xml의 설정 정보를 사용해 EntityManagerFactory 생성한다.

 src/main/resources 폴더 아래에 META-INF 폴더를 만들고 persistence.xml 파일을 만들어 보자

 ```XML
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">
    <persistence-unit name="demo">
        <class>com.example.demo.models.Person</class>
        <properties>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:postgresql://localhost:5432/postgres"/>
            <property name="jakarta.persistence.jdbc.user"
                      value="postgres"/>
            <property name="jakarta.persistence.jdbc.password"
                      value="password"/>
        </properties>
    </persistence-unit>
</persistence>
 ```

 그리고 EntityManagerFactory를 생성하는 코드는 다음과 같다.

 ```java
 static private EntityManagerFactory entityManagerFactory;

@BeforeAll
static void createEntityManagerFactory() {
    entityManagerFactory = Persistence.createEntityManagerFactory("demo");
}
 ```

 우선 Persistence 클래스를 살펴보자. 이 클래스의 주요한 역할은 EntityManagerFactory 개체를 생성하는 것이다.(bootstrap) createEntityManagerFactory라는 메서드를 통해서 EntityManagerFactory를 생성할 수 있다. createEntityManagerFactory의 메서드 시그니처는 다음과 같다.

 ```java
 public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, null);
    }
 ```

 매개변수로 String 타입의 persistenceUnitName을 받게 된다. persistence.xml의 <persistence-unit name="demo">에서 [persistence unit](https://docs.oracle.com/middleware/1221/toplink/concepts/app_dev.htm#OTLCG94272)의 이름을 지정할 수 있다.

 @BeforeAll 어노테이션을 사용한 이유는 무엇일까? createEntityManagerFactory는 JPA를 동작시키기 위한 기반 개체를 생성하고, 커넥션 풀을 생성하는 등 비용이 큰 메서드다. 따라서 테스트에서 오직 한 번만 실행하기 위해서 @BeforeAll 키워드를 사용했다. @BeforeAll 어노테이션을 사용하기 위해서는 static 해야하기 때문에 멤버 변수와 테스트 멤버 함수 createEntityManagerFactory에 static 키워드가 붙는다.

 일반적으로 EntityManagerFactory는 어플리케이션 전체에서 딱 한 번만 생성하고 공유해서 사용해야한다. 즉 싱글턴으로 만들어 global하게 접근하도록 한다.

## "No persistence provider for EntityManager named demo", “Could not find any META-INF/persistence.xml file in the classpath” 에러

src/main/resources 폴더 아래에 META-INF 폴더를 만들고 persistence.xml 파일을 만들어 persistence config 데이터를 명시해야함

## <class>com.example.demo.models.Person</class>

xml의 <class>구문은 entity class를 persistence unit의 persistence context에 등록하는 것을 명시한다. 즉 persistence provider(JPA 구현체)에게 Person 클래스가 persistence unit이 관리하는 엔티티라는 것을 알려주게 된다.

```xml
<xsd:element name="class" type="xsd:string" 
                           minOccurs="0" maxOccurs="unbounded">
	<xsd:annotation>
		<xsd:documentation>

		Managed class to be included in the persistence unit and
		to scan for annotations.  It should be annotated 
		with either @Entity, @Embeddable or @MappedSuperclass.

		</xsd:documentation>
	</xsd:annotation>
</xsd:element>
```

따라서 Person 클래스에 @Entity 어노테이션 표기가 필요하다.

## Unable to locate entity descriptor

mapping 정보가 없으면 에러가 발생함

객체에 @Entity 어노테이션을 추가하면 해결할 수 있음

참고로 entity descriptor는 entity class와 data base의 테이블의 mapping정보를 가진 metadata라고 생각하면 된다.

## EntityDescriptor에서 descriptor의 의미

코딩할 때 자주 볼 수 있는게 Descriptor라는 단어다. 일반적인 descriptor의 의미를 알아보자.

descriptor라는 용어는 보통 프로그래밍에서 다른 데이터 구조나 개체에 대한 정보를 제공하는 데이터 구조 또는 개체다. 즉 본질적으로 descriptor는 메타데이터를 가지고 있는 컨테이너다. 어떤 자원, entity, 파일 등에 대해서 이들을 쉽게 관리할 수 있도록 메타데이터를 컨테이너화(추상화 + 캡슐화)했다고 생각하면 됨

대표적으로 예시를 확인해보자.

- File Descriptors
운영체제에서 파일에 접근할 때 사용하는 abstract indicator

- Class Descriptors
OOP의 reflection 환경에서 class descriptor은 클래스에 대한 메타데이터를 가지고 있는 데이터 구조다.

- DB Schema Descriptor
말 그대로 데이터베이스 스키마(table, column, data type, relationship, constraint) 즉 메타데이터를 가지고 있는 데이터 구조다. ORM 프레임워크들이 이를 통해 데이터베이스와 상호작용하게 된다.

## Entity에서 default 생성자

기본 생성자가 없으면 컴파일 에러가 발생한다.

https://www.baeldung.com/jpa-no-argument-constructor-entity-class

## Typed query 사용하는 것 추천

제너릭으로 entity를 컬렉션으로 다루기 쉽게 구현함