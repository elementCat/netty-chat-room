  package com.netty.server;

  /*
   * netty服务端--处理器
   */

  import io.netty.channel.*;
  import io.netty.handler.timeout.IdleState;
  import io.netty.handler.timeout.IdleStateEvent;
  import lombok.extern.slf4j.Slf4j;

  @Slf4j
  public class ServerTrigger extends ChannelInboundHandlerAdapter {


      @Override
      public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
          if(evt instanceof IdleStateEvent){
             IdleStateEvent event = (IdleStateEvent) evt;
             if(event.state().equals(IdleState.ALL_IDLE)){
                 System.out.println("长时间挂机，断开");
                 ChannelFuture future = ctx.writeAndFlush("长时间无操作，会话断开！");
                 future.addListener((ChannelFutureListener) future1 -> ctx.channel().close());
             }else {
                 log.error("没断开");
             }
          }else {
              super.userEventTriggered(ctx, evt);
          }
      }
  }
