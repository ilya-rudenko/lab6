package command;

import collections.MovieCollection;
import util.CommandPackage;

public class InfoCommand implements Command{
    private MovieCollection collection;

    public InfoCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.info();
    }
}