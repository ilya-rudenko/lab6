package util;

import collections.MovieCollection;
import command.*;
import data.Movie;
import stream.StreamManager;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class ClientConsole {
    StreamManager streamManager;
    CommandManager manager;

    String path;

    Map<String,TypeOfArgumentEnum> commandMap;
    LinkedList<String> commandHistory;

    Integer recursionLength;

    DatagramSocket socket;

    static final int PORT = 50001;
    static ByteBuffer inputBuffer;
    static ByteBuffer outputBuffer;
    InetAddress address;

    public ClientConsole(){
        commandMap = new HashMap<String,TypeOfArgumentEnum>();
        commandMap.put("add",TypeOfArgumentEnum.MOVIE);
        commandMap.put("show",TypeOfArgumentEnum.NULL);
        commandMap.put("exit",TypeOfArgumentEnum.NULL);
        commandMap.put("update",TypeOfArgumentEnum.INTEGER_MOVIE);
        commandMap.put("remove_by_id",TypeOfArgumentEnum.INTEGER);
        commandMap.put("clear",TypeOfArgumentEnum.NULL);
//        commandMap.put("save",TypeOfArgumentEnum.NULL);
//        commandMap.put("load",TypeOfArgumentEnum.NULL);
//        commandMap.put("execute_script",TypeOfArgumentEnum.STRING);
        commandMap.put("add_if_max",TypeOfArgumentEnum.MOVIE);
        commandMap.put("remove_lower",TypeOfArgumentEnum.MOVIE);
//        commandMap.put("history",TypeOfArgumentEnum.NULL);
        commandMap.put("min_by_oscars_count",TypeOfArgumentEnum.NULL);
        commandMap.put("count_greater_than_oscars_count",TypeOfArgumentEnum.INTEGER);
        commandMap.put("print_ascending",TypeOfArgumentEnum.NULL);
        commandMap.put("help",TypeOfArgumentEnum.NULL);
        commandMap.put("info",TypeOfArgumentEnum.NULL);

        streamManager= new StreamManager(false);
        commandHistory= new LinkedList<String>();
        recursionLength=0;
    }

    public void run(){
        String inputtedCommand;
        String command;
        String argument;

        Integer number;
        String string;
        Movie movie;
        CommandPackage commandPackage;

        while(true) {
            try {
                inputBuffer = ByteBuffer.allocate(10240);
                outputBuffer = ByteBuffer.allocate(10240);

                address = InetAddress.getByName("localhost");
                socket = new DatagramSocket();
                socket.setSoTimeout(1000);

                while (true) {
                    inputtedCommand = streamManager.stringRequest("Enter the command: ", false);

                    String[] commandArray = inputtedCommand.trim().split(" ");
                    if (commandArray.length > 2) {
                        streamManager.print("Too much arguments in command", 0);
                        return;
                    }

                    command = commandArray[0];
                    if (commandArray.length == 1) argument = null;
                    else argument = commandArray[1];

                    if (command.equals("history")){
                        returnHistory();
                    }
                    else if (command.equals("execute_script")){
                        executeScript(argument);
                    }

                    else if (commandMap.get(command) != null) {
                        if (commandMap.get(command) == TypeOfArgumentEnum.INTEGER) {
                            try {
                                number = Integer.valueOf(argument);
                                commandPackage = new CommandPackage(command, number);
                                sendPackage(commandPackage);
                            } catch (Exception e) {
                                streamManager.print("Wrong argument", 0);
                            }
                        }
                        else if (commandMap.get(command) == TypeOfArgumentEnum.INTEGER_MOVIE) {
                            try {
                                number = Integer.valueOf(argument);
                                movie=Movie.collectMovie(streamManager);

                                commandPackage = new CommandPackage(command, number,movie);
                                sendPackage(commandPackage);
                            } catch (Exception e) {
                                streamManager.print("Wrong argument", 0);
                            }
                        }

                        else if (commandMap.get(command) == TypeOfArgumentEnum.STRING) {
                            if (argument==null){
                                streamManager.print("Wrong argument", 0);
                                continue;
                            }
                            string=argument;
                            commandPackage = new CommandPackage(command,string);
                            sendPackage(commandPackage);

                        }

                        else if (commandMap.get(command) == TypeOfArgumentEnum.MOVIE) {
                            movie=Movie.collectMovie(streamManager);
                            commandPackage=new CommandPackage(command,movie);
                            sendPackage(commandPackage);
                        }

                        else {
                            commandPackage = new CommandPackage(command);
                            sendPackage(commandPackage);
                            if (command.equals("exit")) System.exit(1);
                        }

                        if (commandHistory.size() >= 13) {
                            commandHistory.addFirst(inputtedCommand);
                            commandHistory.removeLast();
                        } else {
                            commandHistory.addFirst(inputtedCommand);
                        }
                    } else {
                        streamManager.print("There is no such command", 0);
                    }
                }
            } catch (Exception e) {
                streamManager.print("Error with server connection...",0);
                e.printStackTrace();
                try{
                    Thread.sleep(1000);
                }
                catch (Exception err){
                    continue;
                }
            }
        }
    }

    public void sendPackage(CommandPackage commandPackage){
        try{
            outputBuffer.clear();
            outputBuffer.put(serialize(commandPackage));
            outputBuffer.flip();

            DatagramPacket request = new DatagramPacket(outputBuffer.array(), outputBuffer.limit(), address, PORT);
            socket.send(request);
            outputBuffer.clear();

            inputBuffer.clear();
            DatagramPacket response = new DatagramPacket(inputBuffer.array(),inputBuffer.capacity());
            try {
                socket.receive(response);
                ServerResponse serverResponse = (ServerResponse)deserialize(inputBuffer.array());
                streamManager.print(serverResponse.getResponse(),serverResponse.getType());
                inputBuffer.clear();
            }
            catch (SocketTimeoutException ex) {
                streamManager.print("Cannot connect to server...",0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            streamManager.print("Something went wrong during sending command to server...", 0);
        }
    }

    public void executeScript (String path){
        String command;
        String argument;

        Integer number;
        String string;
        Movie movie;
        CommandPackage commandPackage;

        if (recursionLength>=10) {
            streamManager.print("Reached recursion length limit",0);
            return;
        }
        recursionLength+=1;
        try(BufferedReader br = new BufferedReader (new FileReader(path)))
        {
            StreamManager fileStreamManager=new StreamManager(true);
            fileStreamManager.setReader(br);


            String s;
            while((s=br.readLine())!=null){
                String[] commandArray = s.trim().split(" ");
                if (commandArray.length > 2) {
                    streamManager.print("Too much arguments in command", 0);
                    return;
                }

                command = commandArray[0];
                if (commandArray.length == 1) argument = null;
                else argument = commandArray[1];

                if (command.equals("history")){
                    returnHistory();
                }
                else if (command.equals("execute_script")){
                    executeScript(argument);
                }

                else if (commandMap.get(command) != null) {

                    if (commandMap.get(command) == TypeOfArgumentEnum.INTEGER) {
                        try {
                            number = Integer.valueOf(argument);
                            commandPackage = new CommandPackage(command, number);
                            sendPackage(commandPackage);
                        } catch (Exception e) {
                            streamManager.print("Wrong argument", 0);
                        }
                    }
                    else if (commandMap.get(command) == TypeOfArgumentEnum.INTEGER_MOVIE) {
                        try {
                            number = Integer.valueOf(argument);
                            movie=Movie.collectMovie(streamManager);

                            commandPackage = new CommandPackage(command, number,movie);
                            sendPackage(commandPackage);
                        } catch (Exception e) {
                            streamManager.print("Wrong argument", 0);
                        }
                    }

                    else if (commandMap.get(command) == TypeOfArgumentEnum.STRING) {

                        string=argument;
                        commandPackage = new CommandPackage(command,string);
                        sendPackage(commandPackage);

                    }

                    else if (commandMap.get(command) == TypeOfArgumentEnum.MOVIE) {
                        movie=Movie.collectMovie(fileStreamManager);
                        commandPackage=new CommandPackage(command,movie);
                        sendPackage(commandPackage);
                    }

                    else {
                        commandPackage = new CommandPackage(command);
                        sendPackage(commandPackage);
                        if (command.equals("exit")) System.exit(1);
                    }

                    if (commandHistory.size() >= 13) {
                        commandHistory.addFirst(s);
                        commandHistory.removeLast();
                    } else {
                        commandHistory.addFirst(s);
                    }
                } else {
                    streamManager.print("There is no such command", 0);
                }
            }
        }
        catch(Exception ex){
            streamManager.print("Cannot open the file",0);
        }
    }

    public void returnHistory(){
        streamManager.print(commandHistory);
    }
}
