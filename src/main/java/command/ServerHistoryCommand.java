package command;

import util.CommandPackage;
import util.ServerConsole;


public class ServerHistoryCommand implements Command{
    private ServerConsole console;

    public ServerHistoryCommand(ServerConsole console){
        this.console=console;
    }

    @Override
    public void execute(CommandPackage arg) {

    }
}