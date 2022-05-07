package command;

import collections.MovieCollection;
import util.CommandPackage;

public class AddIfMaxCommand implements Command{
    private MovieCollection collection;

    public AddIfMaxCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.addIfMax(arg);
    }
}