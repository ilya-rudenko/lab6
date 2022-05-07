package command;

import collections.MovieCollection;
import util.CommandPackage;

public class ClearCommand implements Command{
    private MovieCollection collection;

    public ClearCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.clear();
    }
}
