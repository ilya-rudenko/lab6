package command;

import collections.MovieCollection;
import util.CommandPackage;

public class UpdateCommand implements Command{
    private MovieCollection collection;

    public UpdateCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.update(arg);
    }
}