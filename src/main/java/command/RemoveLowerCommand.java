package command;

import collections.MovieCollection;
import util.CommandPackage;

public class RemoveLowerCommand implements Command{
    private MovieCollection collection;

    public RemoveLowerCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.removeLower(arg);
    }
}