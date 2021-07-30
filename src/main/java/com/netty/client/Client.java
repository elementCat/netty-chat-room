  package com.netty.client;

  /*
   * netty客户端
   */

  import io.netty.bootstrap.Bootstrap;
  import io.netty.channel.Channel;
  import io.netty.channel.ChannelFuture;
  import io.netty.channel.ChannelInitializer;
  import io.netty.channel.EventLoopGroup;
  import io.netty.channel.nio.NioEventLoopGroup;
  import io.netty.channel.socket.nio.NioSocketChannel;
  import io.netty.handler.codec.string.StringDecoder;
  import io.netty.handler.codec.string.StringEncoder;
  import io.netty.handler.timeout.IdleStateHandler;
  import io.netty.util.HashedWheelTimer;
  import lombok.extern.slf4j.Slf4j;

  import java.io.BufferedReader;
  import java.io.InputStreamReader;
  import java.util.concurrent.TimeUnit;

  @Slf4j
  public class Client {

      public static void main(String[] a) {
          //服务类
          Bootstrap bootstrap = new Bootstrap();
         //worker线程池
          EventLoopGroup worker = new NioEventLoopGroup();
          String hostName ="192.168.11.15";
          int port =10001;
          try{
              //设置线程池
              bootstrap.group(worker)
              //设置sokcet工厂
               .channel(NioSocketChannel.class)
              //设置管道工厂
              .handler(new ChannelInitializer<Channel>() {
                  @Override
                  protected void initChannel(Channel ch) {
                      ch.pipeline().addLast(new StringDecoder())
                              .addLast(new StringEncoder())
                              .addLast(new ClientHandler());
                  }
              });
              //连接服务器
             ChannelFuture future = bootstrap.connect(hostName,port);

              BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
              while (true){
                  log.info("input:");
                  String msg = reader.readLine();
                  future.channel().writeAndFlush(msg);
              }
          }catch (Exception e){
              e.printStackTrace();
          }finally {
              //释放资源
              worker.shutdownGracefully();
          }
      }
  }
