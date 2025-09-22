package com.example.boot3.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocketServer
 * 一个ws协议的Controller
 * var reqUrl = "http://localhost:8081/websocket/" + cid;
 * socket = new WebSocket(reqUrl.replace("http", "ws"));
 * @author yuez
 * @since 2024/3/27
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {
    private static AtomicInteger onLineCount = new AtomicInteger(0);
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private String sid;
    private Session session;

    ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(@PathParam("sid")String sid,Session session){
        log.info("连接建立中 ==> session_id = {}， sid = {}", session.getId(), sid);
        sessionMap.put(sid, session);
        onLineCount.incrementAndGet();
        this.sid = sid;
        this.session = session;
        sendToOne(sid,"连接成功");
        log.info("连接建立成功，当前在线数为：{} ==> 开始监听新连接：session_id = {}， sid = {},。", onLineCount, session.getId(), sid);
    }

    @OnClose
    public void onClose(@PathParam("sid")String sid,Session session){
        sessionMap.remove(sid);
        onLineCount.decrementAndGet();
        log.info("连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：session_id = {}， sid = {},。", onLineCount, session.getId(), sid);
    }

    @OnMessage
    public void onMessage(String message,Session session) throws JsonProcessingException {
        //{"sid":"user-1","message":"hello websocket"}
        log.info("收到消息：{}", message);
        Map<String, String> stringMap = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {
        });
        String toSid = stringMap.get("sid");
        String msg = stringMap.get("message");
        if(toSid == null || "".equals(toSid) || toSid.trim().length() == 0){
            sendToAll(msg);
        }else{
            sendToOne(toSid, msg);
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        log.error("WebSocket发生错误，错误信息为：" + error.getMessage());
        error.printStackTrace();
    }


    /**
     * 群发消息
     * @param msg
     */
    private void sendToAll(String msg) {
        sessionMap.forEach((toSid,toSession)->{
           if(!sid.equalsIgnoreCase(toSid)){
               log.info("服务端给客户端群发消息 ==> sid = {}, toSid = {}, message = {}", sid, toSid, msg);
               toSession.getAsyncRemote().sendText(msg);
           }
        });
    }


    /**
     * 单发消息
     * @param sid
     * @param msg
     */
    private void sendToOne(String sid, String msg) {
        Session toSession = sessionMap.get(sid);
        if(toSession == null){
            log.error("服务端给客户端发送消息，不存在：{}=》{}",sid,msg);
            return;
        }
        log.info("服务端给客户端发送消息 ==> sid = {}, toSid = {}, message = {}", this.sid, sid, msg);
        toSession.getAsyncRemote().sendText(msg);
    }


}
