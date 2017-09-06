package cn.mengxiaozhu.jsonrpc;

public class Context {
    private Request request;
    private ResponseWriter responseWriter;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public ResponseWriter getResponseWriter() {
        return responseWriter;
    }

    public void setResponseWriter(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }
}
