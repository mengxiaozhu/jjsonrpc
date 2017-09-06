package cn.mengxiaozhu.jsonrpc;

public class Worker implements Runnable {

    private SimpleDispatcher dispatcher;
    private Context context;

    public Worker(SimpleDispatcher dispatcher, Context context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    @Override
    public void run() {
        dispatcher.Execute(this.context);
    }
}
