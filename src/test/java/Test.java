import com.minis.context.support.ClassPathXmlApplicationContext;
import com.test.service.AService;

public class Test {

    // aService -> bService -> cService -> aService
    private static void test() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) context.getBean("aService");
        aService.sayHello();
    }

    public static void main(String[] args) throws Exception {
        test();
    }
}
