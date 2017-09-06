package cn.mengxiaozhu.jsonrpc;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class JsonrpcHandler extends ChannelInboundHandlerAdapter {

    private static Charset utf8 = Charset.forName("UTF-8");

    private Dispatcher dispatcher;
    private Gson gson;

    JsonrpcHandler(Config config) {
        this.dispatcher = config.getDispatcher();
        this.gson = config.getGson();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf json = (ByteBuf) msg;
        Request request = this.gson.fromJson(json.toString(utf8), Request.class);
        Context context = new Context();
        context.setRequest(request);
        context.setResponseWriter(new NettyJsonResponseWriter(ctx.channel(), this.gson));
        dispatcher.Execute(context);
    }
}
