package command;

import collections.MovieCollection;
import util.CommandPackage;

public class ShowCommand implements Command{
    private MovieCollection collection;

    public ShowCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.print();
    }
}
