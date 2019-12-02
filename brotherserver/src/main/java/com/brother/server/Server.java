package com.brother.server;

import com.brother.server.codec.OrderFrameDecoder;
import com.brother.server.codec.OrderFrameEncoder;
import com.brother.server.codec.OrderProtocolDecoder;
import com.brother.server.codec.OrderProtocolEncoder;
import com.brother.server.handler.OrderServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import java.util.concurrent.ExecutionException;

public class Server {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.group(new NioEventLoopGroup());


        UnorderedThreadPoolEventExecutor pool = new UnorderedThreadPoolEventExecutor(10);
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(pool, new OrderServerProcessHandler());

            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

        channelFuture.channel().closeFuture().get();

    }

}
