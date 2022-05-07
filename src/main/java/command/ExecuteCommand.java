package command;

import collections.MovieCollection;
import util.ClientConsole;
import util.CommandPackage;
import util.Console;

public class ExecuteCommand implements Command{
    private MovieCollection collection;
    private Console console;

    public ExecuteCommand(Console console){
        this.console=console;
    }

    @Override
    public void execute(CommandPackage arg) {
//        console.executeScript(arg);
    }
}
