package com.github.tenorviol.gitsearch;

class GitSearch {
  public static void main(String[] args) {
    try {
      CommandLine cl = new CommandLine(args);
      
      if ("index".equals(cl.command)) {
        IndexFiles.index(cl);
      } else {
        SearchFiles.search(cl);
      }
      
    } catch (Exception e) {
      System.out.println(e.getMessage());
      CommandLine.help();
      System.exit(1);
    }
  }
}
