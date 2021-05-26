package myLuceneApp;

//tested for lucene 7.7.3 and jdk13
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.classification.utils.DocToDoubleVectorUtils;
import txtparsing.CranDoc;
import txtparsing.QueryDoc;
import txtparsing.TXTParsing;


public class CreateTermDocMatrix {



	public static void main(String[] args) throws IOException, ParseException {

		String txtfile =  "docs/cran.all.1400";
		String querries =  "docs/cran.qry";
		String [][] A ;
		String [][] tx1;

		try {
			Date start = new Date();
			//   Specify the analyzer for tokenizing text.
			//   The same analyzer should be used for indexing and searching
			//   Specify retrieval model (Vector Space Model)
			EnglishAnalyzer analyzer = new EnglishAnalyzer();
			Similarity similarity = new ClassicSimilarity();

			//   create the index
			Directory index = new RAMDirectory();

			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			config.setSimilarity(similarity);

			FieldType type = new FieldType();
			type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
			type.setTokenized(true);
			type.setStored(true);
			type.setStoreTermVectors(true);

			IndexWriter writer = new IndexWriter(index, config);

			// parse txt document using TXT parser and index it
			List<CranDoc> docs = TXTParsing.parse(txtfile);
			for (CranDoc doc : docs){
				//indexDoc(writer, doc);
				addDocWithTermVector(writer, doc.getBody(), type);
				//.replace("0","").replace("1","").replace("2","").replace("3","").replace("4","").replace("5","").replace("6","").replace("7","").replace("8","").replace("9","")
			}

			writer.close();

			IndexReader reader = DirectoryReader.open(index);
			Terms terms = getTerms(reader);

			A = testSparseFreqDoubleArrayConversion(reader, docs.size(),terms);

			saveArray(A,"d");

			// searcher can only be closed when there
			// is no need to access the documents any more.
			reader.close();


			//querries
			Directory indexQ = new RAMDirectory();

			IndexWriterConfig configQ = new IndexWriterConfig(analyzer);
			config.setSimilarity(similarity);

			IndexWriter writerQ = new IndexWriter(indexQ, configQ);

			// parse txt document using TXT parser and index it
			List<QueryDoc> qrs = TXTParsing.parseQuery(querries);
			for (QueryDoc doc : qrs){
				//indexDoc(writer, doc);
				addDocWithTermVector(writerQ, doc.getBody(), type);
				//.replace("0","").replace("1","").replace("2","").replace("3","").replace("4","").replace("5","").replace("6","").replace("7","").replace("8","").replace("9","")
			}

			writerQ.close();

			IndexReader readerQ = DirectoryReader.open(indexQ);

			tx1 = testSparseFreqDoubleArrayConversion(readerQ, qrs.size(),terms);

			saveArray(tx1,"q");

			// searcher can only be closed when there
			// is no need to access the documents any more.
			readerQ.close();


			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}



	private static void addDocWithTermVector(IndexWriter writer, String value, FieldType type) throws IOException {
		Document doc = new Document();
		//TextField title = new TextField("title", value, Field.Store.YES);
		Field field = new Field("name", value, type);
		doc.add(field);  //this field has term Vector enabled.
		writer.addDocument(doc);
	}

	private static String[][] testSparseFreqDoubleArrayConversion(IndexReader reader, int docN, Terms fieldTerms) throws Exception {
		String [][] B = new String[(int) fieldTerms.size()][docN];
		System.out.println();
		System.out.println();
		if (fieldTerms != null && fieldTerms.size() != -1) {
			IndexSearcher indexSearcher = new IndexSearcher(reader);
			for (ScoreDoc scoreDoc : indexSearcher.search(new MatchAllDocsQuery(), Integer.MAX_VALUE).scoreDocs) {   //retrieves all documents
				System.out.println("DocID: " + scoreDoc.doc);
				Terms docTerms = reader.getTermVector(scoreDoc.doc, "name");

				Double[] vector = DocToDoubleVectorUtils.toSparseLocalFreqDoubleArray(docTerms, fieldTerms); //creates document's vector

				NumberFormat nf = new DecimalFormat("0.#");
				if (vector!=null) {

					for (int i = 0; i <= vector.length - 1; i++) {
						//System.out.print(nf.format(vector[i]) + " ");   //prints document's vector
						if(vector[i] > 1) {
							B[i][scoreDoc.doc] = nf.format(1);
						} else {
							B[i][scoreDoc.doc] = nf.format(vector[i]);
						}
					}
					//System.out.println();

				} else {
					for (int i = 0; i < (int) fieldTerms.size() ; i++) {
						//System.out.print(nf.format(vector[i]) + " ");   //prints document's vector
						B[i][scoreDoc.doc] = nf.format(0);

					}
				}
			}
			System.out.println();
		}
		return B;
	}

	private static void saveArray(String[][] a, String type) {
		try{
			String name;
			if (type.equals("q")){
				name = "tx1.txt";
			}else{
				name = "txdArray.txt";
			}
			FileWriter newFile = new FileWriter(name);
			//Parse txt file
			for (String[] x : a){
				for (String y : x){
					newFile.write(y + " ");
				}
				newFile.write("\n");
			}

		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	private static Terms getTerms(IndexReader reader) throws Exception{
		Terms fieldTerms = MultiFields.getTerms(reader, "name");   //the number of terms in the lexicon after analysis of the Field "title"
		System.out.println("Terms:" + fieldTerms.size());
		TermsEnum it = fieldTerms.iterator();						//iterates through the terms of the lexicon
		while(it.next() != null) {
			System.out.print(it.term().utf8ToString() + "\n"); 		//prints the terms

		}
		return fieldTerms;
	}

}