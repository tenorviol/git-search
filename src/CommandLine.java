package com.github.tenorviol.gitsearch;

import java.util.Arrays;
import java.util.List;

public class CommandLine {
  public String       indexPath;
  public String       command;
  public List<String> commandArgs;
  
  public CommandLine(String[] args) throws IllegalArgumentException {
    List<String> list = Arrays.asList(args);

    parseCommand(list);
    
    if ("index".equals(command)) {
      parseIndexArgs();
    } else if ("query".equals(command)) {
      parseQueryArgs();
    } else {
      throw new IllegalArgumentException("Unknown command, '" + command + "'");
    }
  }
  
  private void parseCommand(List<String> args) throws IllegalArgumentException {
    if (args.size() < 2 || !"--index".equals(args.get(0))) {
      throw new IllegalArgumentException("--index <path> required");
    }
    indexPath = args.get(1);
    
    if (args.size() < 3) {
      throw new IllegalArgumentException("No command given");
    }
    command = args.get(2);
    commandArgs = args.subList(3, args.size());
  }
  
  private void parseIndexArgs() throws IllegalArgumentException {
    if (commandArgs.size() == 0) {
      throw new IllegalArgumentException("No path(s) given");
    }
  }
  
  private void parseQueryArgs() throws IllegalArgumentException {
    if (commandArgs.size() == 0) {
      throw new IllegalArgumentException("No search query given");
    }
  }
  
  public static void help() {
    System.out.println("java com.github.tenorviol.gitsearch.CommandLine --index <index path> index <path(s)>");
    //String usage = "java org.apache.lucene.demo.IndexFiles"
    //             + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
    //             + "This indexes the documents in DOCS_PATH, creating a Lucene index"
    //             + "in INDEX_PATH that can be searched with SearchFiles";
    System.out.println("java com.github.tenorviol.gitsearch.CommandLine --index <index path> query <query term(s)>");
    //String usage =
    //  "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/java/4_0/demo.html for details.";
  }
  
  public static void main(String[] args) throws IllegalArgumentException {
    help();
    CommandLine cl = new CommandLine(args);
    System.out.println(cl);
  }
}
