package com.belonk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sun on 2019/10/25.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class MyWebsSocketHandler extends TextWebSocketHandler {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(MyWebsSocketHandler.class);
    private static final Map<String, WebSocketSession> WEB_SOCKET_SESSION_CACHE = new ConcurrentHashMap<>();
    static final String WEB_SOCKET_USER_NAME = "web_socket_user_name";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



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
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = (String) session.getAttributes().get(WEB_SOCKET_USER_NAME);
        WEB_SOCKET_SESSION_CACHE.put(userName, session);
        super.afterConnectionEstablished(session);
        log.info("User [" + userName + "] connect to the websocket.");
        log.info("Current connected user number : " + WEB_SOCKET_SESSION_CACHE.size());

        // 发送进入信息
        for (String user : WEB_SOCKET_SESSION_CACHE.keySet()) {
            WebSocketSession webSocketSession = WEB_SOCKET_SESSION_CACHE.get(user);
            webSocketSession.sendMessage(new TextMessage(nowStr() + ": 用户[" + userName + "]进入聊天室"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userName = (String) session.getAttributes().get(WEB_SOCKET_USER_NAME);
        WEB_SOCKET_SESSION_CACHE.remove(userName);
        super.afterConnectionClosed(session, status);
        log.info("User [" + userName + "] disconnect to the websocket.");
        log.info("Current connected user number : " + WEB_SOCKET_SESSION_CACHE.size());

        // 发送退出信息
        for (String user : WEB_SOCKET_SESSION_CACHE.keySet()) {
            WebSocketSession webSocketSession = WEB_SOCKET_SESSION_CACHE.get(user);
            webSocketSession.sendMessage(new TextMessage(nowStr() + ": 用户[" + user + "]退出聊天室"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("message : " + message);
        String curUser = (String) session.getAttributes().get(WEB_SOCKET_USER_NAME);
        // 群聊
        for (String userName : WEB_SOCKET_SESSION_CACHE.keySet()) {
            WebSocketSession webSocketSession = WEB_SOCKET_SESSION_CACHE.get(userName);
            webSocketSession.sendMessage(new TextMessage(nowStr() + " 用户[" + curUser + "]对大家说：<br>" + message.getPayload()));
        }
        super.handleTextMessage(session, message);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String nowStr() {
        return formatter.format(LocalDateTime.now());
    }
}