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
    public void registerMethods(Object object) {
        this.registry.registerMethods(object);
    }

    @Override
    public void registerMethod(String name, Object object, String method) throws NoSuchMethodException {
        this.registry.registerMethod( name,  object,  method);
    }

    @Override
    public void registerService(String name, Object object) {
        this.registry.registerService(name,object);
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
