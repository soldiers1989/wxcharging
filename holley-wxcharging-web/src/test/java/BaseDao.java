public abstract class BaseDao<T extends String> {

    private Class<T> clz = Util.getGenricType(getClass());

    void test() {
        System.out.println(clz);
    }
}
