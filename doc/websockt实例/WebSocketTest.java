package com.holley.wxcharging.action.charging;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/websocket")
public class WebSocketTest {

    public WebSocketTest() {
        System.out.println("initntitnnt");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        System.out.println("***************");
        session.getBasicRemote().sendText("  this is  message");
        int sentMessage = 0;
        while (sentMessage < 3) {
            Thread.sleep(5000);
            session.getBasicRemote().sendText(" this is one mess " + sentMessage);
            sentMessage++;
        }
        session.getBasicRemote().sendText(" message send over ");
    }

    @OnOpen
    public void onOpen() {
        System.out.println(" client connected ");
    }

    @OnClose
    public void onClose() {
        System.out.println(" connection closed ");
    }
}
