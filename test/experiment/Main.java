package experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        Object aObject = a;
        System.out.println(a.getClass());

        B b = new B();
        Object bObject = b;
        System.out.println(b.getClass());

        System.out.println("Cycle start:");
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            boolean next = random.nextBoolean();
            Object object = next ? new A() : new B();
            print(object);
            System.out.println(next ? A.class : B.class);
            System.out.println();
        }

        System.out.println("function start:");
        Consumer<A> aConsumer = x -> System.out.println("It's A, right? " + x.getClass());
        Consumer<B> bConsumer = x -> System.out.println("It's B, right? " + x.getClass());
        Map<Class, Consumer> map = new HashMap<>();
        map.put(A.class, aConsumer);
        map.put(B.class, bConsumer);
        Object next = next(random);
        map.get(next.getClass()).accept(next);

        Map<Class, RequestHandler> map2 = new HashMap<>();
        RequestAHandler requestAHandler = new RequestAHandler();
        map2.put(requestAHandler.getRequestType(), requestAHandler);
        RequestA requestA = new RequestA();
        map2.get(requestA.getClass()).handle(requestA);
    }

    private static void print(Object object) {
        System.out.println(object.getClass());
    }

    private static Object next(Random random) {
        return random.nextBoolean() ? new A() : new B();
    }
}
