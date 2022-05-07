package command;

import collections.MovieCollection;
import util.CommandPackage;

public class RemoveCommand implements Command{
    private MovieCollection collection;

    public RemoveCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.remove(arg);
    }
}