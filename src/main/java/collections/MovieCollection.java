package collections;

import data.*;
import stream.ServerStreamManager;
import util.CommandPackage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class MovieCollection {
    TreeSet<Movie> collection;
    ServerStreamManager streamManager;
    java.time.LocalDateTime collectionCreationDate;

    private Integer lastId;

    private HashSet<Integer> usedIds;

    public MovieCollection(ServerStreamManager streamManager){
        collection =new TreeSet<Movie>();
        this.streamManager=streamManager;
        collectionCreationDate=java.time.LocalDateTime.now();
        lastId=0;
    }

    public void add(CommandPackage arg) {
        Movie temp = arg.getMovie();
        if (Movie.validateMovie(temp,false)) {
            temp.setId(lastId + 1);
            lastId += 1;
            temp.setTime(java.time.LocalDateTime.now());
            collection.add(temp);

            streamManager.sendPackage("Element was successfully added", 1);
        }
        else{
            streamManager.sendPackage("Incorrect movie",0);
        }
    }

    public void addIfMax(CommandPackage arg){
        Movie temp = arg.getMovie();
        if (Movie.validateMovie(temp,false)) {
            temp.setId(lastId + 1);
            lastId += 1;

            boolean flag = collection.stream().anyMatch(movie -> movie.compareToByOscarsCount(temp) > 0);
            if (flag) {
                streamManager.sendPackage("Element wasn't added", 1);
            } else {
                collection.add(temp);
                streamManager.sendPackage("Element was successfully added", 1);
            }
        }
        else{
            streamManager.sendPackage("Incorrect movie",0);
        }
    }

    public void removeLower(CommandPackage arg){
        Movie temp = arg.getMovie();
        if (Movie.validateMovie(temp,false)) {
            collection = collection.stream().filter(e -> e.compareToByOscarsCount(temp) >= 0).collect(Collectors.toCollection(TreeSet::new));
            streamManager.sendPackage("Elements were successfully deleted", 1);
        }
        else{
            streamManager.sendPackage("Incorrect movie",0);
        }
    }

    public void minByOscarsCount(){
        if (collection.isEmpty()){
            streamManager.sendPackage("Collection is empty",2);
            return;
        }
        streamManager.sendPackage(collection.stream().min(Movie::compareToByOscarsCount).toString(),2);
    }

    public void update(CommandPackage arg){
        try{
            Integer id=Integer.valueOf(arg.getNumber());
            Boolean flag =delete(id);
            if (flag==Boolean.TRUE){
                Movie temp =arg.getMovie();
                temp.setId(id);

                if (Movie.validateMovie(temp,false)){
                    collection.add(temp);
                    streamManager.sendPackage("Element was successfully updated",1);
                }
                else{
                    streamManager.sendPackage("Incorrect movie",0);
                }

            }
            else if(flag==Boolean.FALSE){
                streamManager.sendPackage("No such ID in collection",0);
            }
        }
        catch (Exception e){
            streamManager.sendPackage("Argument is not an integer",0);
        }
    }

    public void remove(CommandPackage arg){
        try{
            Integer id=Integer.valueOf(arg.getNumber());
            Boolean flag = delete(id);
            if (flag==Boolean.TRUE){
                streamManager.sendPackage("Element was successfully deleted",1);
            }
            else if(flag==Boolean.FALSE) {
                streamManager.sendPackage("No such ID in collection",0);
            }
            else {
                streamManager.sendPackage("Collection is empty",1);
            }
        }
        catch (Exception e){
            streamManager.sendPackage("Argument is not an integer",0);
        }
    }
    public void countGreater(CommandPackage arg){

        try {
            Integer count = Integer.valueOf(arg.getNumber());
            Integer res = 0;
            res = Math.toIntExact(collection.stream().filter(movie -> movie.getOscarsCount() > count).count());
            streamManager.sendPackage(String.valueOf(res),2);
        }
        catch(Exception e) {
            streamManager.sendPackage("Argument is not a number",0);
        }
    }
    public void printAscending(){
        print();
    }

    public Boolean delete(Integer id){
        if (collection.isEmpty())  {
//            streamManager.print("Collection is empty",1);
            return null;
        }

        for (Movie movie: collection){
            if (movie.getId()==id){
                collection.remove(movie);
                return true;
            }
        }

        return false;
    }

    public void clear(){
        collection.clear();
        streamManager.sendPackage("Collection was cleared successfully",1);
    }

    public boolean save(CommandPackage arg){
        if (streamManager.writeToFile(arg.getCommand(),collection)) {
            return true;
        }
        return false;
    }


    public boolean load(CommandPackage arg){
        if (streamManager.loadFromFile(arg.getCommand())!=null){
            collection =streamManager.loadFromFile(arg.getCommand());
            lastId=0;

            collection=collection.stream().filter(movie->Movie.validateMovie(movie,true)).collect(Collectors.toCollection(TreeSet::new));

            collection.stream().forEach(e->{
                if (e.getId() >= lastId) lastId = e.getId() + 1;
            });

            return true;
        }
        streamManager.writeToFile(arg.getCommand(),collection);
        return true;
    }

    public void print(){
        String line="";
        if (collection.isEmpty()){
            streamManager.sendPackage("Collection is empty",2);
        }
        else {
            for (Movie movie: collection.stream().sorted(new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return (int)(o1.getManhattan()-o2.getManhattan());
                }
            }).collect(Collectors.toList())) line+=movie.toString()+"\n";
        }
        streamManager.sendPackage(line,2);
    }

    public void help(){
        streamManager.sendPackage(
                    "help                                           : just help" + "\n"+
                        "info                                           : information about collection" + "\n"+
                        "show                                           : show all elements of collection" +"\n"+
                        "add {element}                                  : add element to collection" +"\n"+
                        "update id {element}                            : update element by id" +"\n"+
                        "remove_by_id id                                : remove element by id" +"\n"+
                        "clear                                          : clear collection" +"\n"+
                        "save                                           : save collection to the file" + "\n"+
                        "execute_script file_name                       : execute script" +"\n"+
                        "exit                                           : exit from application" + "\n"+
                        "add_if_max {element}                           : add element if it has max oscars count" + "\n"+
                        "remove_lower {element}                         : remove all elements that lower than inputed element" + "\n"+
                        "history                                        : print last 13 commands" + "\n"+
                        "min_by_oscars_count                            : print element that has minimum of oscars count" + "\n"+
                        "count_greater_than_oscars_count oscarsCount    : print the amount of elements that has oscars more than oscarsCount" + "\n"+
                        "print_ascending                                : print elements in ascending order",2);
    }
    public void info(){
        streamManager.sendPackage(
                    "Collection type     : TreeSet"+"\n"+
                        "Amount of elements  : " + collection.size()+"\n"+
                        "Date of creation    : " + collectionCreationDate,2);
    }

}