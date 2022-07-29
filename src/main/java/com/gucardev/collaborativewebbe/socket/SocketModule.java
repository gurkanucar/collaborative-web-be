package com.gucardev.collaborativewebbe.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.gucardev.collaborativewebbe.model.Message;
import com.gucardev.collaborativewebbe.model.Project;
import com.gucardev.collaborativewebbe.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketModule {


    private final ProjectService projectService;
    private final SocketIOServer server;

    public SocketModule(SocketIOServer server, ProjectService projectService) {
        this.projectService = projectService;
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("project_write", Message.class, onChatReceived());
        server.addEventListener("project_get", Project.class, projectGet());
        server.addEventListener("project_save", Project.class, projectSave());

    }

    private DataListener<Project> projectGet() {
        return (senderClient, data, ackSender) -> {
            senderClient.getNamespace().getRoomOperations(data.getRoom()).sendEvent("project_retrieved",
                    projectService.projectToJsonString(projectService.getOrCreateByDefaultValues(data.getRoom())));
        };
    }

    private DataListener<Project> projectSave() {
        return (senderClient, data, ackSender) -> {
            senderClient.getNamespace().getRoomOperations(data.getRoom()).sendEvent("project_saved",
                    projectService.projectToJsonString(projectService.update(data)));
        };
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(data.getRoom()).getClients()) {
                if (!client.getSessionId().equals(senderClient.getSessionId())) {
                    client.sendEvent("project_read",
                            Message.builder().data(data.getData()).type(data.getType()).build());
                }
            }
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {

            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Socket ID[{}] - room[{}]  Connected to chat module through", client.getSessionId().toString(),room);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
        };
    }


}
