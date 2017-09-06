package cn.mengxiaozhu.jsonrpc;

import com.google.gson.JsonArray;

public class ServiceNamedRequest {
    private long id;
    private String Service;
    private String Method;
    private JsonArray params;

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public JsonArray getParams() {
        return params;
    }

    public void setParams(JsonArray params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
