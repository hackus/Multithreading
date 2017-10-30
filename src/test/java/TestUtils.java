import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class TestUtils {
    public static void testFunction(Consumer<List<String>> consumer, List<String> list) {
        long start = System.nanoTime();
        consumer.accept(list);
        long end = System.nanoTime();

        long time = (end - start) / 1000;

        System.out.println(Thread.currentThread().getStackTrace()[2].getClassName() + "," + Thread.currentThread().getStackTrace()[2].getMethodName() + ", Time elapsed: " + time);
    }

    public static void testFunctionTest(BiConsumer<List<String>, Integer> consumer, List<String> list, int index) {

        long start = System.nanoTime();
        consumer.accept(list, index);
        long end = System.nanoTime();

        long time = (end - start) / 1000;

        System.out.println(Thread.currentThread().getStackTrace()[2].getClassName() + "," + Thread.currentThread().getStackTrace()[2].getMethodName() + ", Time elapsed: " + time);
    }

    public static void testFunction(BiConsumer<Set<String>, String> consumer, String str, Set<String> set) {
        long start = System.nanoTime();
        consumer.accept(set, str);
        long end = System.nanoTime();

        long time = (end - start) / 1000;

        System.out.println(Thread.currentThread().getStackTrace()[2].getClassName() + "," + Thread.currentThread().getStackTrace()[2].getMethodName() + ", Time elapsed: " + time);
    }

    public static void testFunctionForMaps(BiConsumer<Map<Integer, String>, Integer> consumer, Map<Integer, String> map, int value) {
        long start = System.nanoTime();
        consumer.accept(map, value);
        long end = System.nanoTime();
        long time = (end - start) / 1000;
        System.out.println(Thread.currentThread().getStackTrace()[2].getClassName() + "," + Thread.currentThread().getStackTrace()[2].getMethodName() + ", Time elapsed: " + time);
    }
}
