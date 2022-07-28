package com.gucardev.collaborativewebbe.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.gucardev.collaborativewebbe.model.Message;
import com.gucardev.collaborativewebbe.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketModule {


    private final SocketService socketService;
    private final SocketIOServer server;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.socketService = socketService;
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("document_write", Message.class, onChatReceived());

    }


    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            for (SocketIOClient client : senderClient.getNamespace().getRoomOperations("room1").getClients()) {
                if (!client.getSessionId().equals(senderClient.getSessionId())) {
                    client.sendEvent("document_read",
                            Message.builder().data(data.getData()).type(data.getType()).build());
                }
            }
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {
            client.joinRoom("room1");
            log.info("Socket ID[{}] - Connected to chat module through", client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
        };
    }


}
