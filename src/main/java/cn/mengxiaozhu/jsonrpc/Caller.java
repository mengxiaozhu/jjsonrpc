package cn.mengxiaozhu.jsonrpc;

import cn.mengxiaozhu.jsonrpc.exceptions.InvalidParamsException;
import cn.mengxiaozhu.jsonrpc.exceptions.NoSuchRPCMethodException;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

public interface Caller {

    Object call(String name, JsonArray params) throws Exception;

    default Response exec(Request request) {
        Response response = new Response();
        response.setId(request.getId());
        try {
            response.setResult(this.call(request.getMethod(), request.getParams()));
        } catch (NoSuchRPCMethodException e) {
            response.setError(new Error(-32601, "Method not found", null));
        } catch (JsonSyntaxException | InvalidParamsException e) {
            response.setError(new Error(-32602, e.getMessage(), null));
        } catch (Exception e) {
            response.setError(new Error(-32000, e.toString(), null));
        }
        return response;
    }
}
