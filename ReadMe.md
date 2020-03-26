--------------------
# Java Spring
--------------------

# 목차

- [1. 스프링 IoC 컨테이너와 빈](#스프링-IoC-컨테이너와-빈)
    - [1. 스프링 IoC 컨테이너](#스프링-IoC-컨테이너)
    - [2. Bean](#Bean)
- [2. ApplicationContext와 다양한 빈 설정 방법](#ApplicationContext와-다양한-빈-설정-방법)
    - [1. Bean 설정파일을 XML 코드로 만들기](#Bean-설정파일을-XML-코드로-만들기)
    - [2. Bean 설정파일을 Java 코드로 만들기](#Bean-설정파일을-Java-코드로-만들기)
    - [3. Java 코드로만든 Bean 설정파일 자동등록](#Java-코드로만든-Bean-설정파일-자동등록)
- [3. @Autowire](#@Autowire)
    - [1. Bean의 타입이 여러개인 경우](#Bean의-타입이-여러개인-경우)
        - [1. @Primary](#@Primary)
        - [2. @Qualifier](#@Qualifier)
        - [3. 해당 타입의 빈 모두 주입 받기](#해당-타입의-빈-모두-주입-받기)
        - [4. 주입받는 Bean 이름을 동일하게 선언](#주입받는-Bean-이름을-동일하게-선언)
        - [5. 동작 원리](#동작-원리)
- [4. @Component와 컴포넌트 스캔](#@Component와-컴포넌트-스캔)
    - [1. Filter 예외](#Filter-예외)
    - [2. Functional Bean 등록](#Functional-Bean-등록)
    - [3. @ComponentScan 동작 원리](#@ComponentScan-동작-원리)
        - [1. BeanFactoryPostProcessor](#BeanFactoryPostProcessor)
- [5. 빈의 스코프](#빈의-스코프)
    - [1. 싱글톤 스코프](#싱글톤-스코프)
    - [2. 프로토타입](#프로토타입)
        - [1. proxyMode](#proxyMode)
        - [2. 코드로 변경](#코드로-변경)
- [6. Environment 1부 프로파일](#Environment-1부-프로파일)
    - [1. 프로파일이란?](#프로파일이란?)
    - [2. @Profile](#@Profile)
- [7. MessageSource](#MessageSource)
    - [1. 릴로딩 기능이 있는 메시지 소스 사용하기](#릴로딩-기능이-있는-메시지-소스-사용하기)
- [8. ApplicationEventPublisher](#ApplicationEventPublisher)
    - [1. Spring 4.2 이전](#Spring-4.2-이전)
    - [2. Spring 4.2 이후](#Spring-4.2-이후)
    - [3. 실행 순서를 줘여하는 경우](#실행-순서를-줘여하는-경우)
    - [4. 비동기](#비동기)
    - [5. 스프링이 제공하는 기본 이벤트](#스프링이-제공하는-기본-이벤트)
- [9. ResourceLoader](#ResourceLoader)
- [10. Resource 추상화](#Resource-추상화)
    - [1. Class Type 검증](#Class-Type-검증)
- [11. Validation(검증) 추상화](#Validation(검증)-추상화)
    - [1. 스프링 부트 2.0.5 이상 버전을 사용할 때](#스프링-부트-2.0.5-이상-버전을-사용할-때)
- [12. PropertyEditor](#PropertyEditor)
- [13. Converter와 Formatter](#Converter와-Formatter)
    - [1. Converter](#Converter)
    - [2. Formatter](#Formatter)
    - [3. ConversionService](#ConversionService)
    - [4. 스프링 부트](#스프링-부트)
    - [5. 등록된 Converter 확인하는 방법](#등록된-Converter-확인하는-방법)
- [14. SpEL (스프링 Expression Language)](#SpEL-(스프링-Expression-Language))
    - [1. 실제로 어디서 쓰나?](#실제로-어디서-쓰나?)
- [15. 스프링 AOP](#스프링-AOP)
- [16. 프록시 기반 AOP](#프록시-기반-AOP)
- [17. @AOP](#AOP)
     - [1. 특정 class 메소드를 적용시키는 경우](#특정-class-메소드를-적용시키는-경우)
     - [2. 특정 메소드에만 적용 시키는 경우](#특정-메소드에만-적용-시키는-경우)
- [18. Null-safety](#Null-safety)

# 스프링 IoC 컨테이너와 빈

Inversion of Control: 의존 관계 주입(Dependency Injection)이라고도 하며, 어떤 객체가
사용하는 의존 객체를 `직접 만들어 사용하는게 아니라, 주입 받아 사용하는 방법`을 말 함.

의존성 추가

~~~
compile group: 'org.springframework', name: 'spring', version: '2.5.6'
compile group: 'javax.annotation', name: 'javax.annotation-api', version: '1.3.2'
~~~

~~~
@Service
public class BookService {
    public BookService() {}

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
~~~

BookService.class 가 bookRepository 를 new 로 객체를 (인스턴스)만들어서 사용하는게 아니라
생성자로 주입을 받아서 사용하는 방법을 IOC 스프링이 없이도 구현이 가능합니다.

~~~
public class BookServiceTest {
    @Test
    public void test() {
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository);
    }
}
~~~

하지만 IOC 컨테이너를 사용하는 이유는 여러 개발자들이 스프링 커뮤니티에서 논의 해서 만들어낸 
여러가지 Dependency Injection(의존성 주입) 방법과 모범 사례들의 노하우가 쌓여있는 프레임 워크이기 때문입니다.
스프링 초창기에는 주로 XML로 설정하였지만 개발자들의 의견을 수용하여 구글 주스가 처음 선보인 에노테이션 기반의 DI를 지원하기 시작했습니다.

스프링 Bean 사용

~~~
@Repository
public class BookRepository { }

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
}
~~~

Bean으로 등록되어있는 포조 클래스 BookRepository를 BookService 에서 @Autowired 사용하여 쉽게 주입을 받아옵니다.

## DI(Dependency Injection) 의존성

- 의존성

~~~
public class Cook {

    private Knife knife;

    public Cook() {
        knife = new Knife();
    }

    public void cook(Food food) {
        knife.cut(food);
    }
}
~~~

`Cook(요리사) 클래스`는 cook()메소드를 실행 할 때에 `cut()을 위해서 Knife 클래스`가 필요하다 
이러한 상황을 `Cook 클래스가 Knife에 의존성을 가지고 있다`(의존하고 있다) 라고한다. 
의존성에 의한 문제점들은 아래와 같다

Cook 클래스는 생성자에서 `Knife객체를 직접 생성`하여 사용하고 있다. 
만약 나중에 Knife라는 클래스가 Rose Knife(장미칼) 등으로 바뀐다면 지금 처럼 `Knife 클래스에 의존하고 있는 Cook의 클래스도 직접 같이 변경` 해주어야 한다. 
즉, `객체간의 강한 결합력`이 생긴다. 소프트웨어 모듈화의 목적인 낮은 결합력과 높은 응집도에 해가 가는 행위가 되는 것이다.

- 의존성 주입(Spring 사용 전)

의존성을 주입하지 않는 경우는 위와 같이 직접 필요한 객체를 생성하는 경우라고 보면 된다. 
의존성 주입을 하는 경우는 다음과 같은 경우가 있다.
생성자를 통해서 주입받는 경우

~~~
public class Cook {

    private Knife knife;

    public Cook(Knife knife) {
        this.knife = knife;
    }
}
~~~

그럼 위와 같은 방법을 사용하면 코드의 수정이 용이 하다는 것이다. 
앞서 말했듯이 Knife 클래스를 RoseKnife 클래스로 변경을 해야하는 상황일 때. 
의존성을 주입하지 않는다면 `Cook 클래스의 직접 Knife를 생성하는 부분을 바꾸어주어야 한다. `
의존성을 주입하는 상황에서도 주입하는 코드를 변경해주어야 하는것은 똑같지만 `한 클래스를 수정했을 때 다른 클래스를 수정해야 하는 상황을 막아준다.`

- DI(Spring 사용)

위와 같은 이유로 `스프링은 DI라는 방식을 이용하여 모듈간의 결합도를 낮추어준다.`
DI란 `IOC Container 가 개발자 대신 xml 파일에 정의된 대로 Bean 객체를 생성하고 의존성을 대신 주입` 하는 것을 의미한다. 
IOC 란 제어의 역전 이라는 말로 사용자가 직접 객체를 생성하고 관리하던 것을 `spring의 IOC Container가 대신` 해준다는 말이다.

## 스프링 IoC 컨테이너

[BeanFactory](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/beans/factory/BeanFactory.html)

- 스프링 IOC 컨테이너의 가장 핵심적인 최상위 인터페이스는 BeanFactory 
- 애플리케이션 컴포넌트의 중앙 저장소.
- 빈 설정 소스로 부터 빈 정의를 읽어들이고, 빈을 구성하고 제공한다.

## Bean

- 스프링 `IoC 컨테이너가 관리 하는 객체.`
- 장점
    - 의존성 관리
    - 스코프
        - 싱글톤: 하나
        - 프로포토타입: 매번 다른 객체
    -  라이프사이클 인터페이스

IOC 의존성 주입을 받으려면 Bean으로 등록이 되어있어야 합니다.
스프링 IOC Bean들은 기본적으로 싱글톤 스코프로 Bean으로 등록이 됩니다.

스프링에서 주입받아 사용하는 인스턴스들은 항상 같은 객체이므로 메모리 성능면에서 효율적입니다.

라이프사이클 인터페이스이란 어떠한 Bean이 생성이 될때 추가적인 작업이 필요할때 @PostConstruct 활용합니다.

에플리케이션 구동중에 활동합니다.

~~~
@Service
public class BookService {
    public BookService() {
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("==========");
        System.out.println("Hello~!");
        System.out.println("==========");
    }
}
~~~

# ApplicationContext와 다양한 빈 설정 방법

~~~
public class BookRepository {
}

public class BookService {

    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
~~~

BookRepository 와 BookService를 Bean으로 등록을 하겠습니다.

## Bean 설정파일을 XML 코드로 만들기

고전적인 스프링 Bean 설정

> /resources/application.xml

~~~
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean
            id="bookService"
            class="me.whiteship.BookService"
            scope="singleton"
    >
        <property name="bookRepository" ref="bookRepository"></property>
    </bean>
    <bean
            id="bookRepository"
            class="me.whiteship.BookRepository"
    ></bean>

    class : Bean의 타입
    scop  : singleton 만 싱글톤 나머지는 프로토타입
            prototype 매번 새로운 객체를 만듭니다.
            request 마다 새로운 객체를 만듭니다.
            session http 마다 객체를 만듭니다.
</beans>
~~~

bookService Bean위치에 bookRepository `property 를 추가하므로서 생성자로 주입`을 받을 수 있습니다.

property name="bookRepository" 에서 `name` 은 bookRepository의 `setter에서 참조`한것입니다.
`ref` 는 다른 Bean을 참조한다는 의미로 사용됩니다.

Bean 생산이 완료되었다면 ApplicationContext를 만들어서 사용하면 됩니다.

~~~
public class Application {
    public static void main(String[] args) {
        ApplicationContext appC                = new ClassPathXmlApplicationContext("application.xml");

        // 등록된 bean의 정보 목록
        String[]           beanDefinitionNames = appC.getBeanDefinitionNames();
        System.out.println("beanDefinitionNames : " + Arrays.toString(beanDefinitionNames));

        // 등록된 Bean을 선택하여 가져올 수 있습니다.
        // bookService 클래스에 bookRepository 생성자가 주입이 되었는지 확인합니다.
        // 값이 존재하므로 me.whiteship.BookRepository@273e7444 출력합니다.
        // 만약 property 주석처리되어 실행하면 Null 을 출력하게 됩니다.
        BookService bookService = (BookService) appC.getBean("bookService");
        System.out.println("getBean : " + bookService.bookRepository);
    }
}
~~~

xml로 설정하여 Bean 의존성 관리의 단점은 직접 Bean을 서로 연관되어 작성하는게 번거롭다는 것입니다.

그래서 등장한것이 Spring 2.5v 추가된 context:component-scan 입니다.

~~~
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <context:component-scan base-package="me.whiteship"/>

</beans>
~~~

base-package 설정된 경로부터 Bean으로 등록된 클래스를 탐색하여 Bean을 등록을 합니다.

@Component를 확장하는 에노테이션 @Service, @Repository 등등 을 탐색하여 등록합니다.

~~~
@Service
public class BookService {  
    @Autowired
    public BookRepository bookRepository;

    ...
}

@Repository
public class BookRepository { ... }
~~~

component-scan으로 Bean으로는 등록이 되지만 의존성 주입은 아직 안됩니다.
Bean으로 등록된 클래스를 의존성 주입을 받으려면 대표적으로 @Autowired 에노테이션을 사용합니다.

## Bean 설정파일을 Java 코드로 만들기

~~~
// Bean 설정파일이라는 것을 알려주는 에노테이션
@Configuration
public class ApplcationConfig {

    /*
     * Bean:메소드 name  : bookRepository
     * Bean:메소드의 type : BookRepository
     * Bean:메소드의 class: new BookRepository()
     * */

    @Bean
    BookRepository bookRepository() {
        return new BookRepository();
    }

    @Bean
    BookService bookService() {
        BookService bookService = new BookService();
        bookService.setBookRepository(bookRepository()); // 메소드를 호출해서 의존성 주입을 직접 해줄 수 있습니다.
        return bookService;
    }

    @Bean
    BookService bookService(BookRepository bookRepository) {
        BookService bookService = new BookService();
        bookService.setBookRepository(bookRepository);  // 메소드 파라미터로 주입을 받아서 의존성을 주입받을 수 있습니다.
        return bookService;
    }
}

public class BookService {
    public BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}

public class BookRepository { ... }

public class Application {
    public static void main(String[] args) {
        ApplicationContext appC = new AnnotationConfigApplicationContext(ApplcationConfig.class);
        ...
    }
}

결과 -

beanDefinitionNames : [org.springframework.context.annotation.internalConfigurationAnnotationProcessor, org.springframework.context.annotation.internalAutowiredAnnotationProcessor, org.springframework.context.annotation.internalCommonAnnotationProcessor, org.springframework.context.event.internalEventListenerProcessor, org.springframework.context.event.internalEventListenerFactory, applcationConfig, bookRepository, bookService]
getBean : me.whiteship.BookRepository@4bdeaabb
~~~

ApplcationConfig.class 를 Bean 설정으로 사용하도록 ApplicationContext 등록합니다.
동작은 xml하고 같습니다.

ApplcationConfig에서 의존성을 직접 주입하지않고 사용하려면 @Autowired 를 사용하면 됩니다.

## Java 코드로만든 Bean 설정파일 자동등록

~~~
@Configuration
@ComponentScan(basePackageClasses = Application.class)
public class ApplcationConfig { }

@Service
public class BookService {
    @Autowired
    public BookRepository bookRepository;
    ...
}

@Repository
public class BookRepository { }
~~~

@ComponentScan(basePackageClasses = Application.class)

Application.class 위치한 곳부터 탐색을 하라는 코드
즉 모든 클래스에 붙어있는 에노테이션을 찾아서 Bean 등록을 합니다. 

Spring Boot의 경우 이를 통으로 넣은 에노테이션이 `@SpringBootApplication` 입니다. 

# @Autowire

Bean으로 등록되지 않은 BookRepository.class 를 BookService.class 에서 Bean으로 주입받아 사용해보겠습니다.

~~~
@Service
public class BookService { }

public class BookRepository { }
~~~

1. 생성자를 사용하여 주입받기

~~~
@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}

결과 -

Description:
Parameter 0 of constructor in me.whiteship.BookService required a bean of type 'me.whiteship.BookRepository' that could not be found.

Action:
Consider defining a bean of type 'me.whiteship.BookRepository' in your configuration.
~~~

BookRepository의 Bean이 등록이 되어있지 않아서 다음 에러가 발생합니다.
BookRepository를 Bean으로 등록하라는 에러 메세지입니다.
BookRepository.class에 @Repository 에노테이션을 추가하여 Bean으로 등록하면 정상 작동합니다.

2. Setter를 사용하여 주입받기

~~~
@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
~~~

생성자를 사용하여 주입받는 방법과 똑같은 에러가 발생합니다.

이를 통해서 알수있는 사실은 
1번의 생성자 주입은 Bean을 만들다가 Bean에 필요한 다른 의존성에 해당하는 bookRepository Bean을 몾찾아서 오류가 발생했습니다.

2번의 경우 단순이 Setter였으니 적어도 BookService 인스턴스는 생성 가능해야 하는게 아닐까 라는 생각이들 수 도 있습니다.
BookService.class 자체는 인스턴스화가 가능하지만 BookService Bean을 생성할때
@Autowired 에노테이션이 존재해서 setBookRepository의 의존성을 주입을 하려고 시도를 합니다.
하지만 setBookRepository의 bookRepository Bean을 못찾아 실패하게 되는겁니다.

이런경우 setBookRepository의 bookRepository 의존성이 옵셔널이다 하면
@Autowired(required = false) 설정을 추가하면 Bean이 존재하지 않으면 건너띄게 됩니다. 

생성자를 사용한 의존성 주입은 해당 Bean을 만들때에도 개입이 됩니다.
생성자의 주입을 필수로 받아야 하기 때문입니다.

setter나 필드 의존성 주입을 사용할때는 해당 클래스 생산에 필수조건이 아니므로
옵셔널 설정을해서 생성 가능합니다.

## Bean의 타입이 여러개인 경우

~~~
public interface BookRepository { }

@Repository
public class MyBookRepository implements BookRepository { }

@Repository
public class JjunproBookRepository implements BookRepository { }

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
}

결과 - 

Description:
Field bookRepository in me.whiteship.BookService required a single bean, but 2 were found:
	- jjunproBookRepository: defined in file [/Users/kimminseok/git-repository/Java-spring/Autowire/project/build/classes/java/main/me/whiteship/JjunproBookRepository.class]
	- myBookRepository: defined in file [/Users/kimminseok/git-repository/Java-spring/Autowire/project/build/classes/java/main/me/whiteship/MyBookRepository.class]

Action:
Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
~~~

BookService.class 에 주입해야하는 bookRepository.class Bean 타입이 2개가 존재합니다.
둘중 하나만 등록하여 사용하라는 경고문입니다.

- 해결방법
    - @Primary
    - @Qualifier (빈 이름으로 주입)
    - 해당 타입의 빈 모두 주입 받기
    - 주입받는 Bean 이름을 동일하게 선언

### @Primary

~~~
@Repository @Primary
public class MyBookRepository implements BookRepository { }

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    // bookRepository Bean을 주입받았는지 확인하는 메소드
    public void printBookRepository() {
        System.out.println(bookRepository.getClass());
    }
}

@Component
public class BookServiceRunner implements ApplicationRunner {
    @Autowired
    BookService bookService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bookService.printBookRepository();
    }
}

결과 - 

me.whiteship.MyBookRepository@ec0c838
~~~

여러개의 Repository 중에서 @Primary 붙은 클래스를 사용하겠다고 선언

BookService.class 에 bookRepository.class Bean이 주입이 되었는지 확인하는 메소드를 만들어 확인했습니다.
결과는 정상적으로 Bean이 주입이 되었습니다.

### @Qualifier

~~~
@Service
public class BookService {
    @Autowired
    @Qualifier("myBookRepository")
    private BookRepository bookRepository;

    public void printBookRepository() {
        System.out.println(bookRepository.getClass());
    }
}
~~~

### 해당 타입의 빈 모두 주입 받기

~~~
@Service
public class BookService {
    @Autowired
    private List<BookRepository> bookRepositorys;

    public void printBookRepository() {
        this.bookRepositorys.forEach(System.out::println);
    }
}

결과 -

me.whiteship.JjunproBookRepository@2a65bb85
me.whiteship.MyBookRepository@4b85880b
~~~

### 주입받는 Bean 이름을 동일하게 선언

~~~
@Service
public class BookService {
    @Autowired
    private BookRepository myBookRepository;

    public void printBookRepository() {
        System.out.println(myBookRepository.getClass());
    }
}
~~~

### 동작 원리

- BeanPostProcessor
    - 새로 만든 빈 인스턴스를 수정할 수 있는 라이프 사이클 인터페이스
- AutowiredAnnotationBeanPostProcessor extends - - BeanPostProcessor
    - 스프링이 제공하는 @Autowired와 @Value 애노테이션 그리고 JSR-330의
        - @Inject 애노테이션을 지원하는 애노테이션 처리기.
- Bean 인스턴스가 만들어진 다음에 추가적인 작업을 하는 라이프싸이클
    - 인터페이스 기반
        - InitializingBean
    - 에노테이션 기반
        - @PostConstruct



BeanPostProcessor 라이브 싸이클 구현체에 의해서 동작하는 방식

Bean을 만든 후에 실행되는 라이브싸이클이 BeanPostProcessor 입니다.

AutowiredAnnotationBeanPostProcessor 추가적으로 작동을하면서 @Autowired 에노테이션을 처리를 해줍니다.
위 처리과정은 @PostConstruct 에노테이션 이전에 작동한 기능이므로
@PostConstruct 에서 주입받은 Bean을 사용할 수 있습니다.

~~~
@Service
public class BookService {

    @Autowired
    private BookRepository myBookRepository;

    @PostConstruct
    public void setup() {
        System.out.println(myBookRepository.getClass());
    }
}
~~~

ApplicationRunner 의 경우에는 애플리케이션이 전부 구동이 끝났을때 등록이 되는 반면 

@PostConstruct 어노테이션 콜백같은 경우 에플리케이션 구동중에 실행이 됩니다.

BeanPostProcessor 어떻게 동작하는가 
BeanFactory가 BeanPostProcessor타임의 Bean을 탐색합니다.
그중 하나가 AutowiredAnnotationBeanPostProcessor 가 등록이 되어 있습니다.

다른 일반적인 Bean들을한테 AutowiredAnnotationBeanPostProcessor 를 적용을 시킵니다.

AutowiredAnnotationBeanPostProcessor Bean으로 등록된 사실을 알고싶다면

~~~
@Component
public class BookServiceRunner implements ApplicationRunner {
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AutowiredAnnotationBeanPostProcessor beanPostProcessor = applicationContext.getBean(AutowiredAnnotationBeanPostProcessor.class);
        System.out.println(beanPostProcessor);
    }
}
~~~

# @Component와 컴포넌트 스캔

- 컨포넌트 스캔 주요 기능
    - 스캔 위치 설정
    - 필터: 어떤 애노테이션을 스캔 할지 또는 하지 않을지
- @Component
    - @Repository
    - @Service
    - @Controller
    - @Configuration

ComponentScan.class 로 인해서 컴포넌트가 등록이 됩니다.
가장 중요한 설정이 basePackages 입니다.

basePackages는 문자열로 package를 저장합니다.
이는 type-safe하지 않으므로 basePackageClasses 속성으로 type-safe 설정을 해줍니다.

basePackageClasses값에 전달된 해당 클래스 기준으로 ComponentScan을 시작합니다.

## Filter 예외

ComponentScan 동작시 제외시키는 설정

~~~
@ComponentScan(
    excludeFilters = { 
        @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) 
    }
)
~~~

## Functional Bean 등록

~~~
@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(Application.class);
        // 애플리케이션 추가 동작 메소드
        app.addInitializers((ApplicationContextInitializer<GenericApplicationContext>) act -> {
            // registerBean 통해서 직접 Bean을 등록할 수 있습니다.
            act.registerBean(SampleRunner.class);
            // Supplier 는 등록되는 Bean 타입의 인스턴스를 제공해줘야 합니다.
            // ApplicationRunner를 만들고 간단하게 메세지를 띄웁니다.
            act.registerBean(
                    SampleRunner.class,
                    (Supplier<ApplicationRunner>) () -> args1 -> System.out.println("Functional Bean !!")
            );
        });
        // 이렇게 설정하여 애플리케이션을 구동할 경우 장점은 여러가지 예를들어 (조건문, 포문..)코딩을 사용하여 커스텀 할 수 있습니다.
        // 왜냐하면 리플렉션이나, 씨지라이브러리 프록시 같은것을 사용안하니 애플리케이션 구동시 속도 성능상에 이점이 있습니다.

        // 애플리케이션 구동
        app.run(args);
    }
}
~~~

리플렉션 https://docs.oracle.com/javase/tutorial/reflect/index.html

프록시 https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html

리플렉션은 보통 동적으로 특정 클래스를 로딩하거나, 클래스의 필드나 메소드를 알아내고 값을 읽어오거나 메소드를 실행할 수 있는 기능을 말합니다.

@ComponentScan을 대신하여 Functional Bean 등록하여 사용하기에는 너무 많은 작업시간이 소요되므로 추천하지 않는 방법

## @ComponentScan 동작 원리

- @ComponentScan은 스캔할 패키지와 애노테이션에 대한 정보
- 실제 스캐닝은 ConfigurationClassPostProcessor라는 BeanFactoryPostProcessor에
의해 처리 됨.

[ConfigurationClassPostProcessor](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/ConfigurationClassPostProcessor.html)
[BeanFactoryPostProcessor](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/config/BeanFactoryPostProcessor.html)


### BeanFactoryPostProcessor

BeanPostProcessor 와 비슷하지만 실행되는 시점이 다릅니다.
다른 모든 Bean들을 만들기 이전에 적용을 해줍니다. 
다른 Bean들은 개발자가 직접 등록하는 모든 Bean들도 포함이 되어있습니다.

# 빈의 스코프

개발자가 등록한 모든 Bean들은 `스코프라는것이 존재`합니다.
그중에서 `싱글톤 스코프`만 사용했습니다.
왜냐하면 아무런 설정이 없다면 `초기값으로 싱글톤이 설정`됩니다.

## 싱글톤 스코프

애플리케이션 전반에 걸쳐서 해당 `Bean에 인스턴스가 오직 하나만 존재`하는것

모든 싱글톤 스코프 Bean들은 기본값이 ApplicationContext생성할때 만들게 되어있습니다.
그러므로 애플리케이션 구동할때 시간이 조금더 소요될 수 있습니다.

간단한 에제

~~~
@Component
public class Proto { }

@Component
public class Single {
    @Autowired
    private Proto proto;

    public Proto getProto() {
        return proto;
    }
}

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private Single single;

    @Autowired
    private Proto proto;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(proto);
        System.out.println(single.getProto());

        /*
        * - 싱글톤 -
        * 첫번째 출력하는 proto는 AppRunner가 받아온 Proto입니다.
        * 두번째 출력하는 proto는 Single이 참조한 Proto
        * 둘의 Proto는 같은 인스턴스를 사용합니다.
        * 해당 Bean의 인스턴스 하나만 사용합니다.
        * */
    }
}
~~~

경우에 따라서는 프로토타입 Request, Session, WebSocket... 여러가지를 사용할 수 도 있습니다.

## 프로토타입

매번 새로운 객체 인스턴스를 만들어 사용하는것 

간단한 예제

~~~
@Component
@Scope("prototype")
public class Proto { }

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext act;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        * - 프로토타입 -
        * */
        System.out.println("ProtoType");
        System.out.println(act.getBean(Proto.class));
        System.out.println(act.getBean(Proto.class));
        System.out.println(act.getBean(Proto.class));

        System.out.println("SingleType");
        System.out.println(act.getBean(Single.class));
        System.out.println(act.getBean(Single.class));
        System.out.println(act.getBean(Single.class));
    }
}

결과 - 

ProtoType
me.whiteship.Proto@1e469dfd
me.whiteship.Proto@554f0dfb
me.whiteship.Proto@1f7076bc
SingleType
me.whiteship.Single@71904469
me.whiteship.Single@71904469
me.whiteship.Single@71904469
~~~

@Scope("prototype") 선언으로 Proto Bean을 받아올때마다 새로운 인스턴스가 생성됩니다.

### proxyMode

프로토타입 Bean이 싱글톤 Bean을 참조해서 사용하면 문제가 발생하지 않습니다.

하지만 싱글톤 Bean이 프로토타입 Bean을 사용할경우 
싱글톤 Bean은 인스턴스가 한번만 생성됩니다.
참조하고있는 프로토타입 Bean은 이미 셋팅이 되어 있습니다. 그렇기 때문에
실글톤 Bean을 계속해서 사용할 경우 프로토타입 Bean은 변경되지 않는 문제가 발생합니다.

~~~
...
System.out.println("ProtoType By SingleType");
System.out.println(act.getBean(Single.class).getProto());
System.out.println(act.getBean(Single.class).getProto());
System.out.println(act.getBean(Single.class).getProto());
...

결과 - 

ProtoType By SingleType
me.whiteship.Proto@5bbbdd4b
me.whiteship.Proto@5bbbdd4b
me.whiteship.Proto@5bbbdd4b
~~~

이 문제를 해결하려면 proxyMode를 사용하는겁니다.

~~~
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Proto { }
~~~

Proto를 프록시 기반의 클래스로 감싸도록 설정합니다.
왜 프로시로 감싸도록 한것인가?

Single 스코프가 Prototype 스코프의 Bean을 직접참조하면 안 되기 때문입니다.
프록시를 거쳐서 참조하도록 해야합니다.
직접 쓰면 바꿔줄 상황이 되도록 프록시를 감싸주는것입니다.

ScopedProxyMode.TARGET_CLASS -> 클래스 기반의 프록시를 만들어서 씨지라이브러리 기반의 클래스를 상속받은 프록시를 만들도록 알려줍니다.

### 코드로 변경

~~~
@Component
@Scope(value = "prototype")
public class Proto { }

@Component
public class Single {
    @Autowired
    private ObjectProvider<Proto> proto;

    public Proto getProto() {
        return proto.getIfAvailable();
    }
}

결과 - 

ProtoType By SingleType
me.whiteship.Proto@d71adc2
me.whiteship.Proto@1a1d3c1a
me.whiteship.Proto@24528a25
~~~

# Environment 1부 프로파일

~~~
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
            ...
        }
~~~

ApplicationContext 인터페이스는 수많은 인터페이스를 상속받고 있습니다.
그 중에서 Environment 프로파일을 살펴보겠습니다.

## 프로파일이란?

환경설정에서 Bean들의 묶음입니다.
각각에 환경에 따라서 다른 Bean들을 사용해야 하는 경우
또는 특정 환경에서만 어떠한 Bean을 등록해야 하는 경우

~~~
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext act;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Environment environment = act.getEnvironment();
        
        // 현재 액티브중인 프로파일들이 무엇인지 나열하는 메소드
        System.out.println(Arrays.asList(environment.getActiveProfiles()));
    }
}

public interface EnvironmentCapable {
	Environment getEnvironment();
}
~~~

act.getEnvironment() 는 EnvironmentCapable 에서 가져온것입니다.
ApplicationContext가 EnvironmentCapable를 상속 받았기 때문에 Environment를 가져와서 사용할 수 있습니다.

## @Profile

~~~
public interface BookRepository { }

public class TestBookRepository implements BookRepository { }

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    BookRepository bookRepository() {
        return new TestBookRepository();
    }
}
~~~

@Profile("test")인 경우에만 Bean설정이되는 클래스입니다.

- 프로파일 설정하기
    - Vm option -> -Dspring.profiles.avtive=”test,A,B,...”
    - @ActiveProfiles (테스트용)
    
# Environment 2부 프로퍼티

계층형 구조 hierarchical 입니다. 
동일한 설정이 여러 곳에 있어도 계층형 구조라서 우선 순위가 높은게 사용 된다는 설명입니다.

~~~
VM Option -> -Dapp.name=spring5

Environment environment = act.getEnvironment();
System.out.println(environment.getProperty("app.name"));
~~~

## properties

~~~
app.name=properties

@SpringBootApplication
@PropertySource("classpath:/app.properties")
public class Application { ... }

or - 

@Value("{app.name}")
String appName;
~~~

# MessageSource

ApplicationContext 내부에 MessageSource를 상속받고 있으므로
직접 MessageSource를 주입받아 사용할 수 있습니다.

~~~
messages_en.properties {
    hello=hello, {0}
}

messages.properties {
    hello=안녕, {0}
}
~~~

별다른 설정을 하지 않아도 SpringBootApplication은 `messages` 로 시작하는 프로퍼티스들을  메세지 소스로 읽어줍니다.

~~~
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    MessageSource messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String hello = messageSource.getMessage(
                "hello",
                new String[]{ "jjunpro" },
                Locale.KOREA
        );
        System.out.println(hello);

        String helloDefault = messageSource.getMessage(
                "hello",
                new String[]{ "jjunpro" },
                Locale.getDefault()
        );
        System.out.println(helloDefault);
    }
}
~~~

만약 둘다 출력이 동일하게 출력된다면? 

안녕, jjunpro<br/>
안녕, jjunpro

default 로케일이 한국이라서 한글 메시지가 찍히는겁니다. 
만약 개발 환경 default가 영문이면 영문 프로퍼티가 출력됩니다. 

## 릴로딩 기능이 있는 메시지 소스 사용하기

~~~
...
@Autowired
MessageSource messageSource;

@Override
public void run(ApplicationArguments args) throws Exception {
    String hello = messageSource.getMessage(
            "hello",
            new String[]{ "jjunpro" },
            Locale.ENGLISH
    );
    String helloDefault = messageSource.getMessage(
            "hello",
            new String[]{ "jjunpro" },
            Locale.getDefault()
    );

    // 1초마다 출력을 합니다.
    while (true) {
        System.out.println(hello);
        System.out.println(helloDefault);
        Thread.sleep(2000l);
    }
}
...

@SpringBootApplication
public class Application {
    ...
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");

        // 3초 캐싱 후 다시 읽도록합니다.
        messageSource.setCacheSeconds(3000);
        return messageSource;
    }
}
~~~

메세지 프로퍼티를 수정 후 다시 빌드하면 실시간으로 변경된 소스가 출력됩니다.

# ApplicationEventPublisher

옵져버 패턴의 구현체로 이벤트 기반의 프로그래밍을 할때 유용한 인터페이스입니다.

## Spring 4.2 이전

~~~
// Bean으로 등록되지 않는 이벤트입니다.
public class MyEvent extends ApplicationEvent {
    private int data;

    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source, int data) {
        super(source);
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
~~~

int data를 담아서 전송하는 이벤트 생성자를 통해서 만들어 전송합니다.

~~~
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventPublisher.publishEvent(new MyEvent(this, 100));
    }
}
~~~

ApplicationEventPublisher publishEvent 메소드를 통해서 이벤트를 실행할 수 있습니다.

해당 이벤트를 받아서 처리하는 이벤트 핸들러는 Bean으로 등록이 되어야합니다.

~~~
@Component
public class MyEventHandler implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        int data = event.getData();
        System.out.println("이벤트를 받았다. 데이터 : " + data);
    }
}

결과 -

이벤트를 받았다. 데이터 : 100
~~~

## Spring 4.2 이후

더이상 이벤트가 ApplicationEvent를 상속받을 필요가 없어졌습니다.

~~~
// Bean으로 등록되지 않는 이벤트입니다.

// Spring 4.2 이전 버전에는 ApplicationEvent 상속이 필수였습니다.
// public class MyEvent extends ApplicationEvent {

public class MyEvent {
    private Object source;
    private int    data;

    /*
    spring 4.2 이전 버전
    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source, int data) {
        super(source);
        this.data = data;
    }
    */

    public MyEvent(Object source, int data) {
        this.source = source;
        this.data = data;
    }

    public Object getSource() {
        return source;
    }

    public int getData() {
        return data;
    }
}
~~~

Object를 자기자신이 가지고 담아서 전송하도록 합니다.
Spring 추구하는 비침투성 스프링 소스코드가 개발자 코드에 들어가지 않는 POJO 기반의 프로그래밍
테스트하기 편하고 유지보수하기 편해집니다.
 
~~~
@Component
// Spring 4.2 이전 버전에는 ApplicationListener 상속이 필수였습니다.
// public class MyEventHandler implements ApplicationListener<MyEvent> {
public class MyEventHandler {
    @EventListener
    public void onApplicationEvent(MyEvent event) {
        int data = event.getData();
        System.out.println("이벤트를 받았다. 데이터 : " + data);
    }
}
~~~

MyEventHandler는 Bean으로 등록이 되어있어야 합니다.
Spring이 누구한테 전달해야 하는지 알수 있기때문입니다.

## 실행 순서를 줘여하는 경우

~~~
@Component
public class AnotherHandler {

    @EventListener
    public void hendler(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("AnotherHandler : " + myEvent.getData());
    }
}

@Component
public class MyEventHandler {
    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void onApplicationEvent(MyEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("MyEventHandler : " + event.getData());
    }
}

결과 - 

Thread[main,5,main]
AnotherHandler : 100
Thread[main,5,main]
MyEventHandler : 100
~~~

같은 Thread에서 순차적으로 실행되며 실행 순서는 @Order 사용해서 조정할 수 있습니다.

## 비동기

비동기적으로 실행할때는 각각의 Thread에서 따로 실행되기 때문에 그리고 Thread의 실행 순서는 Thread 스케줄링 상황에 따라서 달라집니다. 
따라서 @Order 의미는 없습니다.

~~~
public class MyEventHandler {
    @EventListener
    @Async
    public void onApplicationEvent(MyEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("MyEventHandler : " + event.getData());
    }
}

@Component
public class AnotherHandler {

    @EventListener
    @Async
    public void hendler(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("AnotherHandler : " + myEvent.getData());
    }
}
~~~

지금 상태에서 실행을 해도 메인 Thread에서 실행이 됩니다.
main Application.class 에 @EnableAsync 에노테이션을 추가합니다.

~~~
Thread[task-2,5,main]
MyEventHandler : 100
Thread[task-1,5,main]
AnotherHandler : 100
~~~

각각 다른 Thread에서 출력은 됩니다.

## 스프링이 제공하는 기본 이벤트

- ContextRefreshedEvent: ApplicationContext를 초기화 했더나 리프래시 했을 때 발생.
- ContextStartedEvent: ApplicationContext를 start()하여 라이프사이클 빈들이 시작
신호를 받은 시점에 발생.
- ContextStoppedEvent: ApplicationContext를 stop()하여 라이프사이클 빈들이 정지
신호를 받은 시점에 발생.
- ContextClosedEvent: ApplicationContext를 close()하여 싱글톤 빈 소멸되는 시점에
발생.
- RequestHandledEvent: HTTP 요청을 처리했을 때 발생.

~~~
@Component
public class MyEventHandler {

    @EventListener
    @Async
    public void onApplicationEvent(ContextRefreshedEvent refreshedEvent) {
        // 해당 이벤트는 ApplicationContext 상속하고있어서 ApplicationContext 꺼내서 사용할 수 있습니다.
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextRefreshedEvent : " + refreshedEvent.getSource());
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextClosedEvent closedEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextClosedEvent : " + closedEvent.getSource());
    }
}
~~~

# ResourceLoader

리소스를 읽어오는 기능을 제공하는 인터페이스

~~~
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ResourceLoader loader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = loader.getResource("classpath:test.txt");
        System.out.println(resource.exists());
        System.out.println(resource.getDescription());
        System.out.println(Files.readString(Path.of(resource.getURI())));
    }
}

결과 - 

true
class path resource [test.txt]
hello spring file
~~~

# Resource 추상화

[The ResourceLoader](https://docs.spring.io/spring/docs/4.3.26.RELEASE/spring-framework-reference/htmlsingle/#resources)

org.springframework.core.io.Resource

- 특징
    - java.net.URL을 추상화 한 것.
    - 스프링 내부에서 많이 사용하는 인터페이스.
- 추상화 한 이유
    - 클래스패스 기준으로 리소스 읽어오는 기능 부재
    - ServletContext를 기준으로 상대 경로로 읽어오는 기능 부재
    - 새로운 핸들러를 등록하여 특별한 URL 접미사를 만들어 사용할 수는 있지만 구현이 복잡하고 편의성 메소드가 부족하다.

실제 로우레벨에 있는 리소스에 접근하는 기능을 만든겁니다.
기존 java.net.URL는 classpath 기준으로 URL을 가져오는 기능은 존재하지않습니다.

ClassPathXmlApplicationContext는 classpath 기준으로 Bean 설정파일을 찾아 등록합니다.
FileSystemXmlApplicationContext는 FileSystem 경로 기준으로 문자열 파일을 찾아서 Bean 설정파일 등록합니다.

- 구현체
    - UrlResource: java.net.URL 참고, 기본으로 지원하는 프로토콜 http, https, ftp, file, jar.
    - ClassPathResource: 지원하는 접두어 classpath:
    - FileSystemResource
    - ServletContextResource: 웹 애플리케이션 루트에서 상대 경로로 리소스 찾는다.

- 리소스 읽어오기
    - Resource의 타입은 locaion 문자열과 ApplicationContext의 타입에 따라 결정 된다.
    - ClassPathXmlApplicationContext -> ClassPathResource
    - FileSystemXmlApplicationContext -> FileSystemResource
    - WebApplicationContext -> ServletContextResource

자신이 사용하는 ApplicationContext에 따라서 달라집니다.

예제 코드

~~~
@Autowired
ClassPathXmlApplicationContext loader;

@Override
public void run(ApplicationArguments args) throws Exception {
    Resource resource = loader.getResource("test.txt");
}
~~~

ClassPathXmlApplicationContext 에서 불러오는 것이라면 classpath 문자열을 생략가능합니다.

- `ApplicationContext의 타입에 상관없이 리소스 타입을 강제`하려면 java.net.URL 접두어(+ classpath:)중 하나를 사용할 수 있다.
    - classpath:me/whiteship/config.xml -> ClassPathResource
    - file:///some/resource/path/config.xml -> FileSystemResource

## Class Type 검증

~~~
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext loader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = loader.getResource("classpath:test.txt");

        // Class Type 검증

        // locader는 ApplicationContext 타입이므로 ApplicationContext.class 출력되야 합니다.
        System.out.println(loader.getClass());   

        // resource는 classpath를 사용하였으므로 ClassPathResource.class 출력되야 합니다.
        System.out.println(resource.getClass()); 
    }
}

결과 - 

class org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
class org.springframework.core.io.ClassPathResource
~~~

검증이 완료되었습니다.

classpath 라는 프리픽스를 사용했기때문에 ClassPathResource 출력된것입니다.

# Validation(검증) 추상화

[org.springframework.validation](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/Validator.html)

~~~
 public class UserLoginValidator implements Validator {

    private static final int MINIMUM_PASSWORD_LENGTH = 6;

    public boolean supports(Class clazz) {
       return UserLogin.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "field.required");
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
       UserLogin login = (UserLogin) target;
       if (login.getPassword() != null
             && login.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH) {
          errors.rejectValue("password", "field.min.length",
                new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},
                "The password must be at least [" + MINIMUM_PASSWORD_LENGTH + "] characters in length.");
       }
    }
 }
~~~

개발자가 검증해야하는 인스턴스 class가 
UserLoginValidator.class가 검증할 수 있는 class인지 확인하는 메소드 supports
validate 메소드에서 검증작업이 실행됩니다.

간단 예제

제목이 비어있을 수 없는 조건으로 검증하겠습니다.

~~~
public class Event {
    private Long id;
    private String title;

    // getter, setter
}

public class EventValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        // 검증하는 타입의 class가 동일한지 확인합니다.
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // errorCode는 ApplicationContext에서 상속받는 MessageSource 기능을 사용해서 key값에 해당하는 에러 코드를 가져옵니다.
        // defalutmessage 매개변수는 errorCode 메세지에서 key값을 못찾았을때 출력하는 문자열
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "not empty", "Empty Masseage!!");
    }
}

@Component
public class EventRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Event event = new Event();
        EventValidator eventValidator = new EventValidator();

        // 어떤 객체를 검사할것인지 어떤 이름인지 작성
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(event, "event");

        // 검증 실시
        eventValidator.validate(event, beanPropertyBindingResult);

        // error 존재여부 확인
        System.out.println(beanPropertyBindingResult);

        beanPropertyBindingResult.getAllErrors().forEach(e -> {
            System.out.println("=======");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}

결과 - 

not empty.event.title
not empty.title
not empty.java.lang.String
not empty
Empty Masseage!!
~~~

## 스프링 부트 2.0.5 이상 버전을 사용할 때

- LocalValidatorFactoryBean 빈으로 자동 등록
- JSR-380(Bean Validation 2.0.1) 구현체로 hibernate-validator 사용.
- https://beanvalidation.org/

~~~
public class Event {
    private Long id;

    @NotEmpty
    private String title;

    @Min(0)
    private int limit;

    @Email
    private String email;

    ...
}

// 스프링 부트 2.0.5 이상 버전 검증방법
@Component
public class EventRunner implements ApplicationRunner {

    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(validator);

        Event event = new Event();
        event.setLimit(-1);
        event.setEmail("asdasd");

        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(event, "event");

        validator.validate(event, beanPropertyBindingResult);

        System.out.println(beanPropertyBindingResult);

        beanPropertyBindingResult.getAllErrors().forEach(e -> {
            System.out.println("=======");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}

결과 - 

=======
NotEmpty.event.title
NotEmpty.title
NotEmpty.java.lang.String
NotEmpty
비어 있을 수 없습니다
=======
Email.event.email
Email.email
Email.java.lang.String
Email
올바른 형식의 이메일 주소여야 합니다
=======
Min.event.limit
Min.limit
Min.int
Min
0 이상이어야 합니다
~~~

# PropertyEditor

[DataBinder](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/DataBinder.html)

기술적인 관점: 프로퍼티 값을 타겟 객체에 설정하는 기능
사용자 관점: 사용자 입력값을 애플리케이션 도메인 모델에 동적으로 변환해 넣어주는 기능.
해석하자면: 할당할때 왜 바인딩이 필요하냐면 `입력값은 대부분 “문자열”`인데, 그 값을 객체가 가지고 있는 int, long, Boolean, Date 등 심지어 Event, Book 같은 도메인 타입으로도 `변환해서 넣어주는 기능.`

- 스프링 3.0 이전까지 DataBinder가 변환 작업 사용하던 인터페이스
- 쓰레드-세이프 하지 않음 (상태 정보 저장 하고 있음, 따라서 싱글톤 빈으로 등록해서
쓰다가는...)
- Object와 String 간의 변환만 할 수 있어, 사용 범위가 제한적 임. (그래도 그런 경우가
대부분이기 때문에 잘 사용해 왔음. 조심해서..)

간단 예제

~~~
public class Event {
    private Long Id;
    private String title;

    public Event(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

/*
 * Text를 Event 로 변환해야합니다.
 * */
public class EventEditor extends PropertyEditorSupport {

    /*
    * getValue, setValue 는 PropertyEditor 가지고있는 값입니다.
    * 이 값이 서로다른 Thread 에서 공유가 됩니다.
    * stateful 상태입니다. 이는 thread safe 하지 않기 때문에 여러 thread 에서 공유해서 사용하면 안됩니다.
    * 즉 EventEditor.class 를 Bean으로 등록해서 사용하면 안됩니다.
    * 그럼 어떻게 사용하느냐?
    * @InitBinder 를 사용하여 사용하는 Controller 위치에 등록하는 방법이 있습니다.
    * */
    @Override
    public String getAsText() {
        Event event = (Event) getValue();
        return event.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        /*
        * 값이 전달되는 것은 문자열이지만 개발자는 숫자로 인식할겁니다.
        * */
        Event event = new Event(Long.parseLong(text));
        setValue(event);
    }
}

@RestController
public class EventController {

    /*
    * @InitBinder 를 사용하여 EventEditor를 Controller 위치에 등록
    * Controller가 어떤 요청을 처리하기 전에 WebDataBinder 내부에 들어있는 PropertyEditor 정보를 사용하게 됩니다.
    * 문자열로 들어온 {event} 값을 원하는 값의 형으로 변환 후 Event 객체로 바꾸는 작업을 합니다.
    * */
    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Event.class, new EventEditor());
    }

    /*
    * {event} 입력을 1, 2, 3, 4, ... int 형으로 event의 id를 입력합니다.
    * 입력한 숫자를 Event 타입으로 변환을 해서 Spring에서 받습니다.
    * */
    @GetMapping("/event/{event}")
    public String getEvent(@PathVariable Event event) {
        System.out.println(event.getId());
        return event.getId().toString();
    }
}

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getEvent() throws Exception {
        mockMvc
                .perform(get("/event/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());
    }
}
~~~

Stateless는 http와 같이 `이전의 상태를 기록하지 않는 접속`입니다.
어떤 절차에 따른 작업을 하기 위해서 웹서버에 접속을 하고 작업을 진행하다 접속이 끊어졌을때 작업을 새로이 시작해야 한다고 보시면 됩니다. 하긴 요즈음의 경우 쿠키같은 것으로 조금은 개선이 되어 있기는 하지만요..
결국 웹서버가 사용자의 작업을 기억하지 않고 있다는 의미입니다.

그에 비해 stateful은 `상태를 기억하고 있는 것`입니다. 아마 이에 대한 적절한 예는 온라인 게임의 경우라고 볼 수 있겠네요. 사용자가 진행한 작업또는 사용자의 요청을 서버가 기록하고 있으며 사용자가 다시 게임에 로그온 했을때는 이전에 기록된 단계에서 부터 다시 시작하는 것입니다.

쓰레드 세이프(thread-safe)란 무엇인가?
thread safe란 것은 `여러 thread가 동시에 사용되어도 안전`하단 말입니다.

# Converter와 Formatter

[Interface Converter<S,T>](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/Converter.html)

[Interface ConverterRegistry](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/ConverterRegistry.html)

PropertyEditor의 여러가지 단점을 보안해서 Converter 나왔습니다.

## Converter

- Converter
    - S 타입을 T 타입으로 변환할 수 있는 매우 일반적인 변환기.
    - 상태 정보 없음 == Stateless == 쓰레드세이프
    - ConverterRegistry에 등록해서 사용

간단 예제

~~~
@RestController
public class EventController {
    /*
    * {event} 입력을 1, 2, 3, 4, ... int 형으로 event의 id를 입력합니다.
    * 입력한 숫자를 Event 타입으로 변환을 해서 Spring에서 받습니다.
    * 쭌프로피셜 event는 숫자입니다 하지만 매개변수는 Event 타입 입니다.
    * 숫자를 Event 타입으로 변환해주는게 이번 예제의 핵심
    * */
    @GetMapping("/event/{event}")
    public String getEvent(Event event) {
        System.out.println(event);
        return event.getId().toString();
    }
}


public class EventConverter {
    /*
    * 상태정보가 없기 때문에 Bean으로 등록해도 상관없습니다.
    * */

    public static class StringToEventConverter implements Converter<String, Event> {
        @Override
        public Event convert(String source) {
            return new Event(Long.parseLong(source));
        }
    }

    public static class EventToStringConverter implements Converter<Event, String> {
        @Override
        public String convert(Event source) {
            return source.getId().toString();
        }
    }
}

/*
* StringToEventConverter 등록합니다.
* 모든 Controller에서 동작을 합니다.
* */
@Configuration
public class WebConfig implements WebMvcConfigurer {    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EventConverter.StringToEventConverter());
    }
}
~~~

## Formatter

[Interface Formatter<T>](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/Formatter.html)
[Interface FormatterRegistry](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/FormatterRegistry.html)

- PropertyEditor 대체제
    - Object와 String 간의 변환을 담당한다.
    - 문자열을 Locale에 따라 다국화하는 기능도 제공한다. (optional)
    - FormatterRegistry에 등록해서 사용

간단 예제

~~~
/*
* thread-safe 하므로 Bean 등록이 가능합니다.
*
* 등록해서 사용하는 방법은 ConverterRegistry에 등록하여 사용해야 합니다.
* */
public class EventFormatter implements Formatter<Event> {
    @Override
    public Event parse(String text, Locale locale) throws ParseException {
        return new Event(Long.parseLong(text));
    }

    @Override
    public String print(Event object, Locale locale) {
        return object.getId().toString();
    }
}

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*
    * StringToEventConverter 등록합니다.
    * ConversionService 등록이 됩니다.
    * */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new EventFormatter());
    }
}
~~~

## ConversionService

- 실제 변환 작업은 이 인터페이스를 통해서 쓰레드-세이프하게 사용할 수 있음.
- 스프링 MVC, 빈 (value) 설정, SpEL에서 사용한다.
- DefaultFormattingConversionService
    - FormatterRegistry
    - ConversionService
    - 여러 기본 컴버터와 포매터 등록 해 줌.

## 스프링 부트

- 웹 애플리케이션인 경우에 DefaultFormattingConversionSerivce를 상속하여 만든
WebConversionService를 빈으로 등록해 준다.
- Formatter와 Converter 빈을 찾아 자동으로 등록해 준다.

## 등록된 Converter 확인하는 방법

~~~
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ConversionService conversionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(conversionService);
        System.out.println(conversionService.getClass().toString());
    }
}
~~~

여러가지 등록된 Converter 확인할 수 있습니다.

# SpEL (스프링 Expression Language)

[Core Technologies](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions)

- 스프링 EL이란?
    - 객체 그래프를 조회하고 조작하는 기능을 제공한다.
    - Unified EL과 비슷하지만, 메소드 호출을 지원하며, 문자열 템플릿 기능도 제공한다.
    - OGNL, MVEL, JBOss EL 등 자바에서 사용할 수 있는 여러 EL이 있지만, SpEL은 모든 스프링 프로젝트 전반에 걸쳐 사용할 EL로 만들었다.
    - 스프링 3.0 부터 지원.

- 문법
    - #{“표현식"}
    - ${“프로퍼티"}
    - 표현식은 프로퍼티를 가질 수 있지만, 반대는 안 됨.
        - #{${my.data} + 1}
    - 레퍼런스 참고

간단 예제

~~~
my.value=100

@Component
public class AppRunner implements ApplicationRunner {

    // 표현식 사용방법
    @Value("#{1 + 1}")
    int number;

    @Value("#{'hello' + 'hi~!'}")
    String string;

    @Value("#{1 eq 1}")
    boolean trueOrFalse;

    // property 잠조방법
    @Value("${my.value}")
    String myValue;

    // property 잠조와 동시에 표현식을 사용 가능합니다.
    @Value("#{${my.value} + 10}")
    String myValue2;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======");
        System.out.println(number);
        System.out.println(string);
        System.out.println(trueOrFalse);
        System.out.println(myValue);
        System.out.println(myValue2);
        System.out.println("======");
    }
}

결과 -

======
2
hellohi~!
true
100
110
======
~~~

## 실제로 어디서 쓰나?

- @Value 애노테이션
- @ConditionalOnExpression 애노테이션
- 스프링 시큐리티
    - https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html
    - 메소드 시큐리티, @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
    - XML 인터셉터 URL 설정
- 스프링 데이터
    - https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions
    - @Query 애노테이션
- Thymeleaf

# 스프링 AOP

Aspect-oriendted Programming (AOP)은 OOP를 보완하는 수단으로, 흩어진 Aspect를 모듈화 할 수 있는 프로그래밍 기법.

- AOP 주요 개념
    - Aspect와 Target(Aspect가 가지고있는 적용하는 대상들)
    - Advice(작업해야하는 일)
    - Pointcut(어디에 적용되야 하는지 임시 저장하는 곳)
    - Join point()

- AOP 구현체
    - https://en.wikipedia.org/wiki/Aspect-oriented_programming
    - 자바
        - AspectJ
        - 스프링 AOP

- AOP 적용 방법
    - 컴파일
    - 로드 타임
    - 런타임

Spring AOP의 `핵심기능과 부가기능`

- `업무 로직을 포함`하는 기능을 핵심 기능(Core Concerns)
- `핵심 기능을 도와주는 부가적인 기능(로깅, 보안)`을 부가기능(Cross-cutting Concerns) 이라고 부른다.

AOP는 애플리케이션에서의 관심사의 분리(기능의 분리), `핵심적인 기능에서 부가적인 기능을 분리`한다.
`분리한 부가기능을 에스펙트(Aspect)`라는 독특한 모듈형태로 만들어서 설계하고 개발하는 방법이다.

- OOP를 적용하여도 핵심기능에서 부가기능을 쉽게 분리된 모듈로 작성하기 어려운 문제점을 AOP가
해결해 준다고 볼수 있다.
- AOP는 부가기능을 애스펙트(Aspect)로 정의하여, 핵심기능에서 부가기능을 분리함으로써 
핵심기능을 설계하고 구현할 때 객체지향적인 가치를 지킬수 있도록 도와주는 개념이다.

## 애스펙트(Aspect)란?

- 애스펙트는 부가기능을 정의한 코드인 어드바이스(Advice)를 `어디에 적용하지를 결정하는 포인트컷(PointCut)`을 합친 개념이다.

Advice + PointCut = Aspect

- AOP 개념을 적용하면 핵심기능 코드 사이에 침투된 부가기능을 독립적인 애스펙트로 구분해 낼수 있다.
- 구분된 부가기능 애스펙트를 런타임 시에 필요한 위치에 동적으로 참여하게 할 수 있다.

## AOP 용어 정리

- 타겟(Target)
    - 핵심 기능을 담고 있는 모듈로 타겟은 `부가기능을 부여할 대상`이 된다.
- 어드바이스(Advice)
    - 어드바이스는 타겟에 제공할 `부가기능을 담고 있는 모듈`이다.
- 조인포인트(Join Point)
    - 어드바이스가 `적용될 수 있는 위치`를 말한다.
    - 타겟 객체가 구현한 인터페이스의 모든 메서드는 조인 포인트가 된다.
- 포인트 컷(Pointcut)
    - 어드바이스를 `적용할 타겟의 메서드를 선별하는 정규표현식`이다.
    - 포인트컷 표현식은 execution으로 시작하고 메서드의 Signature를 비교하는 방법을 주로 이용한다.
- 애스펙트(Aspect)
    - 애스펙트는 AOP의 기본 모듈이다.
    - 애스펙트 = `어드바이스 + 포인트컷`
    - 애스펙트는 싱글톤 형태의 객체로 존재한다.
- 어드바이저(Advisor)
    - 어드바이저 = 어드바이스 + 포인트컷
    - 어드바이저는 `Spring AOP에서만 사용되는 특별한 용어`이다.
- 위빙(Weaving)
    - 위빙은 포인트컷에 의해서 결정된 타겟의 조인 포인트에 `부가기능(어드바이스)를 삽입하는 과정`을 뜻한다.
    - 위빙은 AOP가 `핵심기능(타겟)의 코드에 영향을 주지 않으면서 필요한 부가기능(어드바이스)를 추가할 수 있도록 해주는 핵심적인 처리과정`이다.

## Spring AOP 특징

- 1. Spring은 `프록시 기반 AOP`를 지원한다.
    - Spring은 `타겟(target) 객체에 대한 프록시를 만들어 제공`한다.
    - 타겟을 감싸는 프록시는 `실행시간(Runtime)에 생성`된다.
    - 프록시는 어드바이스를 타겟 객체에 적용하면서 생성되는 객체이다.
- 2. `프록시(Proxy)가 호출을 가로챈다(Intercept)`
    - 프록시는 타겟 객체에 대한 호출을 가로챈 다음 어드바이스의 부가기능 로직을 수행하고 난 후에 타겟의 핵심기능 로직을 호출한다.(전처리 어드바이스)
    - 또는 타겟의 핵심기능 로직 메서드를 호출한 후에 부가기능(어드바이스)을 수행하는 경우도 있다.(후처리 어드바이스)
- 3. Spring AOP는 `메서드 조인 포인트만 지원`한다.
    - Spring은 동적 프록시를 기반으로 AOP를 구현하므로 메서드 조인 포인트만 지원한다.
    - 핵심기능(타겟)의 메서드가 호출되는 `런타임 시점에만 부가기능(어드바이스)을 적용`할 수 있다.
    - 반면에 `AspectJ 같은 고급 AOP 프레임워크를 사용하면 객체의 생성, 필드값의 조회와 조작, static 메서드 호출 및 초기화 등의 다양한 작업에 부가기능을 적용 할 수 있다.`

## Spring AOP의 구현 방식

- 1. `XML 기반의 POJO 클래스를 이용한 AOP 구현`
    - 부가기능을 제공하는 Advice 클래스를 작성한다.
    - XML 설정 파일에 <aop:config>를 이용해서 애스펙트를 설정한다. (즉, 어드바이스와 포인트컷을 설정함)

- 2. `@Aspect 어노테이션을 이용한 AOP 구현`
    - @Aspect 어노테이션을 이용해서 부가기능을 제공하는 Aspect 클래스를 작성한다.
    - 이 때 Aspect 클래스는 어드바이스를 구현하는 메서드와 포인트컷을 포함한다.
    - XML 설정 파일에 <aop:aspectj-autoproxy />를 설정한다.

## Advice의 종류

- Around 어드바이스
    - 타겟의 메서드가 호출되기 이전(before) 시점과 이후 (after) 시점에 모두 처리해야 할 필요가 잇는 부가기능을 정의한다.
    - -> Joinpoint 앞과 뒤에서 실행되는 Advice
- Before 어드바이스
    - 타겟의 메서드가 실행되기 이전(before) 시점에 처리해야 할 필요가 있는 부가기능을 정의한다.
    - -> Jointpoint 앞에서 실행되는 Advice
- After Returning 어드바이스
    - 타겟의 메서드가 정상적으로 실행된 이후(after) 시점에 처리해야 할 필요가 있는 부가기능을 정의한다.
    - -> Jointpoint 메서드 호출이 정상적으로 종료된 뒤에 실행되는 Advice
- After Throwing 어드바이스
    - 타겟의 메서드가 예외를 발생된 이후(after) 시점에 처리해야 할 필요가 있는 부가기능을 정의한다.
    - -> 예외가 던져 질때 실행되는 Advice

## Advice를 정의하는 태그

- <aop:before>
    - 메서드 실행전에 적용되는 어드바이스를 정의한다.
- <aop:after-returning>
    - 메서드가 정상적으로 실행 된 후에 적용되는 어드바이스를 정의한다.
- <aop:after-throwing>
    - 메서드가 예외를 발생시킬 때 적용되는 어드바이스를 정의한다.
    - try-catch 블록에서 catch 블록과 비슷하다.
- <aop:after>
    - 메서드가 정상적으로 실행되는지 또는 예외를 발생시키는지 여부에 상관없이 어드바이스를 정의한다.
    - try-catch-finally에서 finally 블록과 비슷하다.
- <aop:around>
    - 메서드 호출 이전, 이후, 예외발생 등 모든 시점에 적용 가능한 어드바이스를 정의한다.

## JointPoint 인터페이스

- JoinPoint는 Spring AOP 혹은 AspectJ에서 AOP가 적용되는 지점을 뜻한다.
- 해당 지점을 AspectJ에서 JoinPoint라는 인터페이스로 나타낸다.

1. JointPoint 메서드

- getArgs() 
    - 메서드 아규먼트를 반환한다.
- getThis()  
    - 프록시 객체를 반환한다.
- getTarget() 
    - 대상 객체를 반환한다.
- getSignature()
    - 어드바이즈 또는 메서드의 설명(description)을 반환한다.
- toString() 
    - 어드바이즈 되는 메서드의 설명을 출력한다.

# 프록시 기반 AOP

- 스프링 AOP 특징
    - 프록시 기반의 AOP 구현체
    - 스프링 빈에만 AOP를 적용할 수 있다.
    - 모든 AOP 기능을 제공하는 것이 목적이 아니라, 스프링 IoC와 연동하여 엔터프라이즈 애플리케이션에서 가장 흔한 문제에 대한 해결책을 제공하는 것이 목적.

- 프록시 패턴
    - 왜? (기존 코드 변경 없이) `접근 제어 또는 부가 기능 추가`
    - 기존 코드를 건드리지 않고 성능을 측정해 보자. (프록시 패턴으로)

간단 예제

~~~
/*
* interface Subject
* */
public interface EventService {
    void createEvent();
    void publicEvent();
}

/*
 * Real Subject
 * */
@Service
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("createEvent");
    }

    @Override
    public void publicEvent() {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("publicEvent");
    }
}

/*
 * Client
 * */
@Component
public class AppRunner implements ApplicationRunner {

    /*
     * Interface 형식으로 주입받는것을 권장합니다.
     * 일단 인터페이스 타입을 쓰면 클래스 타입을 쓸 때 보다 프록시를 만들 때 제약이 별로 없거든요.
     * 클래스 같은 경우엔 final class거나 생성자가 private라거나 그런 경우에 프록시를 만들지 못하는 경우도 있어요.
     * 또한 인터페이스가 있는데 굳이 클래스 타입을 쓰면 프록시 주입이 제대로 안되는 경우가 발생할 수도 있습니다.
     */
    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventService.createEvent();
        eventService.publicEvent();
    }
}
~~~

프록시로 SimpleEventService.class, AppRunner.class 를 건들이지 않고 기능을 추가하는 것입니다.
SimpleEventService.class 메소드들이 실행되는 시간을 측정하는 기능을 추가할겁니다.

만약 직접 코드를 건들여서 추가한다면

~~~
@Service
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() {
        long l = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("createEvent");
         
        System.out.println(System.currentTimeMillis() - l);
    }
}
~~~

프로시를 활용한다면

~~~
/*
 * Sbject와 타입이 동일해야 합니다.
 * */
@Primary
@Service
public class ProxySimpleEventService implements EventService {

    /*
     * Proxy 같은경우 Subject Bean을 주입 받아서 사용해야 합니다.
     * */
    @Autowired
    EventService eventService;

    @Override
    public void createEvent() {
        /*
        * 기능을 위임해 줍니다.
        * 그와 동시에 시간을 측정하는 기능을 추가합니다.
        * */
        long l = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        eventService.createEvent();

        System.out.println(System.currentTimeMillis() - l);
    }

    @Override
    public void publicEvent() {
        eventService.publicEvent();
    }
}
~~~

기존 클래스를 건들이지 않고 기능을 추가해서 실행하였습니다.
하지만 아직도 중복의 코드는 존재합니다.
그리고 프록시 코드를 만드는 비용이 너무 크다는 단점.
똑같은 프록시 기능이 다른 class 에도 적용이 된다면?

- 그래서 등장한 것이 스프링 AOP
- 스프링 IoC 컨테이너가 제공하는 기반 시설과 Dynamic 프록시를 사용하여 여러 복잡한 문제 해결.
- 동적 프록시: 동적으로 프록시 객체 생성하는 방법
    - 자바가 제공하는 방법은 인터페이스 기반 프록시 생성.
    - CGlib은 클래스 기반 프록시도 지원.
- 스프링 IoC: 기존 빈을 대체하는 동적 프록시 빈을 만들어 등록 시켜준다.
    - 클라이언트 코드 변경 없음.
    - AbstractAutoProxyCreator implements BeanPostProcessor

- 어드바이스 정의
    - @Before
    - @AfterReturning
    - @AfterThrowing
    - @Around

SimpleEventService.class가 Bean으로 등록이 되면 AbstractAutoProxyCreator의 BeanPostProcessor 로 SimpleEventService.class 감싸는 프록시 Bean을 만들어서 SimpleEventService.class 대신에 등록이 됩니다.

# @AOP

## 특정 class 메소드를 적용시키는 경우

~~~
@Aspect
@Component
public class PerfAspect {

    /*
     * 해야할 일(advice) 생성합니다.
     * ProceedingJoinPoint advice가 적용하는 대상
     *
     * @Around() Pointcut 이름을 줄수도 있고 직접 정의할 수 도 있습니다.
     * 해당 메소드를 감싸는 형태로 적용됩니다.
     * proceed 메소드 호출 전체를 감싸고 있으므로 호출 이전과 이후의 동작을 추가하게 할 수 있습니다.
     * */
    @Around("execution(* com.example..*.EventService.*(..))")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();
        Object proceed = pjp.proceed();
        System.out.println(System.currentTimeMillis() - l);
        return proceed;
    }
}
~~~

## 특정 메소드에만 적용 시키는 경우

에노테이션을 생성하여 등록합니다.

~~~
/*
 * 에노테이션에서 RetentionPolicy 란 에노테이션 정보를 얼마나 유지할 것인가
 * RetentionPolicy.CLASS 인 경우 class 까지만 유지하겠다는 설정
 * 즉 컴파일 바이트코드 안에도 에노테이션이 존재한다는 뜻입니다.
 * 기본값은 CLASS 입니다. 고로 @Retention 생략 가능합니다.
 * 만약 RetentionPolicy.SOURCE 인경우 컴파일 이후 사라집니다.
 *
 * @Target 메소드라는 것을 선언해줍니다.
 *
 * @Around("@annotation(PerLogging)") PerLogging 에노테이션이 붙은 메소드에만 실행하는 코드선언
 * */
@Retention(RetentionPolicy.CLASS) // 생략 가능
@Target(ElementType.METHOD)
@Documented
public @interface PerLogging { }

@Aspect
@Component
public class PerfAspect {
    @Around("@annotation(PerLogging)")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        ...
    }
}

@Service
public class SimpleEventService implements EventService {
    @PerLogging
    @Override
    public void createEvent() {...}

    ...
}
~~~

# Null-safety

- 스프링 프레임워크 5에 추가된 Null 관련 애노테이션
    - @NonNull
    - @Nullable
    - @NonNullApi (패키지 레벨 설정)
    - @NonNullFields (패키지 레벨 설정)

~~~
@Service
public class EventService {

    @NonNull
    public String createEvent(@NonNull String name) {
        return name;
    }
}

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String message = eventService.createEvent(null);
        // error 발생
    }
}
~~~