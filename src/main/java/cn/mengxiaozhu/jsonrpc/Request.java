package cn.mengxiaozhu.jsonrpc;


import com.google.gson.JsonArray;

import java.io.Serializable;

public class Request implements Serializable{
    static final long serialVersionUID = 0L;
    private String jsonrpc;
    private String method;
    private JsonArray params;
    private long id;

    public String getJsonrpc() {

        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public JsonArray getParams() {
        return params;
    }

    public void setParams(JsonArray params) {
        this.params = params;
    }
}
