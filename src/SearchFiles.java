package com.github.tenorviol.gitsearch;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class SearchFiles {

  private SearchFiles() {}

  /** Simple command-line based search demo. */
  public static void search(CommandLine cl) throws Exception {
    String index = "index";
    String field = "contents";
    String queries = null;
    int repeat = 0;
    boolean raw = false;
    String queryString = "";
    int hitsPerPage = 25;

    Iterator it = cl.commandArgs.iterator();
    while (it.hasNext()) {
      queryString += " " + it.next();
    }

    IndexReader reader = IndexReader.open(FSDirectory.open(new File(cl.indexPath)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);

    QueryParser parser = new QueryParser(Version.LUCENE_31, field, analyzer);

    String line = queryString;
    line = line.trim();

    Query query = parser.parse(line);
    System.out.println("Searching for: " + query.toString(field));

    if (repeat > 0) {                           // repeat & time as benchmark
      Date start = new Date();
      for (int i = 0; i < repeat; i++) {
        searcher.search(query, null, 100);
      }
      Date end = new Date();
      System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
    }

    doPagingSearch(searcher, query, hitsPerPage);

    searcher.close();
    reader.close();
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   *
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   *
   */
  public static void doPagingSearch(IndexSearcher searcher, Query query, int hitsPerPage) throws IOException {
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;

    int numTotalHits = results.totalHits;
    System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
    end = Math.min(hits.length, start + hitsPerPage);

    for (int i = start; i < end; i++) {
      System.out.print((i+1) + ". ");
      Document doc = searcher.doc(hits[i].doc);
      String path = doc.get("path");
      if (path != null) {
        System.out.print(path);
        String title = doc.get("title");
        if (title != null) {
          System.out.print("   Title: " + doc.get("title"));
        }
      } else {
        System.out.print("No path for this document");
      }
      System.out.println(" (" + hits[i].score + ")");
    }
  }
}
