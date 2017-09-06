package cn.mengxiaozhu.jsonrpc.exceptions;

public class NoSuchRPCMethodException extends Exception {
    public NoSuchRPCMethodException(String message) {
        super(message);
    }
}
