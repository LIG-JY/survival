# Relationship Mapping

## 효과

데이터 모델 Entity의 관계를 객체 참조로 간단히 활용할 수 있게 해준다.

## 주의사항

오용하기 쉽다.

일반적으로는 DDD의 Aggregate를 구현하기 위해 `CascadeType.ALL`과 `orphanRemoval=true`를 함께 사용하는 게 강력히 권장된다.

