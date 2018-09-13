import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WrapAnimal implements Animal {

    private Animal                   animal;
    private Class<? extends Feature> clz;

    public WrapAnimal(Animal animal, Class clz) {
        this.animal = animal;
        this.clz = clz;
    }

    @Override
    public void see() {
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object obj = method.invoke(clz.newInstance(), args);
                animal.see();
                return obj;
            }
        };
        Feature f = (Feature) Proxy.newProxyInstance(getClass().getClassLoader(), clz.getInterfaces(), handler);
        f.load();
    }

    public static void main(String[] args) {
        Animal rat = new Rat();
        rat = new WrapAnimal(rat, Fly.class);
        rat = new WrapAnimal(rat, Dig.class);
        rat.see();
    }
}
