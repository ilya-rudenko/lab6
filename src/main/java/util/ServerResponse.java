package util;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private int type;
    private String response;

    public int getType() {
        return type;
    }

    public String getResponse() {
        return response;
    }

    public ServerResponse(String response, int type){
        this.response=response;
        this.type=type;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "type=" + type +
                ", response='" + response + '\'' +
                '}';
    }
}
