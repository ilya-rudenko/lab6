package command;

import collections.MovieCollection;
import util.CommandPackage;

public class CommandManager {
    Command add,update,remove,clear,minByOscarsCount,printAscending;
    Command show,save,load,execute,countGreater,help;
    Command exit,addIfMax,removeLower,returnHistory,info;

    public CommandManager(Command add,Command show,Command exit,Command update,Command remove,Command clear,Command save,Command load,Command execute,Command addIfMax,Command removeLower,Command returnHistory,Command minByOscarsCount, Command countGreater, Command printAscending,Command help,Command info){
        this.add=add;
        this.show=show;
        this.exit = exit;
        this.update=update;
        this.remove=remove;
        this.clear=clear;
        this.save=save;
        this.load=load;
        this.execute=execute;
        this.addIfMax=addIfMax;
        this.removeLower=removeLower;
        this.returnHistory=returnHistory;
        this.minByOscarsCount=minByOscarsCount;
        this.countGreater=countGreater;
        this.printAscending=printAscending;
        this.help=help;
        this.info=info;
    }

    public void add(CommandPackage arg){
        add.execute(arg);
    }
    public void addIfMax(CommandPackage arg){ addIfMax.execute(arg);}
    public void removeLower(CommandPackage arg){removeLower.execute(arg);}

    public void show(){
        show.execute(null);
    }
    public void exit(CommandPackage arg){
        exit.execute(arg);
    }
    public void update(CommandPackage arg) {update.execute(arg);}
    public void remove(CommandPackage arg) {remove.execute(arg);}
    public void clear() {clear.execute(null);}
//    public void save(CommandPackage arg) {save.execute(arg);}
//    public void load(CommandPackage arg) {load.execute(arg);}
//    public void execute(CommandPackage arg) {execute.execute(arg);}
//    public void returnHistory() {returnHistory.execute(null);}
    public void minByOscarsCount(CommandPackage arg){minByOscarsCount.execute(arg);}
    public void countGreater(CommandPackage arg){countGreater.execute(arg);}
    public void printAscending(){printAscending.execute(null);}
    public void help(){help.execute(null);}
    public void info(){info.execute(null);}
}
