package cn.mengxiaozhu.jsonrpc;


import cn.mengxiaozhu.jsonrpc.annotation.NoRegistry;
import cn.mengxiaozhu.jsonrpc.annotation.RegistryName;
import cn.mengxiaozhu.jsonrpc.exceptions.MethodNotOnlyException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRegistry implements Registry, FunctionFactory {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Function>> store = new ConcurrentHashMap<>();

    @Override
    public Function get(String name) {
        ConcurrentHashMap<String, Function> meathodMap = store.get(name);
        if (meathodMap != null) {
            List<Function> list = new ArrayList<>();
            list.addAll(meathodMap.values());
            if(list.size()>0){
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public Function getByParamsLength(String name, String count) {
        ConcurrentHashMap<String, Function> meathodMap = store.get(name);
        if (meathodMap != null && count != null) {

            return meathodMap.get(count);
        }
        return null;
    }

    @Override
    public void registerMethods(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        ConcurrentHashMap<String, Function> funMap = null;
        for (Method method : methods) {
            if (method.getAnnotation(NoRegistry.class) != null) {
                return;
            }
            if (method.isAccessible()) {
                if (this.store.get(method.getName()) == null) {
                    funMap = new ConcurrentHashMap<>();
                } else {
                    funMap = this.store.get(method.getName());
                }
                funMap.put("" + method.getParameterCount(), new Function(method, object));
                this.store.put(method.getName(), funMap);
            }
        }
    }

    @Override
    public void registerMethod(String name, Object object, String method) throws NoSuchMethodException {
        Map<String, Function> map = new HashMap<>();
        Method[] methods = object.getClass().getDeclaredMethods();
        String[] realNameArr = new String[1];
        Arrays.asList(methods).forEach((Method temp) -> {
            String realName = temp.getAnnotation(RegistryName.class).value();
            if (method.equals(realName)) {
                map.put("" + temp.getParameterCount(), new Function(temp, object));
                realNameArr[0] = temp.getName();
            }
        });
        ConcurrentHashMap<String, Function> funMap = null;
        if (realNameArr[0] != null && realNameArr[0].length() > 0) {
            if (this.store.get(realNameArr[0]) == null) {
                funMap = new ConcurrentHashMap<>();
            } else {
                funMap = this.store.get(realNameArr[0]);
            }

        } else {
            throw new NoSuchMethodException();
        }

        //Method realMethod = object.getClass().getDeclaredMethod(method);
//        if(realNameArr[0].equals(realMethod.getName())){
//            funMap.put(""+realMethod.getParameterCount(),new Function(object.getClass().getDeclaredMethod(method), object));
//        }
        funMap.putAll(map);
        this.store.put(name + "." + realNameArr[0], funMap);
    }

    @Override
    public void registerService(String name, Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        ConcurrentHashMap<String, Function> funMap = null;
        for (Method method : methods) {
            if (method.getAnnotation(NoRegistry.class) != null) {
                return;
            }
            if (Modifier.isPublic(method.getModifiers())) {
                String methodName = method.getName();

                if (this.store.get(name + "." + methodName) == null) {
                    funMap = new ConcurrentHashMap<>();
                } else {
                    funMap = this.store.get(name + "." + methodName);
                    if (funMap.get(method.getParameterCount() + "") != null) {
                        throw new MethodNotOnlyException("Registry failed");
                    }
                }

                funMap.put("" + method.getParameterCount(), new Function(method, object));
                this.store.put(name + "." + method.getName(), funMap);
            }
        }
    }
}
