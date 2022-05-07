

package command;

import collections.MovieCollection;
import util.CommandPackage;


public class PrintAscendingCommand implements Command{

    private MovieCollection collection;

    public PrintAscendingCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public void execute(CommandPackage arg) {
        collection.printAscending();
    }
}