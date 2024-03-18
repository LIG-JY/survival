# Hibernate

## 데이터 모델과 객체 모델 불일치 문제(TODO)

https://en.wikipedia.org/wiki/Object%E2%80%93relational_impedance_mismatch

## EntityManager(TODO)

## EntityManagerFactory(TODO)

beforAll, afterAll과 함께 설명

## “Could not find any META-INF/persistence.xml file in the classpath” 에러

java 환경이라서 src/main/resources 폴더 아래에 META-INF 폴더를 만들고 persistence.xml 파일을 만들어 persistence config 데이터를 명시해야함

## Unable to locate entity descriptor

mapping 정보가 없으면 에러가 발생함

객체에 @Entity 어노테이션을 추가하면 해결할 수 있음

## EntityDescriptor(TODO)

## Entity에서 default 생성자

없으니까 컴파일 에러가 발생함

https://www.baeldung.com/jpa-no-argument-constructor-entity-class

## Typed query 사용하는 것 추천

제너릭으로 entity를 컬렉션으로 다루기 쉽게 구현함