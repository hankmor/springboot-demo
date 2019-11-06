package com.belonk.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.UnsupportedEncodingException;

/**
 * Created by sun on 2019/11/6.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Component
public class MyWebsocketListener {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @EventListener
    public void whenAvailability(BrokerAvailabilityEvent event) {
        System.err.println("Broker now is available : " + event.isBrokerAvailable());
    }

    @EventListener
    public void whenConnected(SessionConnectedEvent event) {
        String user = event.getUser().getName();
        String s = "用户" + user + "成功连入聊天室";
        System.err.println(s);
        simpMessagingTemplate.convertAndSend("/topic/chat", s);
    }

    @EventListener
    public void whenConnecting(SessionConnectEvent event) {
        String user = event.getUser().getName();
        System.err.println("用户" + user + "正在连入聊天室");
    }

    @EventListener
    public void whenDisconnected(SessionDisconnectEvent event) {
        System.err.println("sessionId : " + event.getSessionId());
        try {
            System.err.println("message : " + new String(event.getMessage().getPayload(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.err.println("status : " + event.getCloseStatus());
        String user = event.getUser().getName();
        System.err.println("source : " + event.getSource());

        String s = "用户" + user + "已经退出聊天室";
        System.err.println(s);
        simpMessagingTemplate.convertAndSend("/topic/chat", s);
    }
}