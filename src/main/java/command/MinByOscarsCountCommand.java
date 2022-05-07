package command;

import collections.MovieCollection;
import util.CommandPackage;


public class MinByOscarsCountCommand implements Command{
    private MovieCollection collection;

    public MinByOscarsCountCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.minByOscarsCount();
    }
}
