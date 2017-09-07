package cn.mengxiaozhu.jsonrpc;

public interface Registry {
    void registerMethods(Object object);

    void registerMethod(String name, Object object, String method) throws NoSuchMethodException;

    void registerService(String name, Object object);
}