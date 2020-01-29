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
생성자로 주입을 받아서 사용하는 방법을 IOC

스프링이 없이도 구현이 가능합니다.

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

Bean 생산이 완료되었다면 ApplicationContext를 만들어서 사용하면 됍니다.

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

component-scan으로 Bean으로는 등록이 되지만 의존성 주입은 아직 안됍니다.
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