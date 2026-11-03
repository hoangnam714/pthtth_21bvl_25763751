package lab06.bai1;
import java.rmi.registry.LocateRegistry;
import lab06.common.RmiSupport;
public class HelloClient {
    public static void main(String[] args) throws Exception {
        HelloService service = (HelloService) LocateRegistry.getRegistry(args.length > 0 ? args[0] : "localhost", RmiSupport.PORT).lookup("HelloService");
        System.out.println(service.sayHello());
    }
}
