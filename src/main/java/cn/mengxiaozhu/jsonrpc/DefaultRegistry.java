package cn.mengxiaozhu.jsonrpc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRegistry implements Registry, FunctionFactory {
    private final ConcurrentHashMap<String, Function> store = new ConcurrentHashMap<>();

    @Override
    public Function get(String name) {
        return store.get(name);
    }

    @Override
    public void registMethods(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAccessible()) {
                this.store.put(method.getName(), new Function(method, object));
            }
        }
    }

    @Override
    public void registMethod(String name, Object object, String method) throws NoSuchMethodException {
        this.store.put(name, new Function(object.getClass().getDeclaredMethod(method), object));
    }

    @Override
    public void registService(String name, Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                this.store.put(name + "." + method.getName(), new Function(method, object));
            }
        }
    }
}
