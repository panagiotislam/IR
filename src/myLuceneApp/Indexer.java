package myLuceneApp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import txtparsing.*;

public class Indexer {

    public Indexer() throws Exception{
        
        String txtfile =  "docs/cran.all.1400"; //txt file to be parsed and indexed, it contains one document per line
        String indexLocation = ("index"); //define were to store the index        
        
        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexLocation + "'...");
            
            Directory dir = FSDirectory.open(Paths.get(indexLocation));
            // define which analyzer to use for the normalization of documents
            Analyzer analyzer = new EnglishAnalyzer();
            // define retrieval model 
            Similarity similarity = new ClassicSimilarity();
            // configure IndexWriter
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setSimilarity(similarity);

            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);

            // create the IndexWriter with the configuration as above 
            IndexWriter indexWriter = new IndexWriter(dir, iwc);
            
            // parse txt document using TXT parser and index it
            List<CranDoc> docs = TXTParsing.parse(txtfile);
            for (CranDoc doc : docs){
                indexDoc(indexWriter, doc);
            }
            
            indexWriter.close();
            
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
            
        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
        
        
    }

    private void indexDoc(IndexWriter indexWriter, CranDoc mydoc){
        
        try {
            
            // make a new, empty document
            Document doc = new Document();
            
            // create the fields of the document and add them to the document
            StoredField id = new StoredField("id", mydoc.getId());
            doc.add(id);
            StoredField title = new StoredField("title", mydoc.getTitle());
            doc.add(title);
            StoredField author = new StoredField("author", mydoc.getAuthor());
            doc.add(author);
            StoredField b = new StoredField("b", mydoc.getB());
            doc.add(b);
            StoredField body = new StoredField("body", mydoc.getBody());
            doc.add(body);
            String fullSearchableText = mydoc.getId() + " " + mydoc.getTitle() + " " + mydoc.getAuthor() + " " + mydoc.getB() + " " + mydoc.getBody();
            TextField contents = new TextField("contents", fullSearchableText, Field.Store.NO);
            doc.add(contents);
            
            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
                System.out.println("adding " + mydoc);
                indexWriter.addDocument(doc);
            } 
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Indexer indexer = new Indexer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
