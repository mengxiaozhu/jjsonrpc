package cn.mengxiaozhu.jsonrpc;

public class SimpleDispatcher implements Dispatcher {
    private Caller caller;

    public SimpleDispatcher(Caller caller) {
        this.caller = caller;
    }

    @Override
    public void Execute(Context ctx) {
        ctx.getResponseWriter().write(caller.exec(ctx.getRequest()));
    }
}