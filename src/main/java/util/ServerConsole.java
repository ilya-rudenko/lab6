package util;

import collections.MovieCollection;
import command.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stream.ServerStreamManager;
import stream.StreamManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class ServerConsole {






    ServerStreamManager streamManager;
    MovieCollection collection;
    CommandManager manager;

    CommandPackage path;

    Map<String, CommandInterface> commandMap;
    LinkedList<String> commandHistory;

    Integer recursionLength;

    public ServerConsole(){
        streamManager = new ServerStreamManager();
        collection=new MovieCollection(streamManager);
        commandHistory= new LinkedList<String>();
//        recursionLength=0;

        manager = new CommandManager(new AddCommand(collection),new ShowCommand(collection),new ExitCommand(collection),new UpdateCommand(collection),new RemoveCommand(collection),new ClearCommand(collection),new SaveCommand(collection),new LoadCommand(collection),new ServerExecuteCommand(this),new AddIfMaxCommand(collection),new RemoveLowerCommand(collection),new ServerHistoryCommand(this),new MinByOscarsCountCommand(collection),new CountGreaterCommand(collection),new PrintAscendingCommand(collection),new HelpCommand(collection), new InfoCommand(collection));
        commandMap = new HashMap<String, CommandInterface>();
        commandMap.put("add",arg-> manager.add(arg));
        commandMap.put("show",arg-> manager.show());
        commandMap.put("exit",arg->manager.exit(path));
        commandMap.put("update",arg-> manager.update(arg));
        commandMap.put("remove_by_id",arg-> manager.remove(arg));
        commandMap.put("clear",arg-> manager.clear());
//        commandMap.put("save",arg->manager.save(arg));
//        commandMap.put("load",arg->manager.load(arg));
//        commandMap.put("execute_script",arg -> manager.execute(arg));
        commandMap.put("add_if_max",arg->manager.addIfMax(arg));
        commandMap.put("remove_lower",arg->manager.removeLower(arg));
//        commandMap.put("history",arg->manager.returnHistory());
        commandMap.put("min_by_oscars_count",arg -> manager.minByOscarsCount(arg));
        commandMap.put("count_greater_than_oscars_count",arg->manager.countGreater(arg));
        commandMap.put("print_ascending",arg->manager.printAscending());
        commandMap.put("help",arg -> manager.help());
        commandMap.put("info",arg->manager.info());


    }

    public void run() {
        if (System.getenv("DATAPATH") != null && collection.load(new CommandPackage(System.getenv("DATAPATH")))) {
            path = new CommandPackage(System.getenv("DATAPATH"));
        } else {
            path = new CommandPackage("data.json");
            collection.load(path);
        }
        CommandPackage commandPackage;

        while (true) {
                commandPackage=streamManager.receivePackage();

                if (commandMap.get(commandPackage.getCommand()) != null) {
                    commandMap.get(commandPackage.getCommand()).execute(commandPackage);
                } else {
                    streamManager.sendPackage("There is no such command", 0);
                }

        }
    }

}
