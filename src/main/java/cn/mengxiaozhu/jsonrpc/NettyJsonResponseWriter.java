package cn.mengxiaozhu.jsonrpc;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class NettyJsonResponseWriter implements ResponseWriter {
    private Channel channel;
    private Gson gson;

    public NettyJsonResponseWriter(Channel channel, Gson gson) {
        this.channel = channel;
        this.gson = gson;
    }

    @Override
    public void write(Response response) {
        this.channel.writeAndFlush(Unpooled.copiedBuffer(gson.toJson(response).getBytes()));
    }
}
