# 3주차 DTO & JSON & CORS

## 목표

1. JSON 이해하기
2. Java의 DTO 이해하기
3. CORS 문제 해결하기

F/E는 사용자의 입력을 받아 B/E에 요청하고, B/E는 요청을 처리해 응답한다. 이 응답을 F/E는 사용자에게 적절한 형태로 보여주게 되고, 사용자는 다시 다른 입력을 하게 된다.

웹 서비스에서는 위 프로세스를 반복하게 된다. 이 때 데이터를 다룰 때 JSON이란 포맷을 활용하려고 하고, Java에서는 DTO란 형태로 다루려고 한다.

서로 다른 도메인 끼리 통신할 때 생기는 CORS 문제를 해결하는 방법을 알아본다.

## 목차

1. [1. DTO](./dto.md)
2. [2. 직렬화](./serialization.md)
3. [3. Jackson ObjectMapper](./jackson-object-mapper.md)
4. [4. CORS](./cors.md)
5. [부록1. RMI](./rmi.md)

## keyword

### DTD

- DTO (Data Transfer Object) 란
  - 프로세스 간 통신(IPC, Inter-Process Communication)
- “무기력한 도메인 모델” 이란 그리고 안티 패턴인 이유
- 자바빈즈(JavaBeans)
- EJB(Enterprise JavaBeans)
- Java의 record
- DAO
- ORM
