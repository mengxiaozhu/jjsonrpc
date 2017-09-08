package cn.mengxiaozhu.jsonrpc;

public interface FunctionFactory {
    Function get(String name);

    Function getByParamsLength(String name,String count);
}