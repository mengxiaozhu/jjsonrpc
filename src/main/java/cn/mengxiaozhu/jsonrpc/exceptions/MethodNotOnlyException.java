package cn.mengxiaozhu.jsonrpc.exceptions;

public class MethodNotOnlyException extends RuntimeException {
    public MethodNotOnlyException(String message) {
        super(message);
    }
}
