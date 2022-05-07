package command;

import collections.MovieCollection;
import util.CommandPackage;

public class LoadCommand implements Command{
    private MovieCollection collection;

    public LoadCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.load(arg);
    }
}
