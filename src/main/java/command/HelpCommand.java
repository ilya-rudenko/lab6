package command;

import collections.MovieCollection;
import util.CommandPackage;

public class HelpCommand implements Command{
    private MovieCollection collection;

    public HelpCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.help();
    }
}
