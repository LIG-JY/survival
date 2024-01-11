# D.I

## Contents

### Factory

#### Factory란?

Factory는 객체를 만드는 어떤 것을 말한다. 객체지향에서 [Factory Pattern](<https://en.wikipedia.org/wiki/Factory_(object-oriented_programming)>)을 참고하자.

#### Factory를 사용하면 어떤 좋은 점이 있나요?

#### 1. 단일 책임 원칙

객체를 생성하는 책임만 가진 객체(펙토리)를 만들면 어떤 객체가 필요할 때 객체를 직접 만들지 않고 펙토리가 만들어준 객체를 사용하기만 하면 된다.

따라서 펙토리는 객체를 만드는 책임만 지고 이를 사용하는 객체는 원래 관심사에만 집중할 수 있다.

#### 2. 싱글턴

객체를 모두 팩토리에서 얻는다면, 팩토리에서 객체를 관리함으로써 싱글턴처럼 쓸 수 있다. 따라서 매번 새로운 객체를 만들어서 생기는 문제점을 막을 수 있다.

##### 팩토리를 통해 객체를 싱글턴 처럼 사용하는 예시

```<Java>
public class Factory {
    private static PostRepository postRepository;

    // 싱글톤 패턴
    public static PostRepository postRepository() {
        if (postRepository == null) {
            return new PostRepository();
        }
        return postRepository;
    }
}
```

static 하게 Factory를 사용할 수 있도록 구현했다. Factory는 오직 하나의 PostRepository 객체를 유지하기 때문에 싱글톤 처럼 PostRepository를 사용할 수 있다.

> branch : week5/singleton-factory

#### 3. Pluggable

코드의 결합도를 줄일 수 있다. PostDAO 또는 PostRepository의 구현이 여럿이라면 이를 사용하는 객체에서 펙토리에만 의존하여 구현체를 바꿀 때 코드 수정량을 줄일 수 있다. 이런 성질을 **[Pluggable](https://martinfowler.com/eaaCatalog/plugin.html)** 이라고 부른다.

### Inversion of Control과 Dependency Injection

#### 제어의 역전

Hollywood Principle이라고도 부른다. 배우가 직접 영화사에 연락하는 것이 아니라 영화사에서 배우를 대신 불러주는 것이다.

> “Don't call us, we'll call you”.

IoC는 프레임워크의 공통적인 특징이라고 볼 수 있다. 그래서 마틴 파울러는 아래와 같이 IoC가 특별한 것이 아니라고 설명했다.

_“saying that these lightweight containers are special because they use **inversion of control** is like saying **my car is special because it has wheels**.”_

#### Dependency Injection

이 당시 Spring 등이 EJB를 비판하면서 POJO, IoC Container를 이야기했고 논쟁이 많았었다. 이 전쟁을 끝내기 위해 Martin Fowler가 새로운 용어를 제시하게 된다. 이게 바로 **Dependency Injection**이다.

예전에는 스프링 개발자들이 Property 주입(Setter Injection)을 많이 썼지만(Java Bean의 흔적 😵), 최근에는 생성자 주입을 선호한다. 특히 final 필드로 만들어서 쓰는 걸 강력히 권장하고 있다.

### BeanFactory

#### 스프링 IoC 컨테이너의 최상위 인터페이스 BeanFactory

- 인터페이스의 역할

  1. Bean Configuration으로 빈의 생성을 제어
  2. 빈 관리
  3. 빈 정의, 개체화, 연결(wiring)

- 객체지향 관점

  1. 빈에 대한 정보(configuration) 캡슐화
  2. 빈 생명 주기 캡슐화

- 효과

  1. 어플리케이션의 모듈의 결합도를 줄일 수 있다.
  2. 어플리케이션의 모듈의 응집도를 높일 수 있다.

The Spring BeanFactory is a central container that manages and controls the creation and configuration of beans in a Spring application.
It serves as the foundation for the Inversion of Control (IoC) container in Spring, allowing beans to be defined, instantiated, and wired together.
The BeanFactory encapsulates the bean definitions, handles their lifecycle, and supports various configuration options.
It is a key component for achieving loose coupling and promoting modularity within a Spring-based application.

#### BeanDefinitionRegistry

- 인터페이스 역할
  1. 빈 정의 등록
  2. 빈 정의 관리(수정, 삭제)

BeanFactory는 BeanDefinitionRegistry에서 정의한 빈의 구현체를 실제로 생성하고 제공하는 주체다. BeanDefinitionRegistry는 빈의 정의를 등록하고 관리하여 BeanFactory가 필요할 때 해당 빈을 생성할 수 있도록 해준다.

Interface in Spring responsible for registering and managing definitions of beans in an application.
It plays a central role in defining and registering beans, supporting Inversion of Control (IoC) by handling the configuration of application beans.

#### GenericBeanDefinition

스프링에서 빈을 코드나 외부 설정(configuration)에서 **선언적으로** 정의하는 클래스다. 아래와 같은 정보를 정의하게 된다. 빈의 메타데이터를 다룬다고 생각하면 된다.

BeanDefinitionRegistry의 구현체에서 GenericBeanDefinition의 구현체를 등록 및 관리한다.

#### Spring Bean

Spring이 관리하는 객체를 Bean이라고 부른다. 대개는 Spring Bean이라고 부른다.

SpringBeanFactory를 Test 해보자.

#### 실습 1: ObjectMapper을 BeanFactory로 부터 얻어보기

```<Java>
@Test
    @DisplayName("Spring IoC 컨테이너에서 ObjectMapper 객체를 가져오는 테스트")
    void getObjectMapper() {

        /* Given */
        // BeanFactory는 스프링 IoC 컨테이너의 최상위 인터페이스이다.
        // BeanFactory 인터페이스를 구현한 DefaultListableBeanFactory 클래스를 사용한다.
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // ObjectMapper 클래스를 Bean으로 등록한다.
        // BeanDefinition의 구현체 중 하나인 GenericBeanDefinition 클래스를 사용한다.
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

        // 빈 정의에 클래스를 지정한다.
        beanDefinition.setBeanClass(ObjectMapper.class);

        // 빈 정의를 빈 팩토리에 등록한다.
        beanFactory.registerBeanDefinition("objectMapper", beanDefinition);

        /* When */
        // 빈 팩토리에서 ObjectMapper 클래스의 인스턴스를 가져온다.
        ObjectMapper objectMapper = beanFactory.getBean("objectMapper", ObjectMapper.class);

        /* Then */
        // ObjectMapper 클래스의 인스턴스가 null이 아님을 검증한다. 정상적으로 생성됬는지 확인한다.
        assertThat(objectMapper).isNotNull();
    }
```

#### 실습 2: PostController을 BeanFactory로 부터 얻어오기 + IOC 컨테이너의 D.I 확인하기

우선 PostController 클래스가 생성자 주입을 받도록 수정하자.

> Could not autowire. No beans of 'PostService' type found.

이런 메세지가 뜨면 postService, PostListService를 빈으로 등록하게 어노테이션을 추가해주면 된다.

```<Java>
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostListService postListService;

    public PostController(PostService postService, PostListService postListService) {
        this.postService = postService;
        this.postListService = postListService;
    }

    ...
}
```

실습 1과 동일하게 테스트 코드를 작성하고 테스트를 돌려보자.

```<Java>
@Test
    @DisplayName("Spring IoC 컨테이너에서 PostController 객체를 가져오는 테스트")
    void getPostController() {
        /* Given */
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(PostController.class);
        beanFactory.registerBeanDefinition("postController", beanDefinition);
        /* When */
        PostController postController = beanFactory.getBean("postController", PostController.class);
        /* Then */
        assertThat(postController).isNotNull();
    }
```

> org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'postController': Failed to instantiate [com.gyo.api.rest.demo.controllers.PostController]: No default constructor found....

테스트가 실패하고 로그가 나오게 된다. 로그를 분석해보면 default 생성자를 찾을 수 없다고 나온다. 즉 PostController가 생성자 주입으로 인자를 2개 받도록 해놨는데, 테스트 코드에서 생성자의 인자로 아무것도 지정해주지 않아서, default 생성자를 통해 PostController을 생성하려고 시도한 것이다.

따라서 테스트 코드에서 생성자 주입으로 인자 2개를 받도록 코드를 수정하자.

```<Java>
@Test
    @DisplayName("Spring IoC 컨테이너에서 PostController 객체를 가져오는 테스트")
    void getPostController() {
        /* Given */
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(PostController.class);
        // 빈 정의에서 생성자에 전달할 인자를 지정한다.
        // 생성자로 전달할 인자는 PostService와 PostListService 개체 생성
        PostService postService = new PostService();
        PostListService postListService = new PostListService();
        // 생성자의 인자에 인덱스에 따라서 값 지정
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, postService);
        constructorArgumentValues.addIndexedArgumentValue(1, postListService);
        // 생성자의 인자로 추가
        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);

        beanFactory.registerBeanDefinition("postController", beanDefinition);
        /* When */
        PostController postController = beanFactory.getBean("postController", PostController.class);
        /* Then */
        assertThat(postController).isNotNull();
    }
```

> branch : week5/getBeanTest

#### JUnit 사용 시 주의사항

JUnit은 Spring Boot Test에 포함되어 있고, 이는 Spring Initializr로 프로젝트를 만들면 기본으로 추가된다.  
각 테스트 메서드는 서로 독립인 프로그램이라 어떤 공통 변수를 선언하고 여러 테스트에서 같은 변수에 접근하면 이상한 결과가 나올 수 있다. 이를 격리된다고 표현한다.

#### 펙토리에서 bean을 get할 때 2가지 방식

```<java>
PostController postController = (PostController) beanFactory.getBean("postController");

PostController postController = beanFactory.getBean("postController", PostController.class);
```

beanFactory는 내부에서 빈의 클래스 정보를 알고 있기 때문에 만약 빈의 실제 타입이 지정한 타입과 일치하지 않으면 런타임에 BeanNotOfRequiredTypeException을 발생시킨다. 전자의 경우 getBean의 return이 Object타입이라 명시적 형변환이 필요하다. 마찬가지로 실제 타입과 형변환에서 지정한 타입이 일치하지 않으면 런타임에 ClassCastException이 발생한다.

BeanNotOfRequiredTypeException이 더 구체적인 정보를 개발자에게 알려주며 이 예외를 catch해 적절한 조치를 취할 수 있다. 따라서 후자가 [타입 안정성](<https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/BeanFactory.html#getBean(java.lang.String,java.lang.Class)>)을 보장한다.

### ApplicationContext

스프링 애플리케이션 대부분은 최소한의 IoC Container인 BeanFactory를 넘어서, ApplicationContext를 사용할 때가 많다. 예전에는 Bean에 대한 정보를 XML 파일로 써줬는데, 최근에는 Java의 Annotation으로 처리한다.

여러 구현 중 선택하거나 값을 주입하는 건 선언적으로 소스코드 외부(XML 파일, 환경변수 등)를 활용하는 게 좋다.

Bean은 Java Config에서 @Bean 애너테이션을 써서 정의하거나, 해당 클래스에 @Component 애너테이션을 붙여주고 Scan한다.

#### 실습 1 : ApplicationContext에서 PostController bean 얻기

```<Java>
public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(DemoApplication.class);

        PostController postController = context.getBean("postController", PostController.class);

        System.out.println("-".repeat(80));
        System.out.println("postController = " + postController);

//        SpringApplication.run(DemoApplication.class, args);
    }
```

참고로 어플리케이션 실행하는 코드를 주석처리 하지 않으면 에러가 발생한다.

#### 실습 2 : @Configuration이 붙은 클래스에 빈 등록하기

@SpringBootApplication에는 @Configuration이 포함되어 있다. @Configuration은 Java Config를 사용한다는 말이다. Java Config를 사용하면 XML 파일 대신 자바 코드를 사용하여 스프링 빈을 설정할 수 있어서 컴파일 타임에 오류를 확인할 수 있고, 런타임 시에 동적으로 빈을 추가하거나 구성을 변경하는 등의 작업도 수행할 수 있다.

@SpringBootApllication 어노테이션이 붙은 클래스 내부에 @Bean을 Config 할 수 있다. CORS 할 때와 똑같다!

```<Java>
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(DemoApplication.class);

        PostController postController = context.getBean("postController", PostController.class);
        PostService postService = context.getBean("postService", PostService.class);
        PostListService postListService = context.getBean("postListService", PostListService.class);

        System.out.println("-".repeat(80));
        System.out.println("postController = " + postController);
        System.out.println("postListService = " + postListService);
        System.out.println("postService = " + postService);
        System.out.println("-".repeat(80));

//        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    PostService postService() {
        return new PostService(postRepository());
    }

    @Bean
    PostListService postListService() {
        return new PostListService(postRepository());
    }

    @Bean
    PostRepository postRepository() {
        System.out.println("*".repeat(80));
        System.out.println("PostRepository 생성");
        System.out.println("*".repeat(80));
        return new PostRepository();
    }

}
```

이런식으로 IOC Container가 관리할 수 있게, Bean을 등록할 수 있다.

ApplicationContext(BeanFactory)에서 싱글턴 처럼 관리해주기 때문에 PostRepository는 한 번 생성되었다.

기존의 만들어놓은 Repositroy, Service 클래스에 어노테이션 붙여도 동일하게 싱글턴 처럼 빈으로 관리한다.

> branch : week5/config-bean-register

물론 이렇게 bean으로 각자 등록하지 않고, 기존의 클래스에 @Service, @Repository같은 @Component를 포함한 어노테이션을 붙여도 등록된다. 아래 브랜치 코드 참고

> branch : week5/annotation-class

사실 위에 이렇게 빈 등록하고 빈 주입해주는 과정(IOC 컨테이너의 역할)이 @SpringBootApplication 어노테이션에 포함되어 있다. 아까 주석을 단 코드를 해제하면 된다. 중복 실행이라 오류가 떴었다.

```<Java>
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

## 참고

- factory

  > [Factory (object-oriented programming)](<https://en.wikipedia.org/wiki/Factory_(object-oriented_programming)>)  
  > [Plugin](https://martinfowler.com/eaaCatalog/plugin.html)  
  > [Singleton pattern](https://ko.wikipedia.org/wiki/%EC%8B%B1%EA%B8%80%ED%84%B4_%ED%8C%A8%ED%84%B4)

- IOC

  > [제어 반전](https://ko.wikipedia.org/wiki/제어_반전)  
  > [Inversion of Control](https://martinfowler.com/bliki/InversionOfControl.html)  
  > [Dependency Injection](https://github.com/ahastudio/til/blob/main/oop/dependency-injection.md)  
  > [Inversion of Control Containers and the Dependency Injection pattern](https://martinfowler.com/articles/injection.html)

- Bean Factory

  > [BeanFactory](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/BeanFactory.html)  
  > [BeanDefinitionRegistry](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/support/BeanDefinitionRegistry.html)  
  > [GenericBeanDefinition](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/support/GenericBeanDefinition.html)  
  > [Given-When-Then](https://github.com/ahastudio/til/blob/main/blog/2018/12-08-given-when-then.md)

- ApplicationContext

  > [ApplicationContext](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)

  > [AnnotationConfigApplicationContext](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/AnnotationConfigApplicationContext.html)
