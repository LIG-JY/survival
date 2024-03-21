# Embeddable

## VO

OOP에선 의미가 드러나지 않는 Primitive Type 대신 Value Object를 적극 활용하는 걸 권한다.

VO(값 객체)는 equlity을 기반으로 한다. 즉 reference로 비교하지 않고, 값으로 비교한다.

일반적으로 VO는 immutable하며 재사용성을 위해 나온 개념이다.

JPA에선 Aggregate Mapping을 통해 VO를 데이터베이스에 저장하도록 지원할 수 있다. 특히 VO인 엔티티가 다른 엔티티의 attribute가 되도록 할 수 있다.

## Aggregate Mapping

Aggregate Mapping에서는 두개의 엔티티들이 연관 관계를 가지게 된다. 부모(source) entity가 자식(target) entity를 own하는 개념이다. 여기서 own의 의미를 알기 위해 `strict one-to-one relationship`를 먼저 알아보자.

`strict one-to-one relationship`(엄격한 1대1 관계)는 두 Entity의 각 개체가 서로 서로 일대일 관계를 맺는 다는 것이다. 즉 Entity인 User가 있고, Entity인 UserProfile이 엄격한 1대1 관계를 맺는다고 하면, User의 개체 A와 UserProfile 개체 A'만 1대1 관계를 가진다는 것이다. A는 A'이외의 다른 개체와 관계를 갖지 않는다.

엄격한 1대1 관계를 만족하면 `Attribute Storage`가 가능하다. 즉 부모 엔티티의 테이블에 자식 엔티티를 포함시킨다. 즉 오직 하나의 테이블만 사용할 수 있게된다.

따라서 `Existential Dependency`를 가진다. 부모 엔티티 없이는 자식 엔티티가 존재할 수 없게 된다. 그렇게 해서 부모 엔티티가 파괴되면 자식 엔티티도 파괴된다. 이를 `Cascading Action`라고 부른다.

hibernate에서 이를 코드로 구현하는건 간단하다.

```java
@Embeddable
public class Gender {
  @Column(name = "gender")
  private String value;

  private Gender() {
  }

  private Gender(String value) {
    this.value = value;
  }

  public static Gender male() {
    return new Gender("남");
  }

  public static Gender female() {
    return new Gender("여");
  }

  @Override
  public String toString() {
    return value;
  }
}
```

```java
@Embedded
private Gender gender;
```

## 참고 문서

https://docs.oracle.com/cd/E16439_01/doc.1013/e13981/cmp30cfg011.htm

https://en.wikibooks.org/wiki/Java_Persistence/Embeddables