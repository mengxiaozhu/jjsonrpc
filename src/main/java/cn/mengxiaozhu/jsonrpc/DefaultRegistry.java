package cn.mengxiaozhu.jsonrpc;


import cn.mengxiaozhu.jsonrpc.annotation.Ignore;
import cn.mengxiaozhu.jsonrpc.annotation.Register;
import cn.mengxiaozhu.jsonrpc.exceptions.MethodNameConflictException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRegistry implements Registry, FunctionFactory {
    private final ConcurrentHashMap<String, ConcurrentHashMap<Integer, Function>> store = new ConcurrentHashMap<>();

    @Override
    public Function get(String name) {
        ConcurrentHashMap<Integer, Function> meathodMap = store.get(name);
        if (meathodMap != null) {
            List<Function> list = new ArrayList<>();
            list.addAll(meathodMap.values());
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public Function get(String name, int length) {
        ConcurrentHashMap<Integer, Function> meathodMap = store.get(name);
        if (meathodMap != null) {
            return meathodMap.get(length);
        }
        return null;
    }

    @Override
    public void registerMethods(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        ConcurrentHashMap<Integer, Function> funMap = null;
        for (Method method : methods) {
            if (method.getAnnotation(Ignore.class) != null) {
                continue;
            }
            if (method.isAccessible()) {
                Register register = method.getAnnotation(Register.class);
                String methodName = method.getName();
                if(register!=null){
                    methodName = register.value();
                }
                if (this.store.get(methodName) == null) {
                    funMap = new ConcurrentHashMap<>();
                } else {
                    funMap = this.store.get(methodName);
                }
                funMap.put(method.getParameterCount(), new Function(method, object));
                this.store.put(methodName, funMap);
            }
        }
    }

    @Override
    public void registerMethod(String name, Object object, String method) throws NoSuchMethodException {
        ConcurrentHashMap<Integer, Function> funMap = new ConcurrentHashMap<>();

        Map<Integer, Function> map = new HashMap<>();
        Method method1 = object.getClass().getDeclaredMethod(method);
        int paramsLength = method1.getParameterCount();
        map.put(paramsLength, new Function(method1, object));
        funMap.putAll(map);

        this.store.put(name, funMap);
    }

    @Override
    public void registerService(String name, Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        ConcurrentHashMap<Integer, Function> funMap = null;
        for (Method method : methods) {
            if (method.getAnnotation(Ignore.class) != null) {
                return;
            }
            if (Modifier.isPublic(method.getModifiers())) {
                String methodName = method.getName();

                Register register = method.getAnnotation(Register.class);
                StringBuffer keyBuffer = new StringBuffer();

                if(register!=null){
                    methodName = register.value();
                    keyBuffer.append(methodName);
                }else{
                    keyBuffer.append(name).append(".").append(methodName);
                }
                methodName = keyBuffer.toString();
                if (this.store.get(methodName) == null) {
                    funMap = new ConcurrentHashMap<>();
                } else {
                    funMap = this.store.get(methodName);
                    if (funMap.get(method.getParameterCount()) != null) {
                        throw new MethodNameConflictException("Registry failed");
                    }
                }

                funMap.put(method.getParameterCount(), new Function(method, object));
                this.store.put(methodName, funMap);
            }
        }
    }
}
