package model;

import java.net.Socket;

public class HttpModel {
    private Socket socket;

    //Constructor
    public HttpModel(Socket socket) {
        this.socket = socket;
    }

    //Getter

    public Socket getSocket() {
        return socket;
    }
}
