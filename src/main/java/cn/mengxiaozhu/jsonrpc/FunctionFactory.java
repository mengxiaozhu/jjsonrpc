package cn.mengxiaozhu.jsonrpc;

public interface FunctionFactory {
    Function get(String name);

    Function get(String name,int length);
}