package command;

import util.CommandPackage;

@FunctionalInterface
public interface CommandInterface {
    void execute(CommandPackage arg);
}
