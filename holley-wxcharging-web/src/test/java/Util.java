import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Util {

    public static <T> Class<T> getGenricType(Class clz) {
        Type type = clz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Type[] types = ptype.getActualTypeArguments();
            if (types != null && types.length > 0) {
                return (Class) types[0];
            }
        }
        return (Class) Object.class;
    }

}
