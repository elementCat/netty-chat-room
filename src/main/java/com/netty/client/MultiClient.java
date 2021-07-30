/*
 * Copyright: 20210729 BY CXK
 */
 package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MultiClient {
   //服务类
   private Bootstrap bootstrap = new Bootstrap();
   //会话组
   private List<Channel> channels = new ArrayList<>();
   //主机名
   private String HOST_NAME ="192.168.11.15";
   //端口
   private int PORT = 10001;
   //引用计数器cc
   private final AtomicInteger index = new AtomicInteger();
   //初始化连接
   public void init(int count) {
    //worker线程池
    EventLoopGroup worker = new NioEventLoopGroup();
      //设置线程池
      bootstrap.group(worker)
              //设置sokcet工厂
              .channel(NioSocketChannel.class)
              //设置管道工厂
              .handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                  ch.pipeline().addLast(new StringDecoder())
                          .addLast(new StringEncoder())
                          .addLast(new ClientHandler());
                }
              });
      //连接服务器 多个
      for (int i = 0; i < count; i++) {
        ChannelFuture future = bootstrap.connect(HOST_NAME, PORT);
        channels.add(future.channel());
      }
      //释放资源
   //   worker.shutdownGracefully();
   }
  /**
   * 获取会话
   */
  public Channel nextChannel(){
    return getfirstActiveChannel(0);
  }

  private Channel getfirstActiveChannel(int count){
    Channel channel = channels.get(Math.abs(index.getAndIncrement()%channels.size()));
    if(!channel.isActive()){
      // todo 重连
       reconnect(channel);
      if(count>channels.size()){
        throw new RuntimeException("无可用的channel");
      }
      return getfirstActiveChannel(count+1);
    }
    return channel;
  }

  /**
   * 重连
   */
  private void reconnect(Channel channel){
    synchronized (channel){
      if(channels.indexOf(channel)==-1){
        return;
      }
      Channel newChannel = bootstrap.connect(HOST_NAME, PORT).channel();
      channels.set(channels.indexOf(channel),newChannel);
    }
  }

  public void close(Channel channel){
      channel.close();
  }
}
 
