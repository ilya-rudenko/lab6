package command;

import collections.MovieCollection;
import util.CommandPackage;

public class ExitCommand implements Command{
    private MovieCollection collection;

    public ExitCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.save(arg);
    }
}
