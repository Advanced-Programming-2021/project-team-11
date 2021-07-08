package view.menus;

public class Util {
    // https://stackoverflow.com/a/9391517/4213397
    public static <T> Class<T> forceInit(Class<T> klass) {
        try {
            Class.forName(klass.getName(), true, klass.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
        return klass;
    }
}
