/*
 * Copyright: 20210729 BY CXK
 */
package com.netty.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StartMultiClinent {
    public static void main(String[] a){
        MultiClient client = new MultiClient();
        client.init(5);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                System.out.println("please input:");
                String msg = reader.readLine();
                client.nextChannel().writeAndFlush(msg);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
             //   client.nextChannel().close();
            }

        }
    }

}
 
