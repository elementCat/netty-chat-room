  package com.netty.client;

  /*
   * netty客户端--处理器
   */

  import io.netty.channel.ChannelHandlerContext;
  import io.netty.channel.SimpleChannelInboundHandler;
  import lombok.extern.slf4j.Slf4j;

  @Slf4j
  public class ClientHandler extends SimpleChannelInboundHandler<String> {


      @Override
      protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception {
          log.info("客服端收到消息："+s);
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
          log.error("客户端出现异常");
      }
  }
