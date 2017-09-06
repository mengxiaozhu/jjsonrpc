package cn.mengxiaozhu.jsonrpc;


public class Response {
    static final long serialVersionUID = 0L;
    private String jsonrpc="2.0";
    private Error error;
    private Object result;
    private long id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Response{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", error='" + error + '\'' +
                ", result=" + result +
                ", id=" + id +
                '}';
    }
}
