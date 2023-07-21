import com.minis.context.ClassPathXmlApplicationContext;
import com.test.AService;

public class Test {

    // baseBaseService -> aService -> baseService -> baseBaseService
    private static void test() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) context.getBean("aService");
        aService.sayHello();
    }

    public static void main(String[] args) throws Exception {
        test();
    }
}
