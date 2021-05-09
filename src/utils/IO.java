package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class IO {

    public static File[] listFiles(String directoryName) {

        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();

        return fList;
    }

    public static String ReadEntireFileIntoAString(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\\A"); //\\A stands for :start of a string
        String entireFileText = scanner.next();
        return entireFileText;
    }

    public static StringBuffer ReadFileIntoAStringLineByLine(String file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        return stringBuffer;
    }

    public String ReadEntireFileIntoAStringCharByChar(String file) throws IOException {

        FileReader fileReader = new FileReader(file);

        String fileContents = "";

        int i;

        while ((i = fileReader.read()) != -1) {
            char ch = (char) i;

            fileContents = fileContents + ch;
        }
        return fileContents;
    }
}