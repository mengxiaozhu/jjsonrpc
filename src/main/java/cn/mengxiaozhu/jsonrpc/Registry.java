package cn.mengxiaozhu.jsonrpc;

public interface Registry {
    void registMethods(Object object);

    void registMethod(String name, Object object, String method) throws NoSuchMethodException;

    void registService(String name, Object object);
}