  package com.netty.server;

  /*
   * netty服务端--处理器
   */

  import io.netty.channel.*;
  import lombok.extern.slf4j.Slf4j;

  @Slf4j
  public class ServerHandler extends SimpleChannelInboundHandler<String> {


      @Override
      protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception {
          log.info("服务端收到消息："+s);
          ctx.writeAndFlush("hi! "+s);
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
          log.error("有异常：");
      }

      @Override
      public void channelActive(ChannelHandlerContext ctx) throws Exception {
          log.info("建立连接：");
      }

      @Override
      public void channelInactive(ChannelHandlerContext ctx) throws Exception {
          log.info("断开连接：");
      }
  }
