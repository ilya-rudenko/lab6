package command;

import collections.MovieCollection;
import util.CommandPackage;


public class AddCommand implements Command{
    private MovieCollection collection;

    public AddCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.add(arg);
    }
}
