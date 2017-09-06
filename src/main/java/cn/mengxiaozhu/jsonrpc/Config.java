package cn.mengxiaozhu.jsonrpc;

import com.google.gson.Gson;


public class Config implements Registry {
    private Registry registry;
    private Dispatcher dispatcher;
    private Gson gson;
    private int port;

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public void registMethods(Object object) {
        this.registry.registMethods(object);
    }

    @Override
    public void registMethod(String name, Object object, String method) throws NoSuchMethodException {
        this.registry.registMethod( name,  object,  method);
    }

    @Override
    public void registService(String name, Object object) {
        this.registry.registService(name,object);
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
