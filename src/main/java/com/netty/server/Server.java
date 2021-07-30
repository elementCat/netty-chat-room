  package com.netty.server;

  /*
   * netty服务端
   */

  import io.netty.bootstrap.ServerBootstrap;
  import io.netty.channel.*;
  import io.netty.channel.nio.NioEventLoopGroup;
  import io.netty.channel.socket.nio.NioServerSocketChannel;
  import io.netty.handler.codec.string.StringDecoder;
  import io.netty.handler.codec.string.StringEncoder;
  import io.netty.handler.timeout.IdleStateHandler;
  import lombok.extern.slf4j.Slf4j;

  @Slf4j
  public class Server {

      public static void main(String[] a) {
          //服务类
          ServerBootstrap bootstrap = new ServerBootstrap();
         //boss和worker 线程池
          EventLoopGroup boss = new NioEventLoopGroup();
          EventLoopGroup worker = new NioEventLoopGroup();
          Channel channel = null;
          int port =10001;
          try{
              //设置线程池
              bootstrap.group(boss,worker)
                //设置sokcet工厂
               .channel(NioServerSocketChannel.class)
                //设置tcp服务端 accept队列大小
                .option(ChannelOption.SO_BACKLOG,2048)
                 //设置长连接 TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                //允许较小的包发送 ，降低延迟
                .childOption(ChannelOption.TCP_NODELAY,true)
                 //设置管道工厂
                .childHandler(new ChannelInitializer<Channel>() {
                  @Override
                  protected void initChannel(Channel ch) throws Exception {
                      ch.pipeline().addLast(new IdleStateHandler(5,5,10))
                       .addLast(new ServerHandler())
                       .addLast(new ServerTrigger())
                       .addLast(new StringDecoder())
                       .addLast(new StringEncoder());
                  }
              });
              //绑定端口
              ChannelFuture future = bootstrap.bind(port).sync();
              log.info("-------服务端启动------");
              //等待服务端关闭
              if (future.isSuccess()) {
                  future.channel().closeFuture().sync();
                 log.info("[start][Netty Server 启动在 {} 端口]", port);
              }
          }catch (Exception e){
              e.printStackTrace();
          }finally {
              if (channel != null) {
                  channel.close();
              }
              //释放资源
              boss.shutdownGracefully();
              worker.shutdownGracefully();
          }
      }
  }
