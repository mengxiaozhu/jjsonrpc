package cn.mengxiaozhu.jsonrpc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PooledDispatcher implements Dispatcher {
    private ExecutorService pool;
    private SimpleDispatcher dispatcher;

    public PooledDispatcher(int size, Caller caller) {
        dispatcher = new SimpleDispatcher(caller);
        pool = Executors.newFixedThreadPool(size);
    }

    @Override
    public void Execute(Context context) {
        pool.submit(new Worker(dispatcher, context));
    }
}
