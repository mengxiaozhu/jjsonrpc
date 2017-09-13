package cn.mengxiaozhu.jsonrpc.exceptions;

public class MethodNameConflictException extends RuntimeException {
    public MethodNameConflictException(String message) {
        super(message);
    }
}
