  package com.netty.server;

  /*
   * netty服务端--处理器
   */

  import io.netty.channel.*;
  import io.netty.handler.timeout.IdleState;
  import io.netty.handler.timeout.IdleStateEvent;
  import lombok.extern.slf4j.Slf4j;

  @Slf4j
  public class ServerHandler extends SimpleChannelInboundHandler<String> {

      @Override
      protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception {
          log.info("服务端收到消息："+s);
          ctx.writeAndFlush("hi! "+s);
      }


      @Override
      public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
          if(evt instanceof IdleStateEvent){
             IdleStateEvent event = (IdleStateEvent) evt;
             if(event.state()==IdleState.ALL_IDLE){
                 ChannelFuture future = ctx.writeAndFlush("长时间无操作，会话断开！");
                 future.addListener((ChannelFutureListener) future1 -> ctx.channel().close());
             }
             else if (event.state().equals(IdleState.WRITER_IDLE)) {
                 System.out.println("WRITER_IDLE");
             } else if (event.state().equals(IdleState.READER_IDLE)) {
                 System.out.println("READER_IDLE");
             }
              super.userEventTriggered(ctx, evt);
          }else {
              super.userEventTriggered(ctx, evt);
          }
      }



      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
          log.error("有异常：");
          ctx.channel().close();
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
