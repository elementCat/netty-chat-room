  /*
   * Copyright: 2021 dingxiang-inc.com Inc. All rights reserved.
   */
  package com.netty.controller;


  import org.springframework.web.bind.annotation.*;

  @RestController
  @RequestMapping("/index")
  public class TestController {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String index(){
      return "hello world---";
    }
  }
