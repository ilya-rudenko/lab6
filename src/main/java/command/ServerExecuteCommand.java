package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerConsole;

public class ServerExecuteCommand implements Command{
    private MovieCollection collection;
    private ServerConsole console;

    public ServerExecuteCommand(ServerConsole console){
        this.console=console;
    }

    @Override
    public void execute(CommandPackage arg) {

    }
}
