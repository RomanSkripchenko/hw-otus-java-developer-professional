import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class LogInvocationHandler implements InvocationHandler {
    private static final Logger LOGGER = Logger.getLogger(LogInvocationHandler.class.getName());
    private final Object target;

    public LogInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Log.class)) {
            LOGGER.info(String.format("Executed method: %s, params: %s", method.getName(), Arrays.toString(args)));
        }
        return method.invoke(target, args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(), new Class<?>[] {interfaceType}, new LogInvocationHandler(target));
    }
}
