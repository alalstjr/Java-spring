package me.whiteship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AppRunner implements ApplicationRunner {

    //    @Autowired
    //    private Single single;

    //    @Autowired
    //    private Proto proto;

    @Autowired
    ApplicationContext act;

    @Value("{app.name}")
    String appName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //        System.out.println(proto);
        //        System.out.println(single.getProto());

        /*
         * - 싱글톤 -
         * 첫번째 출력하는 proto는 AppRunner가 받아온 Proto입니다.
         * 두번째 출력하는 proto는 Single이 참조한 Proto
         * 둘의 Proto는 같은 인스턴스를 사용합니다.
         * 해당 Bean의 인스턴스 하나만 사용합니다.
         * */

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

        System.out.println("ProtoType By SingleType");
        System.out.println(act.getBean(Single.class).getProto());
        System.out.println(act.getBean(Single.class).getProto());
        System.out.println(act.getBean(Single.class).getProto());

        Environment environment = act.getEnvironment();
        System.out.println(Arrays.asList(environment.getActiveProfiles()));
        System.out.println(environment.getProperty("app.name"));
    }
}
