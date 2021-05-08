package myLuceneApp;

// tested for lucene 7.7.2 and jdk13
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import utils.IO;

/**
 *
 * @author Tonia Kyriakopoulou
 */
public class SearcherDemo {

    public SearcherDemo(){
        try{
            String indexLocation = ("index"); //define where the index is stored
            String field = "contents"; //define which field will be searched            

            //Access the index using indexReaderFSDirectory.open(Paths.get(index))
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation))); //IndexReader is an abstract class, providing an interface for accessing an index.
            IndexSearcher indexSearcher = new IndexSearcher(indexReader); //Creates a searcher searching the provided index, Implements search over a single IndexReader.
            indexSearcher.setSimilarity(new ClassicSimilarity());

            //Search the index using indexSearcher
            search(indexSearcher, field);

            //Close indexReader
            indexReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Searches the index given a specific user query.
     */
    private void search(IndexSearcher indexSearcher, String field){
        try{
            // define which analyzer to use for the normalization of user's query
            Analyzer analyzer = new EnglishAnalyzer();

            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);

            //read querries from file
            HashMap<Integer, String> querries = new HashMap<>();
            querries = querryParser("docs/cran.qry");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter number of articles or zero to quit: ");
            System.out.print(">>");
            int k = scanner.nextInt();

            while(k>0){
                FileWriter file = new FileWriter("myIRfor_"+k+".txt");
                for (Integer querryId:querries.keySet()) {
                    String querry = querries.get(querryId).replace("?","");

                    // parse the query according to QueryParser
                    Query query = parser.parse(querry);
                    System.out.println("Searching for: " + query.toString(field));

                    // search the index using the indexSearcher
                    TopDocs results = indexSearcher.search(query, k);
                    ScoreDoc[] hits = results.scoreDocs;
                    long numTotalHits = results.totalHits;
                    System.out.println(numTotalHits + " total matching documents");

                    //display and save results
                    for (int i = 0; i < hits.length; i++) {
                        Document hitDoc = indexSearcher.doc(hits[i].doc);
                        //System.out.println("\tscore " + hits[i].score + "\tid " + hitDoc.get("id"));
                        file.write(querryId + " 0 " +hitDoc.get("id") + " 0 " +hits[i].score + " myIRfor_"+k+"\n");

                    }

                }
                file.close();
                System.out.println("Enter number of articles or zero to quit: ");
                System.out.print(">>");
                k = scanner.nextInt();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private HashMap<Integer, String> querryParser(String file) throws FileNotFoundException {
        HashMap<Integer, String> querries = new HashMap<Integer, String>();
        String txt_file = IO.ReadEntireFileIntoAString(file);
        String[] docs = txt_file.split("\n.I ");
        System.out.println("Read: " + docs.length + " docs");
        for (String doc:docs){
            querries.put(Integer.parseInt(doc.replace(".I ","").substring(0, 3)), doc.split(".W")[1]);
        }
        return querries;

    }


//    try {
//        File myObj = new File("filename.txt");
//        if (myObj.createNewFile()) {
//            System.out.println("File created: " + myObj.getName());
//        } else {
//            System.out.println("File already exists.");
//        }
//    } catch (IOException e) {
//        System.out.println("An error occurred.");
//        e.printStackTrace();
//    }

    /**
     * Initialize a SearcherDemo
     */
    public static void main(String[] args) throws FileNotFoundException{
        SearcherDemo searcherDemo = new SearcherDemo();
    }
}