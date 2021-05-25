package txtparsing;

import utils.IO;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TXTParsing {

    public static List<CranDoc> parse(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("\n.I ");
            System.out.println("Read: "+docs.length + " docs");

            //Parse each document from the txt file
            List<CranDoc> parsed_docs= new ArrayList<CranDoc>();
            for (String doc:docs){
                String[] adoc = doc.split("\n(?:\\.[ATBW])");
                CranDoc mydoc = new CranDoc(Integer.parseInt(adoc[0].replace(".I ","").replace("\r","")),adoc[1].substring(1),adoc[2].substring(1),adoc[3].substring(1),adoc[4].substring(1));
                parsed_docs.add(mydoc);
                System.out.println(mydoc.getId());
            }
            return parsed_docs;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }
        
    }

    public static void editQrel(String file) throws Exception {
        try{
            FileWriter newFile = new FileWriter("newcranqrel.txt");
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("\n");
            for (String doc:docs){
                String values[] = doc.split(" ");
                newFile.write(values[0]+ " 0 "+ values[1] + " 1\n");

            }
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        //editQrel("docs/cranqrel");
        List<QueryDoc> test = parseQuery("docs/cran.qry");
        System.out.println();
    }

    public static List<QueryDoc> parseQuery(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("\n.I ");
            System.out.println("Read: "+docs.length + " docs");

            //Parse each document from the txt file
            List<QueryDoc> parsed_docs= new ArrayList<QueryDoc>();
            for (String doc:docs){
                String[] adoc = doc.split("\n(?:\\.[W])");
                QueryDoc mydoc = new QueryDoc(Integer.parseInt(adoc[0].replace(".I ","").replace("\r","")),adoc[1].substring(1));
                parsed_docs.add(mydoc);
                System.out.println(mydoc.getId());
            }
            return parsed_docs;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }

    }




}
