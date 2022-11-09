import com.sal.flooringmastery.controller.FlooringMasteryController;
import com.sal.flooringmastery.dao.*;
import com.sal.flooringmastery.service.FlooringMasteryService;
import com.sal.flooringmastery.service.FlooringMasteryServiceImpl;
import com.sal.flooringmastery.ui.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {

    //UserIO io = new UserIOImpl();
    //FlooringMasteryView view = new FlooringMasteryView(io);
    //FlooringMasteryDao dao = new FlooringMasteryDaoImpl();
    //AuditDao adao = new AuditDaoImpl();

    //FlooringMasteryService myService = new FlooringMasteryServiceImpl(dao,adao);
    //FlooringMasteryController controller = new FlooringMasteryController(view,myService);
    //controller.run();

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        FlooringMasteryController controller = ctx.getBean("controller",FlooringMasteryController.class);

        controller.run();

    }
}
