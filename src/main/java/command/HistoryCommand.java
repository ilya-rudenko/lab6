package command;

import util.CommandPackage;
import util.Console;

public class HistoryCommand implements Command{
    private Console console;

    public HistoryCommand(Console console){
        this.console=console;
    }

    @Override
    public void execute(CommandPackage arg) {

    }
}