import com.minis.context.support.ClassPathXmlApplicationContext;
import com.test.service.BService;

public class Test {

    // baseBaseService -> aService -> baseService -> baseBaseService
    private static void test() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        BService bService = (BService) context.getBean("bService");
        bService.sayHello();
    }

    public static void main(String[] args) throws Exception {
        test();
    }
}
