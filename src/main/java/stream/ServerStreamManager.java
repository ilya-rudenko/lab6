package stream;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.CommandPackage;
import util.ServerResponse;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.TreeSet;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class ServerStreamManager {
//    private boolean fileMode;
    private BufferedReader reader;
    private final static Logger logger = LogManager.getLogger();
    static ByteBuffer inputBuffer;
    static ByteBuffer outputBuffer;
    static int PORT = 50001;
    SocketAddress remoteAdd;

    DatagramChannel server;

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_BLUE = "\u001B[34m";

    Scanner scanner;
    Gson gson = new Gson();

    public ServerStreamManager(){

        try {
                server = DatagramChannel.open();
                InetSocketAddress iAdd = new InetSocketAddress("localhost", PORT);
                server.bind(iAdd);
                logger.info("Server Started: " + iAdd);
                inputBuffer = ByteBuffer.allocate(10240);
                outputBuffer = ByteBuffer.allocate(10240);
        }
        catch (IOException e) {
            logger.error("Can't establish a connection. Shutting down...");
            System.exit(-1);
        }

    }
    public void logError(String line){
        logger.error(line);
    }
    public void logInfo(String line){
        logger.info(line);
    }

    public CommandPackage receivePackage(){
        try {
            remoteAdd=server.receive(inputBuffer);
            inputBuffer.flip();
            CommandPackage command = deserialize(inputBuffer.array());
            logInfo("Got package: " + command);
            inputBuffer.clear();

            return command;
        }
        catch (Exception e){
            logError("Wrong package: "+e.getMessage());
            return null;
        }

    }

    public void sendPackage(String line,int type){
            try {
                outputBuffer=ByteBuffer.allocate(10240);
                outputBuffer.put(serialize(new ServerResponse(line,type)));
                outputBuffer.flip();
                server.send(outputBuffer,remoteAdd);

                outputBuffer.clear();
                logInfo("Response was sent: "+new ServerResponse(line,type));
            }
            catch (Exception e){
                logError("Something went wrong with sending package: "+e.getMessage());
            }
    }



    public boolean writeToFile(String path,Object obj){
        try {
            new FileOutputStream(path).write(gson.toJson(obj).getBytes(StandardCharsets.UTF_8));
            return true;
        }
        catch (Exception e){
            logError("You dont have access to the file. The collection will not be saved or loaded");
            return false;
        }
    }

    public TreeSet<Movie> loadFromFile(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line="";
            StringBuilder jsonString= new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            return gson.fromJson(jsonString.toString(),new TypeToken<TreeSet<Movie>>() {}.getType());
        }
        catch (Exception e) {
            File file = new File(path);
            logError("There's something wrong with the file. File will be overwritten");
        }
        return null;
    }
}
