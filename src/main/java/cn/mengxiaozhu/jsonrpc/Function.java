package cn.mengxiaozhu.jsonrpc;

import cn.mengxiaozhu.jsonrpc.exceptions.InvalidParamsException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Function {
    public Function(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    private Method method;
    private Object instance;

    Object invoke(Gson gson, JsonArray array) throws InvocationTargetException, IllegalAccessException, JsonSyntaxException,InvalidParamsException {
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        if (types.length != args.length) {
            throw new InvalidParamsException("params length not right");
        }
        for (int i = 0; i < types.length; i++) {
            args[i] = gson.fromJson(array.get(i), types[i]);
        }
        return this.method.invoke(this.instance, args);
    }
}