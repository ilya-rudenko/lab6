package command;

import collections.MovieCollection;
import util.CommandPackage;

public class CountGreaterCommand implements Command{
    private MovieCollection collection;

    public CountGreaterCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.countGreater(arg);
    }
}