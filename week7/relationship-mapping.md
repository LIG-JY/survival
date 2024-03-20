# Relationship Mapping

## 효과

데이터 모델 Entity의 관계를 객체 참조로 간단히 활용할 수 있게 해준다.

## 주의사항

오용하기 쉽다.

일반적으로는 DDD의 Aggregate를 구현하기 위해 `CascadeType.ALL`과 `orphanRemoval=true`를 함께 사용하는 게 강력히 권장된다.

- CascadeType

cascade operation에는 몇 가지가 있다. entity에 대한 연산(cascade operation)중 어떤 것이 그 entity와 관계를 맺는 다른 entity에 적용되는가를 나타내는 enum이다.

- orphanRemoval

부모 자식 관계에서 부모 entity가 더 이상 자식 entity를 참조하지 않으면 자동으로 자식 entity가 제거되는 기능

## Aggregate의 개념이 들어간 예시

Person entity가 Item entity를 소유할 때 Person이 Item에 개별적으로 접근하는 일은 절대 없도록 한다. Aggregate를 통해 접근함

## JPA만 사용할 때 문제점

1. persistence.xml에 Entity가 추가될 때 마다 추가해야함
>> TODO: persistence.xml 작성 요령
2. EntityManager을 얻고 이를 통해 연산해야함(Factory를 통해 얻고 하는 과정이 불편함)
>> TODO: EntityManager, EntityManagerFacotry 코드 구조
3. Transaction을 관리하기도 불편하다.(EntityManager에서 Transaction 얻어야함)
>> EntityManager에서 Transaction을 어떻게 관리하는지 코드 까보기